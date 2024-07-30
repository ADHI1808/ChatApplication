package com.example.chatappli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.example.chatappli.model.Usermodel;
import com.example.chatappli.utils.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;

public class LoginUsername extends AppCompatActivity {

    EditText usernameInput;
    Button letmeinbtn;
    ProgressBar progressBar;
    String phno;
    Usermodel usermodel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_username);

        usernameInput=findViewById(R.id.login_user_name);
        letmeinbtn=findViewById(R.id.login_letmein_button);
        progressBar=findViewById(R.id.login_progressbar);

        phno=getIntent().getExtras().getString("phone");
        getUsername();
        letmeinbtn.setOnClickListener(v ->{
            setUsername();
        });
    }
    void setUsername(){
        String username=usernameInput.getText().toString();
        if (username.isEmpty() || username.length()<3){
            usernameInput.setError("Username Should Be Atleast 3 Chars");
            return;
        }
        setInprogress(true);
        if (usermodel!=null){
            usermodel.setUsername(username);
        }else {
            usermodel =new Usermodel(phno,username, Timestamp.now(),FirebaseUtil.currentUserid());
        }
        FirebaseUtil.currentuserDetails().set(usermodel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
           setInprogress(false);
           if (task.isSuccessful()){
               Intent intent=new Intent(getApplicationContext(), MainActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(intent);
           }
            }
        });
    }
    void  getUsername(){
        setInprogress(true);
        FirebaseUtil.currentuserDetails().get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                setInprogress(false);
                if (task.isSuccessful()){
                  usermodel=  task.getResult().toObject(Usermodel.class);
                  if (usermodel !=null){
                      usernameInput.setText(usermodel.getUsername());
                  }
                }
            }
        });
    }
    void setInprogress(boolean inprogress){
        if (inprogress) {
            progressBar.setVisibility(View.VISIBLE);
            letmeinbtn.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            letmeinbtn.setVisibility(View.VISIBLE);
        }
    }
}