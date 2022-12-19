package com.asas.cloud.Activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.asas.cloud.Model.VideoModel;
import com.asas.cloud.R;
import com.asas.cloud.adapter.PlayAsapter;
import com.asas.cloud.classes.References;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VideoDatiles extends AppCompatActivity {
    VideoView videoView;
    ImageView imageView;
    ViewPager2 viewPager2;
    //MediaController mediaController;
    FloatingActionButton delete, download;

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    String user_id;
    //PlayAsapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_datiles);
        viewPager2=findViewById(R.id.viewpager2);
        user=auth.getCurrentUser();
        user_id=user.getUid();

        FirebaseRecyclerOptions<VideoModel> options
                = new FirebaseRecyclerOptions.Builder<VideoModel>()
                .setQuery(References.Video_Reference.child(user_id), VideoModel.class)
                .build();


    }

}