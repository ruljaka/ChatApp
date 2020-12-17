package com.ruslangrigoriev.chatapp.dao;

import java.util.List;

public interface GetContactsCallback {
    void onChange(List<User> users);

    //void onCancel();
}
