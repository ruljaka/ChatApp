package com.ruslangrigoriev.chatapp.contacts;

import com.ruslangrigoriev.chatapp.base.IView;
import com.ruslangrigoriev.chatapp.dao.User;

import java.util.List;

public interface ContactsActivityContract {
    interface View extends IView {
        void setContactsRV(List<User> userList);
        void notifyContactsAdapter();
    }

    interface Presenter {
        //void getContactList();

        void searchUsers(String s);
    }
}
