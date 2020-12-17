package com.ruslangrigoriev.chatapp.dao;

import java.util.List;

public interface ReadMessageCallback {
    void onChange(List<Chat> chats);

    //void onCancel();
}
