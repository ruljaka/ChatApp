package com.ruslangrigoriev.chatapp.main;

import com.ruslangrigoriev.chatapp.base.IView;
import com.ruslangrigoriev.chatapp.dao.User;

import java.util.List;

public interface MainActivityContract {
    interface View extends IView {
        void startStartActivity();
        void startSettingsActivity();
        void setContactsRV(List<User> userList);
        void notifyContactsAdapter();
        void setChatsRV(List<User> userList);
        void notifyChatsAdapter();
    }

    interface Presenter {
        void onLogout();
        void onSettings();
        void getContactList();
        void checkSignedIn();
        void getChatUsersList();

    }
}
