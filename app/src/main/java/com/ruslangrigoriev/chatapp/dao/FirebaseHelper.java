package com.ruslangrigoriev.chatapp.dao;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.ruslangrigoriev.chatapp.App;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseHelper implements AuthService, DataService {
    private static final String TAG = "MyTag";


    String currentUID;
    FirebaseAuth firebaseAuth;
    DatabaseReference usersReference;
    DatabaseReference chatsReference;
    StorageReference storageReference;

    private StorageTask uploadTask;

    List<User> usersWithChatList;

    public FirebaseHelper() {
        this.usersWithChatList = new ArrayList<>();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.usersReference = FirebaseDatabase
                .getInstance()
                .getReference("Users");
        this.chatsReference = FirebaseDatabase
                .getInstance()
                .getReference("Chats");
        this.storageReference = FirebaseStorage
                .getInstance()
                .getReference("Uploads");

    }

    @Override
    public void register(String username, String email, String password, AuthCallback authCallback) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String userID = firebaseUser.getUid();
                            currentUID = userID;

                            /*userReference = FirebaseDatabase
                                    .getInstance()
                                    .getReference("Users").child(userID);*/

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userID);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

                            usersReference.child(userID).setValue(hashMap)
                                    .addOnCompleteListener(task1 -> {
                                        if (task1.isSuccessful()) {
                                            authCallback.onSuccess();
                                        }
                                    });
                            Log.d(TAG, "register onComplete");
                        } else {
                            authCallback.onError();
                        }
                    }
                });
    }

    @Override
    public void login(String email, String password, AuthCallback authCallback) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        authCallback.onSuccess();
                        Log.d(TAG, "login isSuccessful");
                    } else {
                        authCallback.onError();
                    }
                });
    }

    @Override
    public boolean checkSignedIn() {
        if (firebaseAuth.getCurrentUser() != null) {
            currentUID = firebaseAuth.getCurrentUser().getUid();
            Log.d(TAG, "checkSignedIn true");
            return true;
        }
        Log.d(TAG, "checkSignedIn false");
        return false;

    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
        Log.d(TAG, "logout");
    }

    @Override
    public String getCurrentUserUID() {
        Log.d(TAG, "getCurrentUserUID");
        return firebaseAuth.getCurrentUser().getUid();

    }


    @Override
    public void getContacts(GetContactsCallback contactsCallback) {
        List<User> users = new ArrayList<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        /*usersReference = FirebaseDatabase
                .getInstance()
                .getReference("Users");*/

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange  getContacts");
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (!user.getId().equals(firebaseUser.getUid())) {
                        users.add(user);
                    }
                }
                contactsCallback.onChange(users);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }


    @Override
    public void getUserByID(String userID, GetUserByIDCallback userByIDCallback) {

        usersReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange  getUserByID");

                User user = snapshot.getValue(User.class);
                userByIDCallback.onChange(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void sendMessage(String sender, String receiver, String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("messageText", message);
        chatsReference.push().setValue(hashMap);
        Log.d(TAG, "sendMessage");
    }

    @Override
    public void readMessage(String myID, String userID, String imageURL, ReadMessageCallback readMessageCallback) {
        ArrayList<Chat> chats = new ArrayList<>();
        chatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG, "onDataChange  readMessage");
                chats.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myID) && chat.getSender().equals(userID)
                            || chat.getReceiver().equals(userID) && chat.getSender().equals(myID)) {
                        chats.add(chat);
                    }
                }
                readMessageCallback.onChange(chats);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void getChatUsersList(GetUsersWithChatCallback chatUsersCallback) {
        ArrayList<String> chatedUsersIDsList = new ArrayList<>();

        chatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatedUsersIDsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getSender().equals(currentUID)) {
                        chatedUsersIDsList.add(chat.getReceiver());
                    }
                    if (chat.getReceiver().equals(currentUID)) {
                        chatedUsersIDsList.add(chat.getSender());
                    }
                }

                readChats(chatedUsersIDsList, chatUsersCallback);
                Log.d(TAG, "onDataChange  getChatUsersList");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readChats(ArrayList<String> chatedUsersList, GetUsersWithChatCallback usersWithChatCallback) {

        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                usersWithChatList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    for (String ID : chatedUsersList) {
                        if (user.getId().equals(ID)) {
                            if (!usersWithChatList.contains(user)) {
                                usersWithChatList.add(user);
                            }
                        }
                    }
                }
                usersWithChatCallback.onChange(usersWithChatList);
                Log.d(TAG, "onDataChange  readChats");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = App.getInstance().getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void uploadImage(Uri imageUri) {
        if (imageUri != null) {
            StorageReference fileReference = storageReference.child((System.currentTimeMillis())
                    + "." + getFileExtension(imageUri));
            uploadTask = fileReference.putFile(imageUri);
            uploadTask.continueWithTask((Continuation<UploadTask.TaskSnapshot, Task<Uri>>) task -> {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return fileReference.getDownloadUrl();
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String uri = downloadUri.toString();
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", uri);
                        usersReference.child(currentUID).updateChildren(map);
                        Log.d(TAG, "onComplete  uploadImage");
                    } else {
                        Log.d(TAG, "downloadImage Failed");
                        Toast.makeText(App.getInstance().getApplicationContext(), "downloadImage Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "onFailure  uploadImage");
                    Toast.makeText(App.getInstance().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(App.getInstance().getApplicationContext(), "No image selected", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setStatus(String status) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);
        if(currentUID != null) {
            usersReference.child(currentUID).updateChildren(hashMap);
            Log.d(TAG, "setStatus " + status);
        }

    }


}

