package com.ruslangrigoriev.chatapp.Authentication.presenter;

import com.ruslangrigoriev.chatapp.App;
import com.ruslangrigoriev.chatapp.Authentication.StartActivityContract;
import com.ruslangrigoriev.chatapp.dao.AuthCallback;
import com.ruslangrigoriev.chatapp.dao.AuthService;
import com.ruslangrigoriev.chatapp.base.BasePresenter;

public class AuthPresenter extends BasePresenter<StartActivityContract.View> implements StartActivityContract.Presenter {
    //private final AuthView authView;
    AuthService authService;

    public AuthPresenter() {
        //this.authView = view;
        this.authService = App.getInstance().authService;
    }

    public void onClickRegister(String username, String email, String password) {
        view.showLoader();
        authService.register(username, email, password, new AuthCallback() {
            @Override
            public void onSuccess() {
                view.hideLoader();
                view.startMainActivity();
                view.makeToast("Registration complete");
            }

            @Override
            public void onError() {
                view.hideLoader();
                view.makeToast("You can't register with this email or password");
            }
        });
    }

    public void onClickLogin(String email, String password) {
        view.showLoader();
        authService.login(email, password, new AuthCallback() {
            @Override
            public void onSuccess() {
                view.hideLoader();
                view.startMainActivity();
                view.makeToast("Login complete");
            }

            @Override
            public void onError() {
                view.hideLoader();
                view.makeToast("Authentication failed");
            }
        });
    }

}
