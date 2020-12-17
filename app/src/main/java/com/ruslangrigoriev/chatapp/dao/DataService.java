package com.ruslangrigoriev.chatapp.dao;

public interface DataService {

    void getContacts(GetContactsCallback contactsCallback);

    void getUserByID(String userID, GetUserByIDCallback userByIDCallback);

    void sendMessage(String sender, String receiver, String message);

    void readMessage(String myID, String userID, String imageURL, ReadMessageCallback readMessageCallback);

    void getChatUsersList(GetUsersWithChatCallback chatUsersCallback);
}
