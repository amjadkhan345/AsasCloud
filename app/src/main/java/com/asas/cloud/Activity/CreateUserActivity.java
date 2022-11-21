package com.asas.cloud.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.asas.cloud.Model.UserModel;
import com.asas.cloud.R;
import com.asas.cloud.classes.References;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class CreateUserActivity extends AppCompatActivity {
    TextView age, contry, phone;
    Button btn_reg;
    FirebaseDatabase database;
    DatabaseReference reference;
    ImageView profile;
    int  FILE_SELECT_CODE = 101;
    Bundle bundle;
    Uri uri;
    String myuri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        age = findViewById(R.id.age_reg);
        contry = findViewById(R.id.contry_reg);
        phone = findViewById(R.id.phone_reg);
        btn_reg = findViewById(R.id.reg_btn);
        profile= findViewById(R.id.profile_image1);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("User");
        bundle = getIntent().getExtras();
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                //intent.setType("image/* , video/*");
                String[] mimetypes = {"image/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                //startActivityForResult(intent, FILE_SELECT_CODE);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), FILE_SELECT_CODE);

            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                if (age.equals("")) {
                    age.setText("Put age");
                    age.setTextColor(R.color.reed);
                } else if (contry.equals("")) {
                    age.setText("Put Country Name");
                    age.setTextColor(R.color.reed);
                } else if (phone.equals("")) {
                    age.setText("Put Phone Number");
                    age.setTextColor(R.color.reed);
                } else {
                    StorageReference reference1= References.PROFILE_REFERENCE;
                    //reference.putFile(uri).
                    ProgressDialog pd ;
                    pd = new ProgressDialog(CreateUserActivity.this);
                    pd.setCancelable(false);
                    pd.show();
                    UploadTask uploadTask;
                    if(uri==null){
                        pd.dismiss();
                        Toast.makeText(CreateUserActivity.this, "upload image", Toast.LENGTH_SHORT).show();
                    }else {
                        uploadTask = reference1.putFile(uri);
                        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                System.out.println("done");

                                pd.setMessage(((int) progress) + "% uploaded");
                                //pd.show();
                            }
                        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                                System.out.println("Upload is paused");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                pd.dismiss();
                                // Handle unsuccessful uploads
                                //Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                reference1.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        myuri = uri.toString();
                                        //reference.child(bundle.getString("user_id")).child("profileurl").setValue(myuri);
                                        String age1 = age.getText().toString();
                                        String contry1 = contry.getText().toString();
                                        String phone1 = phone.getText().toString();
                                        UserModel model = new UserModel();

                                        //UserModel userModel = new UserModel(bundle.getString("name"),
                                        //       bundle.getString("email"), age1, contry1, phone1);
                                        model.setAge(age1);
                                        model.setProfileurl(myuri);
                                        //model.setProfileurl("abc");
                                        model.setCountry(contry1);
                                        model.setEmail(bundle.getString("email"));
                                        model.setName(bundle.getString("name"));
                                        model.setPhoneNumber(phone1);
                                        reference.child(bundle.getString("user_id")).setValue(model);
                                        Intent intent = new Intent(CreateUserActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                        pd.dismiss();


                                    }
                                });


                            }
                        });

                    }
                }

            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode== FILE_SELECT_CODE){
            uri = data.getData();
            //Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                profile.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}