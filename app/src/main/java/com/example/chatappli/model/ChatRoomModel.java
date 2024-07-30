package com.example.chatappli.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class ChatRoomModel {
    String chatRoomId;
    List<String> userId;
    Timestamp lasmessageTimeStamp;
    String lastMessageSenderId;
    String lastmessage;

    public ChatRoomModel() {
    }

    public ChatRoomModel(String chatRoomId,List<String> userId,Timestamp lasmessageTimeStamp,String lastMessageSenderId) {
        this.chatRoomId = chatRoomId;
        this.userId=userId;
        this.lasmessageTimeStamp=lasmessageTimeStamp;
        this.lastMessageSenderId=lastMessageSenderId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public void setChatRoomId(String chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

    public List<String> getUserId() {
        return userId;
    }

    public void setUserId(List<String> userId) {
        this.userId = userId;
    }

    public Timestamp getLasmessageTimeStamp() {
        return lasmessageTimeStamp;
    }

    public void setLasmessageTimeStamp(Timestamp lasmessageTimeStamp) {
        this.lasmessageTimeStamp = lasmessageTimeStamp;
    }

    public String getLastMessageSenderId() {
        return lastMessageSenderId;
    }

    public void setLastMessageSenderId(String lastMessageSenderId) {
        this.lastMessageSenderId = lastMessageSenderId;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }
}
