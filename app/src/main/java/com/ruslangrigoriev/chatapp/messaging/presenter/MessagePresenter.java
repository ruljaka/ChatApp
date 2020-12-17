package com.ruslangrigoriev.chatapp.messaging.presenter;

import android.util.Log;

import com.ruslangrigoriev.chatapp.App;
import com.ruslangrigoriev.chatapp.base.BasePresenter;
import com.ruslangrigoriev.chatapp.dao.AuthService;
import com.ruslangrigoriev.chatapp.dao.Chat;
import com.ruslangrigoriev.chatapp.dao.DataService;
import com.ruslangrigoriev.chatapp.dao.GetUserByIDCallback;
import com.ruslangrigoriev.chatapp.dao.ReadMessageCallback;
import com.ruslangrigoriev.chatapp.dao.User;
import com.ruslangrigoriev.chatapp.messaging.MessageActivityContract;

import java.util.ArrayList;
import java.util.List;

public class MessagePresenter extends BasePresenter<MessageActivityContract.View> implements MessageActivityContract.Presenter {

    DataService dataService;
    AuthService authService;

    List<Chat> chatList;

    public MessagePresenter() {
        this.dataService = App.getInstance().dataService;
        this.authService = App.getInstance().authService;
    }


    @Override
    public void getUserByID(String userID) {
        dataService.getUserByID(userID, new GetUserByIDCallback() {
            @Override
            public void onChange(User user) {
                view.setUserInfo(user);
                getMessages(authService.getCurrentUserUID(), userID, user.getImageURL());
            }
        });
    }

    @Override
    public void onBtnSend(String receiverID, String msg) {
        dataService.sendMessage(authService.getCurrentUserUID(), receiverID, msg);
    }

    @Override
    public void getMessages(String myID, String userID, String imageURL) {
        dataService.readMessage(myID, userID, imageURL, new ReadMessageCallback() {
            @Override
            public void onChange(List<Chat> chats) {
                if (view != null) {
                    if (chatList == null) {
                        chatList = new ArrayList<>();
                        chatList.addAll(chats);
                        view.setRecyclerView(chatList);
                    } else {
                        //Log.d("MyTag","onChange if != null");
                        chatList.clear();
                        chatList.addAll(chats);
                        view.notifyAdapter();
                    }
                }
            }
        });
    }
}