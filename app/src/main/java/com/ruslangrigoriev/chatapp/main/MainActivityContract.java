package com.ruslangrigoriev.chatapp.main;

import com.ruslangrigoriev.chatapp.base.IView;
import com.ruslangrigoriev.chatapp.dao.User;

import java.util.List;

public interface MainActivityContract {
    interface View extends IView {
        void startStartActivity();

        void startSettingsActivity();

        void setChatsRV(List<User> userList);

        void notifyChatsAdapter();
    }

    interface Presenter {
        void onLogout();

        void onSettings();

        boolean checkSignedIn();

        void getChatUsersList();

    }
}
