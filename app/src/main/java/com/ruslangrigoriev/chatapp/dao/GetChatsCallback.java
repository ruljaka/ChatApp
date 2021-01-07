package com.ruslangrigoriev.chatapp.dao;

import java.util.List;

public interface GetChatsCallback {
    void onChange(List<User> users);

    //void onCancel();
}
