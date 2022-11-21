package com.asas.cloud.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.asas.cloud.R;
import com.asas.cloud.classes.CallApi;
import com.asas.cloud.classes.Uttilties;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.asas.cloud.classes.SendMail;

//import com.asas.cloud.classes.SendMail;
//import com.asas.cloud.classes.SendMail;

public class LoginWithPasswordActivity extends AppCompatActivity {
    TextView txt, forgetp;
    Button btn;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    String useremail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_with_password);
        txt=findViewById(R.id.password_txt);
        btn = findViewById(R.id.btn_conf);
        forgetp=findViewById(R.id.porget_password);
        database = FirebaseDatabase.getInstance();
        reference=database.getReference();
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        String userId= user.getUid();
        reference.child("User").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    useremail = snapshot.child("email").getValue().toString();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        SharedPreferences sharedPref = getSharedPreferences("myapplication",Context.MODE_PRIVATE);
        String name = sharedPref.getString("Password", "");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gettxt = txt.getText().toString();
                if (gettxt.equals(name)){
                    startActivity(new Intent(LoginWithPasswordActivity.this, MainActivity.class));
                    finish();

                }else
                    Toast.makeText(LoginWithPasswordActivity.this, " password error", Toast.LENGTH_SHORT).show();
                    txt.setText("");
            }
        });
        forgetp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = Uttilties.createRandomCode(6);

                String res= CallApi.sendmail(code,LoginWithPasswordActivity.this, useremail);
                if(res.equals("sent")) {
                    Intent intent = new Intent(LoginWithPasswordActivity.this, Otp_loginActivity.class);
                    intent.putExtra("otp", code);
                    intent.putExtra("type", "1");
                    startActivity(intent);
                    finish();
                }else if(res.equals("filed")){
                    Toast.makeText(LoginWithPasswordActivity.this, "Something went wrong, You can try again ", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(LoginWithPasswordActivity.this, "network error, check your internet connection and try again ", Toast.LENGTH_LONG).show();
                }
            }



        });

    }
}