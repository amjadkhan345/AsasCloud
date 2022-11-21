package com.asas.cloud.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.asas.cloud.R;

public class Otp_loginActivity extends AppCompatActivity {

    TextView otp_text;
    Button login;
    String otp_from_server, otp_from_email;
    String from, otp_textview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_login);

        otp_text=findViewById(R.id.otp_txt);
        login= findViewById(R.id.login_otp);
        otp_from_server = getIntent().getStringExtra("otp");
        from = getIntent().getStringExtra("type");


            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (from.equals("1")) {
                        //loginwithotp(otp);
                        otp_textview = otp_text.getText().toString();
                        if(otp_textview.equals(otp_from_server)){
                            SharedPreferences sharedPref = Otp_loginActivity.this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("lockd_no", "10");
                            editor.putBoolean("lockd", false);
                            editor.apply();
                            startActivity(new Intent(Otp_loginActivity.this, MainActivity.class));
                            finish();
                        }else {
                            otp_text.setText("Your OTP not much");
                        }


                    }else {
                        resatePassword();
                    }
                }

            });


    }

    private void resatePassword() {
    }


}