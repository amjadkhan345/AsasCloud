package com.asas.cloud.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import com.asas.cloud.R;

import java.util.concurrent.Executor;

public class SeetingActivity extends AppCompatActivity {



    LinearLayout layout1, layout2, layout3, layout_none;
    TextView msgtex1;
    Switch aSwitch;
    BiometricPrompt.PromptInfo promptInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seeting);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        layout1=findViewById(R.id.linearLayout1);
        layout2=findViewById(R.id.linearLayout2);
        layout3=findViewById(R.id.linearLayout3);
        aSwitch =findViewById(R.id.switch1);
        SharedPreferences sharedPref = this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
        String name = sharedPref.getString("lockd_no", "");

        boolean is=sharedPref.getBoolean("lockd", false);
        if(is){
            if(name.equals("3")){
                aSwitch.setChecked(true);
            }else{
                aSwitch.setChecked(false);
            }

        }

        //msgtex1=findViewById(R.id.msgtex1);

        /*aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (aSwitch.isChecked())
                    {

                        onFinger();

                    } else {
                        SharedPreferences sharedPref = SeetingActivity.this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("lockd_no", "10");
                        editor.putBoolean("lockd", false);
                        editor.apply();
                        startActivity(new Intent(SeetingActivity.this, MainActivity.class));
                        finish();


                        // do something else

                }
            }
        });*/
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    onFinger();
                } else {
                    SharedPreferences sharedPref = SeetingActivity.this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("lockd_no", "10");
                    editor.putBoolean("lockd", false);
                    editor.apply();
                    //startActivity(new Intent(SeetingActivity.this, MainActivity.class));
                    //finish();


                    // do something else

                }

            }
        });

        layout_none=findViewById(R.id.none);

        layout_none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sharedPref = SeetingActivity.this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("lockd_no", "10");
                editor.putBoolean("lockd", false);
                editor.apply();
                startActivity(new Intent(SeetingActivity.this, MainActivity.class));
                finish();

            }
        });

        /*layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });*/

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SeetingActivity.this, PinCodeActivity.class));
                finish();

            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SeetingActivity.this, PassordProtectActivity.class));
                finish();

            }
        });






    }

    private void onFinger() {
        //biometricPrompt.authenticate(promptInfo);
        promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("Asas cloud")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();


        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                //Toast.makeText(this, "You can use the fingerprint sensor to login", Toast.LENGTH_SHORT).show();
                //msgtex.setText("You can use the fingerprint sensor to login");
                //msgtex.setTextColor(Color.parseColor("#fafafa"));
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Toast.makeText(this, "This device doesnot have a fingerprint sensor", Toast.LENGTH_SHORT).show();
                //msgtex.setText("This device doesnot have a fingerprint sensor");
                //loginbutton.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Toast.makeText(this, "The biometric sensor is currently unavailable", Toast.LENGTH_SHORT).show();
                //msgtex.setText("The biometric sensor is currently unavailable");
                //loginbutton.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(this, "Your device doesn't have fingerprint saved,please check your security settings", Toast.LENGTH_SHORT).show();
                //msgtex.setText("Your device doesn't have fingerprint saved,please check your security settings");
                //loginbutton.setVisibility(View.GONE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(this);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt(SeetingActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                aSwitch.setChecked(false);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                SharedPreferences sharedPref = SeetingActivity.this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("lockd_no", "3");
                editor.putBoolean("lockd", true);
                editor.apply();
                aSwitch.setChecked(true);
                //loginbutton.setText("Login Successful");

                //startActivity(new Intent(SeetingActivity.this, LoginActivity.class));
                //finish();
            }
            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
            }
        });
        biometricPrompt.authenticate(promptInfo);




    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        finish();
        return true;
        //return super.onOptionsItemSelected(item);
    }
}