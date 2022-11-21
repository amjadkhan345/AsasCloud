package com.asas.cloud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.asas.cloud.R;
import com.asas.cloud.classes.ImageResize;
import com.asas.cloud.classes.References;
import com.asas.cloud.classes.Uttilties;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VideoDatiles extends AppCompatActivity {
    VideoView videoView;
    ImageView imageView;
    //MediaController mediaController;
    FloatingActionButton delete, download;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;
    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_datiles);
        videoView = findViewById(R.id.show_video);
        imageView = findViewById(R.id.show_image);
        delete=findViewById(R.id.btn_delete);
        download=findViewById(R.id.btn_downoald);
        database= FirebaseDatabase.getInstance();
        databaseReference= References.Video_Reference;//database.getReference();
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        user_id=user.getUid();
        String type = getIntent().getStringExtra("type");
        String file_id = getIntent().getStringExtra("id");
        databaseReference.child(user_id).child(file_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);


        //VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(file_id);
        videoView.setMediaController(mediaController);
        videoView.start();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uttilties.downloadManager(VideoDatiles.this, file_id);
            }
        });


    }
}