package com.asas.cloud.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.asas.cloud.R;

public class PassordProtectActivity extends AppCompatActivity {
    //ConstraintLayout crate_p, delate_p;

    Toolbar toolbar;
    TextView password, conf_password, ShowTxt;
    Button btn;
    String pass_txt, conf_txt;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passord_protect);
        //crate_p=findViewById(R.id.create_p);
        //delate_p=findViewById(R.id.delate_p);

        toolbar = findViewById(R.id.password_toolbar);
        password= findViewById(R.id.set_password_text);
        conf_password = findViewById(R.id.conform_password_text);
        btn = findViewById(R.id.set_password_btn);
        ShowTxt=findViewById(R.id.show_text);
        //cleare_password=findViewById(R.id.cleare_password);
        setSupportActionBar(toolbar);
        SharedPreferences sharedPref = this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
        boolean name = sharedPref.getBoolean("laked", true);

        String lokdtype= sharedPref.getString("lockd_no", "");

        //if (lokdtype.equals("1")){




        //toolbar.inflateMenu(R.menu.mainmenu);
        //MenuItem menuItem = toolbar.getMenu().findItem(R.id.search_bar);
        //menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_ALWAYS);
        //SearchView searchView = (SearchView) menuItem.getActionView();
        //searchView.setQueryHint("Search Book");
        //setHasOptionsMenu(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Asas Cloud");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass_txt = password.getText().toString();
                conf_txt = conf_password.getText().toString();
                if (pass_txt.length() <= 4){
                    ShowTxt.setText(R.string.reqird_crekter);
                }else if (pass_txt.isEmpty()){
                    ShowTxt.setText(R.string.feild_reqird);

                }else if (conf_txt.isEmpty()){
                    ShowTxt.setText(R.string.feild_reqird);


                }else if (!pass_txt.equals(conf_txt)){
                    ShowTxt.setText("Your password is not equil ");
                    conf_password.setText("");

                }else {
                    //SharedPreferences sharedPref = getPreferences( Context.MODE_PRIVATE);
                    SharedPreferences sharedPref = PassordProtectActivity.this.getSharedPreferences("myapplication", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("lockd_no", "1");
                    editor.putBoolean("lockd", true);
                    editor.apply();
                    //SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                    SharedPreferences.Editor password = sharedPref.edit();
                    password.putString("Password", pass_txt);
                    password.apply();
                   // Toast.makeText(PassordProtectActivity.this, " Your Password save completely", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PassordProtectActivity.this, MainActivity.class));
                    finish();
                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
        //finish();
    }
}