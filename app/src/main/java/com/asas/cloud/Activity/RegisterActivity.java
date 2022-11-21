package com.asas.cloud.Activity;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.asas.cloud.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference();

    TextView Name, Email, Password;
    Button Register;
    private FirebaseAuth mAuth;
    ProgressDialog pd;
    //UserModel userModel; // new UserModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Name = findViewById(R.id.name_reg);
        Email = findViewById(R.id.email_reg);
        Password = findViewById(R.id.password_reg);
        Register = findViewById(R.id.register_btn);
        mAuth = FirebaseAuth.getInstance();
        Register.setEnabled(false);
        pd=new ProgressDialog(this);
        pd.setCancelable(false);
        //userModel = new UserModel();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.setTitle("loading....");
                pd.show();
                String name1 = Name.getText().toString();
                String email1 = Email.getText().toString();
                String password1 = Password.getText().toString();
                //sunip(email1, password1);
                if (name1.equals("") & email1.equals("") & password1.equals("")) {
                    pd.dismiss();
                    Toast.makeText(RegisterActivity.this, "please full field", Toast.LENGTH_LONG).show();
                } else {
                    sunip(email1, password1, name1);



                }
            }
        });
    }
    public void itemClicked1(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            //login_btn.setVisibility(View.GONE);

            Register.setEnabled(true);

        }else {
            Register.setEnabled(false);

        }


    }


    private void sunip(String email1, String password1, String name) {
        mAuth.createUserWithEmailAndPassword(email1, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }

                    private void updateUI(FirebaseUser user) {
                        if (user != null) {
                            pd.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, CreateUserActivity.class);
                            intent.putExtra("name", name);
                            intent.putExtra("email", email1);
                            intent.putExtra("user_id", user.getUid());
                            startActivity(intent);
                        }else {
                            pd.dismiss();
                            Toast.makeText(RegisterActivity.this, "error", Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
}