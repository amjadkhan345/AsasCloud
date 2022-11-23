package com.asas.cloud.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.asas.cloud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;
    boolean is;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        SharedPreferences sharedPref = this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
        name = sharedPref.getString("lockd_no", "");

         is=sharedPref.getBoolean("lockd", false);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                myintent();
            }
        }, 3000);




    }

    private void myintent() {
        Intent intent;
        if (user != null) {
            //intent = new Intent(this, MainActivity.class);
            if (is) {
                switch (name) {
                    case "1":
                        intent = new Intent(this, LoginWithPasswordActivity.class);
                        break;
                    case "2":
                        intent = new Intent(this, ChackPoscodeActivity.class);
                        break;
                    case "3":
                        intent = new Intent(this, BiomatricActivity.class);
                        break;
                    default:
                        intent = new Intent(this, LoginActivity.class);
                        break;
                }
            }else
                intent = new Intent(this, MainActivity.class);


        }else {
            intent = new Intent(this, LoginActivity.class);

        }
        startActivity(intent);
        finish();



    }
}