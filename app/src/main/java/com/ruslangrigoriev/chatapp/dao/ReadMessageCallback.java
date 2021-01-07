package com.ruslangrigoriev.chatapp.dao;

import java.util.List;

public interface ReadMessageCallback {
    void onChange(List<Message> chats);

    //void onCancel();
}
