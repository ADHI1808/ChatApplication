package com.example.chatappli.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.chatappli.model.Usermodel;

public class AndroidUtil {
    public static void showToast(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show();
    }
    public static void  passUserModelAsIntent(Intent intent, Usermodel model){
        intent.putExtra("username",model.getUsername());
        intent.putExtra("phone",model.getPhno());
        intent.putExtra("userId",model.getUserId());
        intent.putExtra("fcmToken",model.getUserId());

    }

    public  static Usermodel getUserModelFromIntent(Intent intent){
        Usermodel userModel = new Usermodel();
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhno(intent.getStringExtra("phone"));
        userModel.setUserId(intent.getStringExtra("userId"));
        return userModel;
    }
    public static void setProfilepic(Context context, Uri ImageURI, ImageView imageView){
        Glide.with(context).load(ImageURI).apply(RequestOptions.circleCropTransform()).into(imageView);
    }
}
