package com.ruslangrigoriev.chatapp.messaging;

import com.ruslangrigoriev.chatapp.base.IView;
import com.ruslangrigoriev.chatapp.dao.Message;
import com.ruslangrigoriev.chatapp.dao.User;

import java.util.List;

public interface MessageActivityContract {

    interface View extends IView {
        void setUserInfo(User user);

        void setRecyclerView(List<Message> chats);

        void notifyAdapter();
    }

    interface Presenter {
        void getUserByID(String userID);

        void onBtnSend(String receiverID, String msg);

        void getMessages(String myID, String userID, String imageURL);

        void removeSeenListener();

        void seenMessage(String userID);
    }

}
