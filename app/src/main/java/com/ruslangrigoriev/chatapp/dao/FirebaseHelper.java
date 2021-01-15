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
import com.google.firebase.database.Query;
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
    DatabaseReference messagesReference;
    DatabaseReference chatsReference;
    StorageReference storageReference;


    ValueEventListener seenListener;

    private StorageTask uploadTask;

    List<User> userListFromChats;

    public FirebaseHelper() {
        this.userListFromChats = new ArrayList<>();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.usersReference = FirebaseDatabase
                .getInstance()
                .getReference("Users");
        this.messagesReference = FirebaseDatabase
                .getInstance()
                .getReference("Messages");
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
                            hashMap.put("search", username.toLowerCase());

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
    public void resetPassword(String email) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(App.getInstance().getApplicationContext(), "Please check your Email", Toast.LENGTH_SHORT).show();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(App.getInstance().getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public void searchContacts(String s, GetContactsCallback contactsCallback) {
        List<User> users = new ArrayList<>();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        Query query = usersReference.orderByChild("search")
                .startAt(s)
                .endAt(s + "\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user != null) {
                        if (!user.getId().equals(firebaseUser.getUid())) {
                            users.add(user);
                        }
                    }
                }
                contactsCallback.onChange(users);
                Log.d(TAG, "onDataChange  searchContacts");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
    public void seenMessage(String userID) {
        seenListener = messagesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange  seenMessage");
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message.getReceiver().equals(currentUID) && message.getSender().equals(userID)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void removeSeenListener() {
        messagesReference.removeEventListener(seenListener);
    }

    @Override
    public void sendMessage(String sender, String receiver, String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("messageText", message);
        hashMap.put("isSeen", false);
        messagesReference.push().setValue(hashMap);
        Log.d(TAG, "sendMessage");

        chatsReference = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(sender)
                .child(receiver);

        chatsReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    chatsReference.child("id").setValue(receiver);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void readMessage(String myID, String userID, String imageURL, ReadMessageCallback readMessageCallback) {
        ArrayList<Message> messages = new ArrayList<>();
        messagesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange  readMessage");
                messages.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Message message = snapshot.getValue(Message.class);
                    if (message.getReceiver().equals(myID) && message.getSender().equals(userID)
                            || message.getReceiver().equals(userID) && message.getSender().equals(myID)) {
                        messages.add(message);
                    }
                }
                readMessageCallback.onChange(messages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void getChats(GetChatsCallback getChatsCallback) {
        ArrayList<ChatList> chats = new ArrayList<>();

        chatsReference = FirebaseDatabase.getInstance().getReference("ChatList").child(getCurrentUserUID());
        chatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatList chatlist = snapshot.getValue(ChatList.class);
                    chats.add(chatlist);
                }
                getUserListFromChats(chats, getChatsCallback);
                Log.d(TAG, "onDataChange  getChats");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getUserListFromChats(ArrayList<ChatList> chats, GetChatsCallback getChatsCallback) {
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userListFromChats.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    for (ChatList chatlist : chats) {
                        if (user.getId().equals(chatlist.getId())) {
                            userListFromChats.add(user);
                        }
                    }
                }
                getChatsCallback.onChange(userListFromChats);
                Log.d(TAG, "onDataChange  getUserListFromChats");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
        if (currentUID != null) {
            usersReference.child(currentUID).updateChildren(hashMap);
            Log.d(TAG, "setStatus " + status);
        }

    }


}

