package com.ruslangrigoriev.chatapp.dao;

import java.util.List;

public interface GetUsersWithChatCallback {
    void onChange(List<User> users);

    //void onCancel();
}
