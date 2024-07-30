package com.example.chatappli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatappli.utils.AndroidUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class LoginOTP extends AppCompatActivity {

    String phno;
    EditText otpInput;
    Button nextbutton;
    TextView resendOtp;
    ProgressBar progressBar;
    FirebaseAuth mAuth=FirebaseAuth.getInstance();
    Long timeoutsec=60L;
    String verificationcode;
    PhoneAuthProvider.ForceResendingToken ResendingToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        otpInput=findViewById(R.id.login_otp);
        nextbutton=findViewById(R.id.next_button);
        progressBar=findViewById(R.id.login_progressbar);
        resendOtp=findViewById(R.id.resend_otp_text);

        phno=getIntent().getExtras().getString("phone");

        sendOtp(phno,true);

        nextbutton.setOnClickListener(v ->{
            String EnteredOTP=otpInput.getText().toString();
            PhoneAuthCredential credential= PhoneAuthProvider.getCredential(verificationcode,EnteredOTP);
            signin(credential);
        });

        resendOtp.setOnClickListener(v ->{
            sendOtp(phno,true);
        });
    }
   void sendOtp(String phno,boolean isResend){
        startResendTimer();
        setInprogress(true);
       PhoneAuthOptions.Builder builder=
               PhoneAuthOptions.newBuilder(mAuth)
                       .setPhoneNumber(phno)
                       .setTimeout(timeoutsec,TimeUnit.SECONDS)
                       .setActivity(this)
                       .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                   @Override
                   public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                       signin(phoneAuthCredential);
                       setInprogress(false);
                   }

                   @Override
                   public void onVerificationFailed(@NonNull FirebaseException e) {
                       AndroidUtil.showToast(getApplicationContext(),"Verification Failed");
                       setInprogress(false);
                   }

                   @Override
                   public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                       super.onCodeSent(s, forceResendingToken);
                       verificationcode =s;
                       ResendingToken=forceResendingToken;
                       AndroidUtil.showToast(getApplicationContext(),"OTP Sent Sucessfully");
                       setInprogress(false);
                   }
               });
       if (isResend){
           PhoneAuthProvider.verifyPhoneNumber(builder.setForceResendingToken(ResendingToken).build());
       }
       else {
           PhoneAuthProvider.verifyPhoneNumber(builder.build());
       }
    }
    void setInprogress(boolean inprogress){
        if (inprogress) {
            progressBar.setVisibility(View.VISIBLE);
            nextbutton.setVisibility(View.GONE);
        }else {
            progressBar.setVisibility(View.GONE);
            nextbutton.setVisibility(View.VISIBLE);
        }
    }
    void signin(PhoneAuthCredential phoneAuthCredential){
            setInprogress(true);
            mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    setInprogress(false);
                    if (task.isSuccessful()){
                        Intent intent=new Intent(getApplicationContext(), LoginUsername.class);
                        intent.putExtra("phone",phno);
                        startActivity(intent);
                    }else{
                        AndroidUtil.showToast(getApplicationContext(),"Verification Failded");
                    }
                }
            });
    }
    void startResendTimer(){
        resendOtp.setEnabled(false);
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeoutsec--;
                resendOtp.setText("Resend OTP in"+timeoutsec+" seconds");
                if (timeoutsec<=0){
                    timeoutsec=60L;
                    timer.cancel();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            resendOtp.setEnabled(true);
                        }
                    });
                }
            }
        },0,1000);
    }
}