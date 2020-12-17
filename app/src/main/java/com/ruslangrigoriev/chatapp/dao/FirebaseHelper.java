package com.ruslangrigoriev.chatapp.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirebaseHelper implements AuthService, DataService {
    private static final String TAG = "MyTag";

    FirebaseAuth firebaseAuth;
    DatabaseReference usersReference;
    DatabaseReference chatsReference;

    List<User> usersWithChatList;

    public FirebaseHelper() {
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.usersReference = FirebaseDatabase
                .getInstance()
                .getReference("Users");
        this.chatsReference = FirebaseDatabase
                .getInstance()
                .getReference("Chats");
        this.usersWithChatList = new ArrayList<>();
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
                    } else {
                        authCallback.onError();
                    }
                });
    }

    @Override
    public boolean checkSignedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    @Override
    public void logout() {
        firebaseAuth.signOut();
    }

    @Override
    public String getCurrentUserUID() {
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
                Log.d(TAG,"onDataChange  getContacts");
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
        /*usersReference = FirebaseDatabase
                .getInstance()
                .getReference("Users").child(userID);*/
        usersReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"onDataChange  getUserByID");

                User user = snapshot.getValue(User.class);
                userByIDCallback.onChange(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    @Override
    public void sendMessage(String sender, String receiver, String message) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        chatsReference.push().setValue(hashMap);
    }

    @Override
    public void readMessage(String myID, String userID, String imageURL, ReadMessageCallback readMessageCallback) {
        ArrayList<Chat> chats = new ArrayList<>();
        chatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d(TAG,"onDataChange  readMessage");
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
                Log.d(TAG,"onDataChange  getChatUsersList");
                chatedUsersIDsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getSender().equals(getCurrentUserUID())){
                        chatedUsersIDsList.add(chat.getReceiver());
                    }
                    if(chat.getReceiver().equals(getCurrentUserUID())){
                        chatedUsersIDsList.add(chat.getSender());
                    }
                }
                
                readChats(chatedUsersIDsList, chatUsersCallback);
                Log.d(TAG,"onDataChange  getChatUsersList");
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
                Log.d(TAG,"onDataChange  readChats");

                usersWithChatList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);

                    for(String ID : chatedUsersList){
                        if(user.getId().equals(ID)){
                            if(!usersWithChatList.contains(user)){
                                usersWithChatList.add(user);
                            }
                        }
                    }
                }
                usersWithChatCallback.onChange(usersWithChatList);
                Log.d(TAG,"onDataChange  readChats");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}

