package com.ruslangrigoriev.chatapp.dao;

import java.util.List;

public interface DataCallback {
    void onChange(List<User> users);

    //void onCancel();
}
