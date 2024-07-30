package com.example.chatappli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.chatappli.model.Usermodel;
import com.example.chatappli.utils.AndroidUtil;
import com.example.chatappli.utils.FirebaseUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
      if( getIntent().getExtras()!=null) {
        String userId = getIntent().getExtras().getString("userId");
        FirebaseUtil.allUserCollectinReference().document(userId).get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        Usermodel model = task.getResult().toObject(Usermodel.class);
                        Intent mainIntent = new Intent(this,MainActivity.class);
                        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(mainIntent);


                        Intent intent=new Intent(this, ChatActivity.class);
                        AndroidUtil.passUserModelAsIntent(intent,model);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

      }else{
          new Handler().postDelayed(new Runnable() {
              @Override
              public void run() {
                  if (FirebaseUtil.isLoggedin()) {
                      startActivity(new Intent(getApplicationContext(), MainActivity.class));
                  } else {
                      startActivity(new Intent(getApplicationContext(), LoginPhoneno.class));
                  }
                  finish();
              }
          },1000);

      }
    }
}