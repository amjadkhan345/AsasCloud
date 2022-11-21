package com.asas.cloud.Activity;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.asas.cloud.R;

import java.lang.reflect.Array;
import java.util.Arrays;

public class PinCodeActivity extends AppCompatActivity implements View.OnClickListener{


    ImageView imageview_circle1, imageview_circle2, imageview_circle3, imageview_circle4, imageview_circle5;

    ImageButton backbtn, done;
    EditText enter_mpin;
    String pass ;

    TextView _1, _2, _3,_4,_5, _6, _7, _8,_9,_0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_code);



        imageview_circle1= findViewById(R.id.imageview_circle1);
        imageview_circle2= findViewById(R.id.imageview_circle2);
        imageview_circle3= findViewById(R.id.imageview_circle3);
        imageview_circle4= findViewById(R.id.imageview_circle4);
        imageview_circle5= findViewById(R.id.imageview_circle5);


        enter_mpin = (EditText) findViewById(R.id.editText_enter_mpin);
        enter_mpin.requestFocus();
        enter_mpin.setInputType(InputType.TYPE_CLASS_NUMBER);
        enter_mpin.setFocusableInTouchMode(true);

        enter_mpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "onKey: screen key pressed");
                imageview_circle1.setImageResource(R.drawable.circle2);
            }
        });

        backbtn=findViewById(R.id.bickbtn);
        done=findViewById(R.id.ok);

        _1=findViewById(R.id._1);
        _2=findViewById(R.id._2);
        _3=findViewById(R.id._3);
        _4=findViewById(R.id._4);
        _5=findViewById(R.id._5);
        _6=findViewById(R.id._6);
        _7=findViewById(R.id._7);
        _8=findViewById(R.id._8);
        _9=findViewById(R.id._9);
        _0=findViewById(R.id._0);

        _1.setOnClickListener((View.OnClickListener) this);
        _2.setOnClickListener((View.OnClickListener) this);
        _3.setOnClickListener((View.OnClickListener) this);
        _4.setOnClickListener((View.OnClickListener) this);
        _5.setOnClickListener((View.OnClickListener) this);
        _6.setOnClickListener((View.OnClickListener) this);
        _7.setOnClickListener((View.OnClickListener) this);
        _8.setOnClickListener((View.OnClickListener) this);
        _9.setOnClickListener((View.OnClickListener) this);
        _0.setOnClickListener((View.OnClickListener) this);
        backbtn.setOnClickListener((View.OnClickListener) this);
        done.setOnClickListener((View.OnClickListener) this);



    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id._0:
                // do your code
                pass = enter_mpin.getText().toString();
                if (pass.length() == 5) {
                    Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.length() == 0) {
                        imageview_circle1.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass+"0");
                        pass= pass+"0";

                    } else if (pass.length() == 1) {
                        imageview_circle2.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass+"0");
                        pass= pass+"0";
                    } else if (pass.length() == 2) {
                        imageview_circle3.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass+"0");
                        pass= pass+"0";
                    } else if (pass.length() == 3) {
                        imageview_circle4.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass+"0");
                        pass= pass+"0";
                    } else if (pass.length() == 4) {
                        imageview_circle5.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass+"0");
                        pass= pass+"0";
                    } else {
                        Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();

                    }
                }

                break;
            case R.id._1:
                // do your code
                pass = enter_mpin.getText().toString();
                if (pass.length() == 5) {
                    Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.length() == 0) {
                        imageview_circle1.setImageResource(R.drawable.circle2);
                        enter_mpin.setText("1");
                        pass= pass+"1";

                    } else if (pass.length() == 1) {
                        imageview_circle2.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass+"1");
                        pass= pass+"1";
                    } else if (pass.length() == 2) {
                        imageview_circle3.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass+"1");
                        pass= pass+"1";
                    } else if (pass.length() == 3) {
                        imageview_circle4.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass+"1");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 4) {
                        imageview_circle5.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass+"1");
                        pass= enter_mpin.getText().toString();
                    } else {
                        enter_mpin.setText(pass+"1");
                        Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case R.id._2:
                // do your code
                pass = enter_mpin.getText().toString();
                if (pass.length() == 5) {
                    Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.length() == 0) {
                        imageview_circle1.setImageResource(R.drawable.circle2);
                        enter_mpin.setText("2");
                        pass= enter_mpin.getText().toString();

                    } else if (pass.length() == 1) {
                        imageview_circle2.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "2");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 2) {
                        imageview_circle3.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "2");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 3) {
                        imageview_circle4.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "2");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 4) {
                        imageview_circle5.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "2");
                        pass= enter_mpin.getText().toString();
                    } else {
                        Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case R.id._3:
                // do your code
                pass = enter_mpin.getText().toString();
                if (pass.length() == 5) {
                    Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.length() == 0) {
                        imageview_circle1.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "3");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 1) {
                        imageview_circle2.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "3");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 2) {
                        imageview_circle3.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "3");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 3) {
                        imageview_circle4.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "3");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 4) {
                        imageview_circle5.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "3");
                        pass= enter_mpin.getText().toString();
                    } else {
                        Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case R.id._4:
                // do your code
                pass = enter_mpin.getText().toString();
                if (pass.length() == 5) {
                    Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.length() == 0) {
                        imageview_circle1.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "4");
                        pass= enter_mpin.getText().toString();

                    } else if (pass.length() == 1) {
                        imageview_circle2.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "4");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 2) {
                        imageview_circle3.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "4");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 3) {
                        imageview_circle4.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "4");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 4) {
                        imageview_circle5.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "4");
                        pass= enter_mpin.getText().toString();
                    } else {
                        Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case R.id._5:
                // do your code
                pass = enter_mpin.getText().toString();
                if (pass.length() == 5) {
                    Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.length() == 0) {
                        imageview_circle1.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "5");
                        pass= enter_mpin.getText().toString();

                    } else if (pass.length() == 1) {
                        imageview_circle2.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "5");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 2) {
                        imageview_circle3.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "5");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 3) {
                        imageview_circle4.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "5");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 4) {
                        //pass= enter_mpin.getText().toString();
                        imageview_circle5.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "5");
                        pass= enter_mpin.getText().toString();
                    } else {
                        Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case R.id._6:
                // do your code
                pass = enter_mpin.getText().toString();
                if (pass.length() == 5) {
                    Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.length() == 0) {
                        imageview_circle1.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "6");
                        pass= enter_mpin.getText().toString();

                    } else if (pass.length() == 1) {
                        imageview_circle2.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "6");
                    } else if (pass.length() == 2) {
                        imageview_circle3.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "6");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 3) {
                        imageview_circle4.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "6");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 4) {
                        imageview_circle5.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "6");
                        pass= enter_mpin.getText().toString();
                    } else {
                        Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case R.id._7:
                // do your code
                pass = enter_mpin.getText().toString();
                if (pass.length() == 5) {
                    Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.length() == 0) {
                        imageview_circle1.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "7");
                        pass= enter_mpin.getText().toString();

                    } else if (pass.length() == 1) {
                        imageview_circle2.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "7");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 2) {
                        imageview_circle3.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "7");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 3) {
                        imageview_circle4.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "7");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 4) {
                        imageview_circle5.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "7");
                        pass= enter_mpin.getText().toString();
                    } else {
                        Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case R.id._8:
                // do your code
                pass = enter_mpin.getText().toString();
                if (pass.length() == 5) {
                    Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.length() == 0) {
                        imageview_circle1.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "8");
                        pass= enter_mpin.getText().toString();

                    } else if (pass.length() == 1) {
                        imageview_circle2.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "8");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 2) {
                        imageview_circle3.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "8");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 3) {
                        imageview_circle4.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "8");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 4) {
                        imageview_circle5.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "8");
                        pass= enter_mpin.getText().toString();
                    } else {
                        Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case R.id._9:
                // do your code
                pass = enter_mpin.getText().toString();
                if (pass.length() == 5) {
                    Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();
                } else {
                    if (pass.length() == 0) {
                        imageview_circle1.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "9");
                        pass= enter_mpin.getText().toString();

                    } else if (pass.length() == 1) {
                        imageview_circle2.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "9");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 2) {
                        imageview_circle3.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "9");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 3) {
                        imageview_circle4.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "9");
                        pass= enter_mpin.getText().toString();
                    } else if (pass.length() == 4) {
                        imageview_circle5.setImageResource(R.drawable.circle2);
                        enter_mpin.setText(pass + "9");
                        pass= enter_mpin.getText().toString();
                    } else {
                        Toast.makeText(this, "your cherkter is full", Toast.LENGTH_SHORT).show();

                    }
                }
                break;
            case R.id.ok:
                // do your code
                if (pass.length()==5) {

                    //Toast.makeText(this, "your textis"+ pass, Toast.LENGTH_LONG).show();
                    SharedPreferences sharedPref = PinCodeActivity.this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("lockd_no", "2");
                    editor.putBoolean("lockd", true);
                    editor.apply();
                    //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor password = sharedPref.edit();
                    password.putString("Password", pass);
                    password.apply();
                    // Toast.makeText(PassordProtectActivity.this, " Your Password save completely", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PinCodeActivity.this, MainActivity.class));
                    finish();
                }else
                    Toast.makeText(this, "5 digats is require", Toast.LENGTH_LONG).show();


                break;
            case R.id.bickbtn:
                // do your code
                String main;

                if(pass.length()==5){
                    char[] ch = pass.toCharArray();
                    String pass1=String.valueOf(ch[0]);
                    String pass2=String.valueOf(ch[1]);
                    String pass3=String.valueOf(ch[2]);
                    String pass4=String.valueOf(ch[3]);

                    pass=pass1+pass2+pass3+pass4;
                    enter_mpin.setText(pass);
                    imageview_circle5.setImageResource(R.drawable.circle);
                }else if (pass.length()==4){
                    char[] ch = pass.toCharArray();
                    String pass1=String.valueOf(ch[0]);
                    String pass2=String.valueOf(ch[1]);
                    String pass3=String.valueOf(ch[2]);
                    //String pass4=String.valueOf(ch[3]);

                    pass=pass1+pass2+pass3;//+pass4;
                    enter_mpin.setText(pass);
                    imageview_circle4.setImageResource(R.drawable.circle);

                }else if(pass.length()==3){
                    char[] ch = pass.toCharArray();
                    String pass1=String.valueOf(ch[0]);
                    String pass2=String.valueOf(ch[1]);
                    //String pass3=String.valueOf(ch[2]);
                    //String pass4=String.valueOf(ch[3]);

                    pass=pass1+pass2;//+pass3+pass4;
                    enter_mpin.setText(pass);
                    imageview_circle3.setImageResource(R.drawable.circle);

                }else if(pass.length()==2){
                    char[] ch = pass.toCharArray();
                    String pass1=String.valueOf(ch[0]);
                    //String pass2=String.valueOf(ch[1]);
                    //String pass3=String.valueOf(ch[2]);
                    //String pass4=String.valueOf(ch[3]);

                    pass=pass1;//+pass2+pass3+pass4;
                    enter_mpin.setText(pass);
                    imageview_circle2.setImageResource(R.drawable.circle);



                }else{
                    enter_mpin.setText("");
                    imageview_circle1.setImageResource(R.drawable.circle);

                }

                break;
            default:
                break;
        }
    }
}