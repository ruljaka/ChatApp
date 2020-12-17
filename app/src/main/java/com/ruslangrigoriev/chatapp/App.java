package com.ruslangrigoriev.chatapp;

import android.app.Application;

import com.ruslangrigoriev.chatapp.dao.AuthService;
import com.ruslangrigoriev.chatapp.dao.FirebaseHelper;
import com.ruslangrigoriev.chatapp.dao.DataService;

public class App extends Application {
    private static App instance;
    public AuthService authService;
    public DataService dataService;
    private FirebaseHelper firebaseHelper;

    public static App getInstance() {
        return instance;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        this.firebaseHelper = new FirebaseHelper();
        this.authService = firebaseHelper;
        this.dataService = firebaseHelper;
    }


}
