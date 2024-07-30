package com.example.chatappli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.hbb20.CountryCodePicker;

public class LoginPhoneno extends AppCompatActivity {
    EditText phnInput;
    Button sendotp;
    CountryCodePicker countryCodePicker;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_phoneno);

        sendotp=findViewById(R.id.senotp_butoon);
        progressBar=findViewById(R.id.login_progressbar);
        phnInput=findViewById(R.id.login_mobile_no);
        countryCodePicker=findViewById(R.id.countryCodeHolder);

        progressBar.setVisibility(View.GONE);

        countryCodePicker.registerCarrierNumberEditText(phnInput);

        sendotp.setOnClickListener(v ->{
            if (!countryCodePicker.isValidFullNumber()){
                phnInput.setError("Number Invalid");
                return;
            }
            Intent intent=new Intent(getApplicationContext(), LoginOTP.class);
            intent.putExtra("phone",countryCodePicker.getFullNumberWithPlus());
            startActivity(intent);
        });
    }
}