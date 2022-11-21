package com.asas.cloud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.asas.cloud.Model.UserModel;
import com.asas.cloud.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static android.content.ContentValues.TAG;

public class FacebookLoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("User");
        //registerCallback
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        createUser(loginResult);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Toast.makeText(FacebookLoginActivity.this, " Cancel ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Toast.makeText(FacebookLoginActivity.this, " Error", Toast.LENGTH_SHORT).show();
                        // App code
                    }
                });
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"));






    }

    private void createUser(LoginResult accessToken) {
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getAccessToken().getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user1 = mAuth.getCurrentUser();
                            //updateUI(user);
                            createuser(accessToken, user1);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(FacebookLoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void createuser(LoginResult token, FirebaseUser user1) {
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
                    //String age        =   object.getString("name");
                    String profile =object.getJSONObject("picture").getJSONObject("data").getString("url"); //https://graph.facebook.com/" + userID+ "/picture?type=large";
                    //FirebaseUser user = mAuth.getCurrentUser();

                    UserModel userModel = new UserModel();
                    userModel.setName(name);
                    userModel.setEmail(email1);
                    userModel.setAge("Your Age");
                    userModel.setPhoneNumber("Your Phone Number");
                    userModel.setCountry("Country");
                    userModel.setProfileurl(profile);
                    reference.child(user1.getUid()).setValue(userModel);


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
        Intent intent = new Intent(FacebookLoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}