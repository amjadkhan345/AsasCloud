package com.asas.cloud.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.asas.cloud.R;
import com.asas.cloud.classes.References;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class ProfileActivity extends AppCompatActivity {

    ImageView profile_image;
    TextView name, email,contry, number;

    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseDatabase database;
    DatabaseReference reference;
    String userId;
    int  FILE_SELECT_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        database=FirebaseDatabase.getInstance();
        userId=References.UserId;
        reference= References.User_Reference;//database.getReference().child(U)
        profile_image=findViewById(R.id.profile_image1);
        name=findViewById(R.id.user_name);
        email=findViewById(R.id.user_email);
        contry=findViewById(R.id.contry_name);
        number=findViewById(R.id.mobil_no);
        ProgressDialog progressDialog= new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Load Data");
        progressDialog.show();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        reference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    //UserModel model = ds.child(userId).getValue(UserModel.class);
                    //Boolean isLike = ds.child("isLike").getValue(picUrl.class);
                    //Log.d("TAG", picUrl + " / " + isLike);
                    //String profileamage=snapshot.child("profileurl").getValue().toString();
                    if(snapshot.exists()) {
                        progressDialog.dismiss();
                        name.setText(snapshot.child("name").getValue().toString());
                        email.setText(snapshot.child("email").getValue().toString());
                        contry.setText(snapshot.child("country").getValue().toString());
                        number.setText(snapshot.child("phoneNumber").getValue().toString());
                        Glide.with(ProfileActivity.this).load(snapshot.child("profileurl").getValue().toString()).into(profile_image);
                        //Glide.with(ProfileActivity.this).load(profileamage).into(profile_image);
                        /*if(profileamage.equals("abc")){
                            profile_image.setImageDrawable(getDrawable(R.drawable.ic_user_circle_24));
                            profile_image.setClickable(true);

                        }else {
                            profile_image.setClickable(false);
                            Glide.with(ProfileActivity.this).load(snapshot.child("profileurl").getValue().toString()).into(profile_image);
                        }

                        }*/
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                //intent.setType("image/* , video/*");
                String[] mimetypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                //startActivityForResult(intent, FILE_SELECT_CODE);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), FILE_SELECT_CODE);

            }
        });*/




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode== FILE_SELECT_CODE){
            Uri uri= data.getData();
            StorageReference reference1=References.PROFILE_REFERENCE;
            //reference.putFile(uri).
            ProgressDialog pd ;
            pd = new ProgressDialog(ProfileActivity.this);
            pd.setCancelable(false);
            pd.show();
            UploadTask uploadTask = reference1.putFile(uri);
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
                            String myuri= uri.toString();

                            reference.child(userId).child("profileurl").setValue(uri);
                            pd.dismiss();


                        }
                    });


                }
            });
        }
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        onBackPressed();
        finish();
        return true;
        //return super.onOptionsItemSelected(item);
    }

}