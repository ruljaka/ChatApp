package com.ruslangrigoriev.chatapp.main;

import com.ruslangrigoriev.chatapp.App;
import com.ruslangrigoriev.chatapp.base.BasePresenter;
import com.ruslangrigoriev.chatapp.dao.AuthService;
import com.ruslangrigoriev.chatapp.dao.DataService;
import com.ruslangrigoriev.chatapp.dao.GetUsersWithChatCallback;
import com.ruslangrigoriev.chatapp.dao.User;
import com.ruslangrigoriev.chatapp.main.MainActivityContract;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter extends BasePresenter<MainActivityContract.View> implements MainActivityContract.Presenter {

    DataService dataService;
    AuthService authService;
    List<User> usersWithChatList;


    public MainPresenter() {
        this.dataService = App.getInstance().dataService;
        this.authService = App.getInstance().authService;
    }

    @Override
    public boolean checkSignedIn() {
        if (!authService.checkSignedIn()) {
            view.startStartActivity();
            return false;
        }
        return true;
    }

    @Override
    public void onLogout() {
        authService.logout();
        view.startStartActivity();
    }

    @Override
    public void onSettings() {
        //TODO  settings
        view.startSettingsActivity();
    }


    /*@Override
    public void getContactList() {
        //view.showLoader();
        dataService.getContacts(new GetContactsCallback() {
            @Override
            public void onChange(List<User> users) {
                if (userList == null) {
                    view.showLoader();
                    userList = new ArrayList<>();
                    userList.addAll(users);
                    view.setContactsRV(userList);
                    view.hideLoader();
                } else {
                    userList.clear();
                    userList.addAll(users);
                    if(view != null) {
                        view.notifyContactsAdapter();
                    }
                }
            }
        });
    }*/

    @Override
    public void getChatUsersList() {
        if(view != null){
            view.showLoader();
        }
        dataService.getChatUsersList(new GetUsersWithChatCallback() {
            @Override
            public void onChange(List<User> users) {
                if (usersWithChatList == null) {
                    usersWithChatList = new ArrayList<>();
                    usersWithChatList.addAll(users);
                    view.setChatsRV(usersWithChatList);
                    view.hideLoader();
                } else {
                    usersWithChatList.clear();
                    usersWithChatList.addAll(users);
                    if (view != null) {
                        view.notifyChatsAdapter();
                        view.hideLoader();
                    }

                }
            }
        });
    }

    @Override
    public void changeStatus(String status) {
        dataService.setStatus(status);
    }


}
