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

import com.asas.cloud.Model.UserModel;
import com.asas.cloud.R;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity {
    boolean checkBox11;


    TextView email,password, forget_password, reg_btn, pravicy_url;
    Button signInButton, login_btn, facebook11;

    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    FirebaseDatabase database;
    DatabaseReference reference;

    private GoogleSignInClient mGoogleSignInClient;
    CallbackManager callbackManager;
    LoginButton facebook_btn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        database = FirebaseDatabase.getInstance();
        facebook11 = findViewById(R.id.facebook11);
        progressDialog= new ProgressDialog(this);
        progressDialog.setCancelable(false);
        facebook11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }
        });
        reference = database.getReference().child("User");
        //checkBox.setChecked(false);
        //checkBox11 = false;
        callbackManager = CallbackManager.Factory.create();

        //onCheckboxClicked(checkBox);
       /* if(checkBox.isChecked()){
            login_btn.setEnabled(true);
            facebook_btn.setEnabled(true);
            signInButton.setEnabled(true);

        }else{*/
        //login_btn.setEnabled(false);
        //facebook_btn.setEnabled(false);
        // signInButton.setEnabled(false);


        init1();
        singinprocise();
        /*facebook_btn.setReadPermissions(Arrays.asList("email"));
        facebook_btn.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebook_btn.setVisibility(View.GONE);
                sinupwithfacebook(loginResult);


                /*AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "signInWithCredential:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);//loginResult.getAccessToken().getToken());
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithCredential:failure", task.getException());
                                    //Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
                                       //     Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });

                GraphRequest graphRequest   =   GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        Log.d("JSON", ""+response.getJSONObject().toString());

                        try
                        {
                            String userID = object.getString("id");
                            String email1       =   object.getString("email");
                            String name        =   object.getString("name");
                            String profile = "https://graph.facebook.com/" + userID+ "/picture?type=large";
                            FirebaseUser user = mAuth.getCurrentUser();

                            UserModel userModel = new UserModel();
                            userModel.setName(name);
                            userModel.setEmail(email1);
                            userModel.setProfileurl(profile);
                            reference.child(user.getUid()).setValue(userModel);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();

                            //first_name  =   object.optString("first_name");
                            //last_name   =   object.optString("last_name");

                            //tvEmail.setText(email);
                            //tvfirst_name.setText(first_name);
                            //tvlast_namee.setText(last_name);
                            //tvfull_name.setText(name);
                            //LoginManager.getInstance().logOut();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });//from here

            }

            @Override
            public void onError(@NotNull FacebookException e) {
                Toast.makeText(LoginActivity.this, "error in login" + e, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

                Toast.makeText(LoginActivity.this, " You cancel the prosise", Toast.LENGTH_SHORT).show();

            }
        });*/
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setTitle("loading....");
                progressDialog.show();

                emaillogin();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                //startActivityForResult(signInIntent, RC_SIGN_IN);
                startActivityForResult(signInIntent, 101);
            }
        });

    }



    private void sinupwithfacebook(LoginResult token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getAccessToken().getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            createuser(token);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void createuser(LoginResult token) {

        GraphRequest graphRequest   =   GraphRequest.newMeRequest(token.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
        {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response)
            {
                Log.d("JSON", ""+response.getJSONObject().toString());

                try
                {
                    String userID = object.getString("id");
                    String email1       =   object.getString("email");
                    String name        =   object.getString("name");
                    String profile = "https://graph.facebook.com/" + userID+ "/picture?type=large";
                    FirebaseUser user = mAuth.getCurrentUser();

                    UserModel userModel = new UserModel();
                    userModel.setName(name);
                    userModel.setEmail(email1);
                    userModel.setProfileurl(profile);
                    reference.child(user.getUid()).setValue(userModel);


                    //first_name  =   object.optString("first_name");
                    //last_name   =   object.optString("last_name");

                    //tvEmail.setText(email);
                    //tvfirst_name.setText(first_name);
                    //tvlast_namee.setText(last_name);
                    //tvfull_name.setText(name);
                    //LoginManager.getInstance().logOut();
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

            }

        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday,cover,picture.type(large)");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateUI(FirebaseUser user) {
    }


    private void singinprocise() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    ////public void onCheckboxClicked(View view) {
    // Is the view now checked?
    //boolean checked = ((CheckBox) view).isChecked();

    // Check which checkbox was clicked
    //if(checked){
    //  facebook_btn.setClickable(true);
    //signInButton.setClickable(true);
    //login_btn.setClickable(true);

    //}else{
    //    facebook_btn.setClickable(false);
    //  signInButton.setClickable(false);
    //login_btn.setClickable(false);

    //}



    // }

    private void init1() {
        email= findViewById(R.id.editTextTextEmailAddress);
        password=findViewById(R.id.editTextTextPassword);
        forget_password=findViewById(R.id.forget_password);
        reg_btn=findViewById(R.id.register_btn_login);
        //facebook_btn1 = findViewById(R.id.singin_with_facebook);
        //facebook_btn =(LoginButton) findViewById(R.id.face_login);
        login_btn=findViewById(R.id.email_login);
        //checkBox =(CheckBox) findViewById(R.id.checkBox);
        signInButton= findViewById(R.id.singin_with_google);
        mAuth = FirebaseAuth.getInstance();
        //checkBox.isChecked();
        pravicy_url = findViewById(R.id.pravcy_url);
        pravicy_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PravicyActivity.class);
                startActivity(intent);

            }
        });


        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PravicyActivity.class);
                startActivity(intent);
            }
        });
        signInButton.setEnabled(false);
        facebook11.setEnabled(false);

        reg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

    }

    public void itemClicked(View v) {
        //code to check if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            //login_btn.setVisibility(View.GONE);
            facebook11.setEnabled(true);
            signInButton.setEnabled(true);
            login_btn.setEnabled(true);

        }else {
            facebook11.setEnabled(false);
            signInButton.setEnabled(false);
            login_btn.setEnabled(false);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 101) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(LoginActivity.this, "Error in login "+ task, Toast.LENGTH_LONG).show();
            }
        }else{
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            UserModel userModel = new UserModel();
                            GoogleSignInAccount account= GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
                            userModel.setName(account.getDisplayName());
                            userModel.setEmail(account.getEmail());
                            userModel.setPhoneNumber("Your phone number");
                            userModel.setCountry("Your Country");
                            userModel.setAge("Your age");
                            userModel.setProfileurl(account.getPhotoUrl().toString());
                            reference.child(user.getUid()).setValue(userModel);
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Error in login 123 ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private void emaillogin(){
        if (email != null && password != null) {
            String email1 = email.getText().toString();
            String password1 = password.getText().toString();
            mAuth.signInWithEmailAndPassword(email1, password1)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                //Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);

                            } else {
                                progressDialog.dismiss();
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                //updateUI(null);
                            }
                        }

                        private void updateUI(FirebaseUser user) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);

                        }
                    });
        }else{
            progressDialog.dismiss();
            Toast.makeText(this, "Your credential is wrong invalid email and password ", Toast.LENGTH_SHORT).show();
        }


    }
}