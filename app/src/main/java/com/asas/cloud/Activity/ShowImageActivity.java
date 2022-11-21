package com.asas.cloud.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.asas.cloud.R;
import com.asas.cloud.classes.ImageResize;
import com.asas.cloud.classes.References;
import com.asas.cloud.classes.Uttilties;
import com.bumptech.glide.Glide;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowImageActivity extends AppCompatActivity {
    ImageView imageView;
    Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;
    String user_id, id, url1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        imageView = findViewById(R.id.show_image);
        id = getIntent().getStringExtra("id");
        url1 = getIntent().getStringExtra("url");


        toolbar = findViewById(R.id.toolbarimage);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.pleymain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Asas Book");
            //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance();
        databaseReference = References.Image_Reference;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_id = user.getUid();
        //String file_id = getIntent().getStringExtra("id");
        Glide.with(this).load(url1).into(imageView);




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.pleymain, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.download) {
            Uttilties.downloadImageManager(this, url1);
            Toast.makeText(this, "Download start....", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ShowImageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();


        } else if (item_id == R.id.delete) {
            databaseReference.child(user_id).child(id).setValue(null);
            Intent intent = new Intent(ShowImageActivity.this, MainActivity.class);
            startActivity(intent);
            finish();

        } else {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }
}