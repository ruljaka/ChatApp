package com.ruslangrigoriev.chatapp.dao;

public interface AuthService {
    void register(String username, String email, String password, AuthCallback authCallback);

    void login(String email, String password, AuthCallback authCallback);

    boolean checkSignedIn();

    void logout();

    String getCurrentUserUID();

    void resetPassword(String email);
}

