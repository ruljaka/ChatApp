package com.ruslangrigoriev.chatapp.dao;

public class Message {
    private String sender;
    private String receiver;
    private String messageText;
    private boolean isSeen;

    public Message(String sender, String receiver, String messageText, boolean isSeen) {
        this.sender = sender;
        this.receiver = receiver;
        this.messageText = messageText;
        this.isSeen = isSeen;
    }

    public Message() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public boolean getIsSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }
}
