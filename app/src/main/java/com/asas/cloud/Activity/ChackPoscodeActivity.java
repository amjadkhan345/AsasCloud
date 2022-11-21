package com.asas.cloud.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.asas.cloud.Model.ApiControler;
import com.asas.cloud.Model.Response;
import com.asas.cloud.R;
import com.asas.cloud.classes.Uttilties;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hanks.passcodeview.PasscodeView;

import retrofit2.Call;
import retrofit2.Callback;

public class ChackPoscodeActivity extends AppCompatActivity {

    PasscodeView passcodeView;
    Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    String useremail;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chack_poscode);
        SharedPreferences sharedPref = this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
        String name = sharedPref.getString("Password", "");
        passcodeView=findViewById(R.id.passcodeView);
        toolbar = findViewById(R.id.toolbar_passcode);
        setSupportActionBar(toolbar);
        context=this;
        toolbar.inflateMenu(R.menu.mainmenu);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Asas Cloud");
            //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        passcodeView.setPasscodeLength(5)
                .setLocalPasscode(name)
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(String number) {
                        startActivity(new Intent(ChackPoscodeActivity.this, MainActivity.class));
                        finish();
                    }
                });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.forget_fassword, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.forget_password_login) {
            String code = Uttilties.createRandomCode(6);

            //String res = CallApi.sendmail(code,BiomatricActivity.this, useremail);
            String senderEmail = "asaskhan039@gmail.com";
            //String message =  code ;
            String subject = "Verification Code";
            //String ReseverEmail = "ak4806030@gmail.com";
            ProgressDialog progressBar = new ProgressDialog(context);
            progressBar.setTitle("Sending Email");

            progressBar.setCancelable(false);

            progressBar.show();


            Call<Response> call= ApiControler.getInstance()
                    .getapi()
                    .sendmail(useremail, subject, code, senderEmail);
            call.enqueue(new Callback<Response>() {
                @Override
                public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                    Response response1 = response.body();
                    if (response1.getMessage().equals("sent")){
                        Intent intent=new Intent(context, Otp_loginActivity.class);
                        intent.putExtra("otp", code);
                        intent.putExtra("type","1");
                        startActivity(intent);
                        finish();
                        //res= "sent";//response1.getMessage();
                        progressBar.dismiss();

                    }else {
                        progressBar.dismiss();
                        Toast.makeText(context, "Something went wrong, You can try again ", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    progressBar.dismiss();
                    Toast.makeText(context, "network error, check your internet connection and try again ", Toast.LENGTH_LONG).show();

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }
}