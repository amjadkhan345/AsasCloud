package com.asas.cloud.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

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

import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;

//@RequiresApi(api = Build.VERSION_CODES.P)
public class BiomatricActivity extends AppCompatActivity {

    boolean isavalebal1;

    private static final String KEY_NAME = "KeyName";
    private static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    private static final String FORWARD_SLASH = "/";
    private Button touchButton;
    BiometricPrompt.PromptInfo promptInfo;
    Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseUser user;
    FirebaseAuth auth;
    String useremail;
    Context context;

    @RequiresApi(api = Build.VERSION_CODES.P)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biomatric);
        toolbar = findViewById(R.id.toolbar_biom);
        setSupportActionBar(toolbar);
        context= this;
        toolbar.inflateMenu(R.menu.mainmenu);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Asas Cloud");
            //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
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

        // Initialising msgtext and loginbutton
        TextView msgtex = findViewById(R.id.msgtext);
        final Button loginbutton = findViewById(R.id.login);
        //biometricPrompt.authenticate(promptInfo);
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Asas cloud")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtex.setText("You can use the fingerprint sensor to login");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtex.setText("This device doesnot have a fingerprint sensor");
                loginbutton.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtex.setText("The biometric sensor is currently unavailable");
                loginbutton.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtex.setText("Your device doesn't have fingerprint saved,please check your security settings");
                loginbutton.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(BiomatricActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                loginbutton.setText("Login Successful");

                startActivity(new Intent(BiomatricActivity.this, LoginActivity.class));
                finish();
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        biometricPrompt.authenticate(promptInfo);
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        //promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Asas cloud")
              //  .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

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
                        Toast.makeText(BiomatricActivity.this, "Something went wrong, You can try again ", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<Response> call, Throwable t) {
                    progressBar.dismiss();
                    Toast.makeText(BiomatricActivity.this, "network error, check your internet connection and try again ", Toast.LENGTH_LONG).show();

                }
            });

        }
        return super.onOptionsItemSelected(item);
    }


}