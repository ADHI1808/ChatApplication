package com.example.chatappli.utils;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.List;

public class FirebaseUtil {
    public  static String currentUserid(){
        return FirebaseAuth.getInstance().getUid();
    }
    public  static DocumentReference currentuserDetails(){
        return FirebaseFirestore.getInstance().collection("users").document(currentUserid());

    }
    public static boolean isLoggedin(){
        if (currentUserid()!=null){
            return true;
        }
        return false;
    }

    public static CollectionReference allUserCollectinReference(){
        return FirebaseFirestore.getInstance().collection("users");
    }

    public static DocumentReference getChatroomReference(String chatRoomId){
        return FirebaseFirestore.getInstance().collection("chatrooms").document(chatRoomId);
    }
    public static CollectionReference getChatRoomMessageReference(String chatroomId){
        return  getChatroomReference(chatroomId).collection("chats");
    }

    public  static String getChatroomId(String userId1,String userId2){
        if (userId1.hashCode()<userId2.hashCode()){
            return userId1+"_"+userId2;
        }else {
            return userId2+"_"+userId1;
        }
    }
    public static CollectionReference allChatroomCollectionReference(){
        return FirebaseFirestore.getInstance().collection("chatrooms");
    }
    public static DocumentReference getOtherUserFromChatroom(List<String> userIds){
        if (userIds.get(0).equals(FirebaseUtil.currentUserid())){
            return allUserCollectinReference().document(userIds.get(1));
        }
        else {
            return allUserCollectinReference().document(userIds.get(0));
        }
    }
    public static String timestampToString(Timestamp timestamp){
        return new SimpleDateFormat("HH:MM").format(timestamp.toDate());
    }
    public static void logout(){
        FirebaseAuth.getInstance().signOut();
    }
    public static StorageReference getCurrentProfilePicStorageRef(){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(FirebaseUtil.currentUserid());
    }
    public static StorageReference getOtherProfilePicStorageRef(String otherUserid){
        return FirebaseStorage.getInstance().getReference().child("profile_pic")
                .child(otherUserid);
    }

}
