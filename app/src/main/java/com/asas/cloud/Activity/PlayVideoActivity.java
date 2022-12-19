package com.asas.cloud.Activity;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.asas.cloud.Model.AudioModel;
import com.asas.cloud.Model.VideoModel;
import com.asas.cloud.R;
import com.asas.cloud.classes.References;
import com.asas.cloud.classes.TrackSelectionDialog;
import com.asas.cloud.classes.Uttilties;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
//import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
//import com.google.android.exoplayer2.ui.PlayerView;


public class PlayVideoActivity extends AppCompatActivity {
    ImageView speedBtn, farwordBtn, rewBtn, setting;
    ArrayList<VideoModel> list;//= new ArrayList<>();

    private boolean isShowingTrackSelectionDialog;
    private DefaultTrackSelector trackSelector;
    String[] speed = {"0.25x", "0.5x", "Normal", "1.5x", "2x"};
    //demo url
    String url1; //= "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    //208A8A8A
    PlayerView playerView;
    SimpleExoPlayer simpleExoPlayer;
    Toolbar toolbar;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    FirebaseUser user;
    String user_id, id, type, videoname, audioname;
    int position;
    VideoModel model;
    AudioModel audioModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        list=new ArrayList<>();
        type=getIntent().getStringExtra("type");
        id = getIntent().getStringExtra("id");
        url1 = getIntent().getStringExtra("url");
        //position= getIntent().getIntExtra("position", 1);


        toolbar = findViewById(R.id.toolbar55);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.pleymain);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Asas Book");
            //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        database = FirebaseDatabase.getInstance();
        databaseReference = References.Video_Reference;
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_id = user.getUid();
        databaseReference.child(user_id).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    videoname= snapshot.child("video_Name").getValue().toString();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        References.AUDIO_Reference.child(user_id).child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    audioname = snapshot.child("video_Name").getValue().toString();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Log.d("user", user_id);
        //R85ykKRgWoU69dBXAsYXm1N2rfU2
        //int postion = list.size();
        list.add(model);
        //String path = list.get(position).getVideo();
        //Uri uri=Uri.parse(path);


        trackSelector = new DefaultTrackSelector(PlayVideoActivity.this);
        simpleExoPlayer = new SimpleExoPlayer.Builder(PlayVideoActivity.this).setTrackSelector(trackSelector).build();
        playerView = findViewById(R.id.exoPlayerView);
        playerView.setPlayer(simpleExoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(url1);
        simpleExoPlayer.addMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();
        farwordBtn = playerView.findViewById(R.id.fwd);
        rewBtn = playerView.findViewById(R.id.rew);
        setting = playerView.findViewById(R.id.exo_track_selection_view);
        speedBtn = playerView.findViewById(R.id.exo_playback_speed);
        TextView speedTxt = playerView.findViewById(R.id.speed);


        speedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder builder = new AlertDialog.Builder(PlayVideoActivity.this);
                builder.setTitle("Set Speed");
                builder.setItems(speed, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // the user clicked on colors[which]

                        if (which == 0) {
                            //  speedTxt.setVisibility(View.VISIBLE);
                            //   speedTxt.setText("0.25X");
                            PlaybackParameters param = new PlaybackParameters(0.5f);
                            simpleExoPlayer.setPlaybackParameters(param);
                        }
                        if (which == 1) {
                            // speedTxt.setVisibility(View.VISIBLE);
                            // speedTxt.setText("0.5X");
                            PlaybackParameters param = new PlaybackParameters(0.5f);
                            simpleExoPlayer.setPlaybackParameters(param);
                        }
                        if (which == 2) {
                            // speedTxt.setVisibility(View.GONE);
                            PlaybackParameters param = new PlaybackParameters(1f);
                            simpleExoPlayer.setPlaybackParameters(param);
                        }
                        if (which == 3) {
                            //  speedTxt.setVisibility(View.VISIBLE);
                            //  speedTxt.setText("1.5X");
                            PlaybackParameters param = new PlaybackParameters(1.5f);
                            simpleExoPlayer.setPlaybackParameters(param);
                        }
                        if (which == 4) {
                            // speedTxt.setVisibility(View.VISIBLE);
                            //  speedTxt.setText("2X");
                            PlaybackParameters param = new PlaybackParameters(2f);
                            simpleExoPlayer.setPlaybackParameters(param);
                        }


                    }
                });
                builder.show();


            }
        });


        farwordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() + 10000);
            }
        });
        rewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long num = simpleExoPlayer.getCurrentPosition() - 10000;
                if (num < 0) {
                    simpleExoPlayer.seekTo(0);
                } else {
                    simpleExoPlayer.seekTo(simpleExoPlayer.getCurrentPosition() - 10000);
                }


            }
        });

        ImageView fullscreenButton = playerView.findViewById(R.id.fullscreen);
        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SourceLockedOrientationActivity")
            @Override
            public void onClick(View view) {


                int orientation = PlayVideoActivity.this.getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                    // code for portrait mode

                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


                } else {
                    // code for landscape mode

                    //   Toast.makeText(MainActivity.this, "Land", Toast.LENGTH_SHORT).show();
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                }


            }
        });


        findViewById(R.id.exo_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleExoPlayer.play();

            }
        });
        findViewById(R.id.exo_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                simpleExoPlayer.pause();

            }
        });


        simpleExoPlayer.addListener(new Player.Listener() {
            @Override
            public void onPlaybackStateChanged(int state) {
                if (state == ExoPlayer.STATE_ENDED) {

                }

            }
        });


        playerView.setControllerVisibilityListener(new PlayerControlView.VisibilityListener() {
            @Override
            public void onVisibilityChange(int visibility) {

            }
        });


        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowingTrackSelectionDialog
                        && TrackSelectionDialog.willHaveContent(trackSelector)) {
                    isShowingTrackSelectionDialog = true;
                    TrackSelectionDialog trackSelectionDialog =
                            TrackSelectionDialog.createForTrackSelector(
                                    trackSelector,
                                    /* onDismissListener= */ dismissedDialog -> isShowingTrackSelectionDialog = false);
                    trackSelectionDialog.show(getSupportFragmentManager(), /* tag= */ null);
                }
            }
        });


    }

    protected void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
            trackSelector = null;
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
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
            if(type.equals("video")) {
                Uttilties.downloadManager(this, url1, videoname);
                Toast.makeText(this, "Download start....", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlayVideoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }else if (type.equals("audio")){
                Uttilties.downloadManager(this, url1, audioname);
                Toast.makeText(this, "Download start....", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PlayVideoActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

            }


        } else if (item_id == R.id.delete) {
            FirebaseStorage storage=FirebaseStorage.getInstance();
            StorageReference photoRef = storage.getReferenceFromUrl(url1);

            photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // File deleted successfully
                    databaseReference.child(user_id).child(id).setValue(null);
                    Intent intent = new Intent(PlayVideoActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    Log.d(TAG, "onSuccess: deleted file");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Uh-oh, an error occurred!
                    Log.d(TAG, "onFailure: did not delete file");
                }
            });


        } else {
            finish();

        }
        return super.onOptionsItemSelected(item);
    }

}