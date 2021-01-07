package com.ruslangrigoriev.chatapp.contacts;

import com.ruslangrigoriev.chatapp.App;
import com.ruslangrigoriev.chatapp.base.BasePresenter;
import com.ruslangrigoriev.chatapp.dao.AuthService;
import com.ruslangrigoriev.chatapp.dao.DataService;
import com.ruslangrigoriev.chatapp.dao.GetContactsCallback;
import com.ruslangrigoriev.chatapp.dao.GetUsersWithChatCallback;
import com.ruslangrigoriev.chatapp.dao.User;
import com.ruslangrigoriev.chatapp.main.MainActivityContract;

import java.util.ArrayList;
import java.util.List;

public class ContactsPresenter extends BasePresenter<ContactsActivityContract.View> implements ContactsActivityContract.Presenter {

    DataService dataService;
    AuthService authService;
    List<User> userList;

    public ContactsPresenter() {
        this.dataService = App.getInstance().dataService;
        this.authService = App.getInstance().authService;
    }



    @Override
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
    }

    @Override
    public void changeStatus(String status) {
        dataService.setStatus(status);
    }
}
