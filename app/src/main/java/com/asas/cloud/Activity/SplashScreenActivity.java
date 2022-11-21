package com.asas.cloud.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.asas.cloud.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreenActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        SharedPreferences sharedPref = this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
        String name = sharedPref.getString("lockd_no", "");

        boolean is=sharedPref.getBoolean("lockd", false);
        //if (name) {
            //startActivity(new Intent(SplashScreenActivity.this, LoginWithPasswordActivity.class));
          //  Intent intent = new Intent(this, LoginWithPasswordActivity.class);
            //startActivity(intent);

            //}else if(name.equals("false")) {


        //}else{
          //    SharedPreferences.Editor editor = sharedPref.edit();
            //editor.putBoolean("laked", false);
            //editor.apply();
            //Toast.makeText(this, " false", Toast.LENGTH_SHORT).show();
        //}


        Intent intent;
        if (user != null) {
            //intent = new Intent(this, MainActivity.class);
            if (is) {
                if (name.equals("1")) {

                    intent = new Intent(this, LoginWithPasswordActivity.class);
                } else if (name.equals("2")) {
                    intent = new Intent(this, ChackPoscodeActivity.class);
                } else if (name.equals("3")) {
                    intent = new Intent(this, BiomatricActivity.class);
                } else
                    intent = new Intent(this, LoginActivity.class);
            }else
                intent = new Intent(this, MainActivity.class);


        }else {
            intent = new Intent(this, LoginActivity.class);

        }
        startActivity(intent);
        finish();
    }
}