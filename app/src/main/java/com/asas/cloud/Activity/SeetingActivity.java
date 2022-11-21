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

import com.asas.cloud.R;

public class SeetingActivity extends AppCompatActivity {



    LinearLayout layout1, layout2, layout3, layout_none;
    TextView msgtex1;
    Switch aSwitch;

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
                    startActivity(new Intent(SeetingActivity.this, MainActivity.class));
                    finish();


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
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(SeetingActivity.this);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                //msgtex1.setText("You can use the fingerprint sensor to login");
                //msgtex1.setTextColor(Color.parseColor("#fafafa"));
                SharedPreferences sharedPref = SeetingActivity.this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("lockd_no", "3");
                editor.putBoolean("lockd", true);
                editor.apply();
                Toast.makeText(SeetingActivity.this, "Success", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SeetingActivity.this, MainActivity.class));
                finish();
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtex1.setText("This device doesnot have a fingerprint sensor");
                //loginbutton.setVisibility(View.GONE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtex1.setText("The biometric sensor is currently unavailable");
                //loginbutton.setVisibility(View.GONE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtex1.setText("Your device doesn't have fingerprint saved,please check your security settings");
                //loginbutton.setVisibility(View.GONE);
                break;

    }


    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        finish();
        return true;
        //return super.onOptionsItemSelected(item);
    }
}