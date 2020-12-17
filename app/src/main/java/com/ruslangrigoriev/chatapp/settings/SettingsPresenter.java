package com.ruslangrigoriev.chatapp.settings;

import com.ruslangrigoriev.chatapp.App;
import com.ruslangrigoriev.chatapp.base.BasePresenter;
import com.ruslangrigoriev.chatapp.dao.AuthService;
import com.ruslangrigoriev.chatapp.dao.DataService;
import com.ruslangrigoriev.chatapp.dao.GetUserByIDCallback;
import com.ruslangrigoriev.chatapp.dao.User;

public class SettingsPresenter extends BasePresenter<SettingsActivityContract.View> implements SettingsActivityContract.Presenter {

    DataService dataService;
    AuthService authService;

    public SettingsPresenter() {
        this.dataService = App.getInstance().dataService;
        this.authService = App.getInstance().authService;
    }


    @Override
    public void getCurrentUser() {
        dataService.getUserByID(authService.getCurrentUserUID(), new GetUserByIDCallback() {
            @Override
            public void onChange(User user) {
                view.setSettingsFragmentData(user);
            }
        });
    }
}
