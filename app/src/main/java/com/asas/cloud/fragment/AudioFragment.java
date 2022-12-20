package com.asas.cloud.fragment;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.asas.cloud.Model.AudioModel;
import com.asas.cloud.R;
import com.asas.cloud.adapter.AudioAdapter;
import com.asas.cloud.adapter.AudioMain;
import com.asas.cloud.classes.References;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AudioFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AudioFragment extends Fragment {
    AudioAdapter adapter;
    FloatingActionButton button;
    RecyclerView recyclerView;
    int  FILE_SELECT_CODE = 101;
    byte[] image;
    String imgString;
    Bitmap bitmap ;

    private Uri ImageUri;
    ArrayList ImageList = new ArrayList();
    private int upload_count = 0;
    private ProgressDialog progressDialog;
    ArrayList urlStrings;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = References.Video_Reference;//database.getReference().child("File");
    DatabaseReference imagerefrance= References.Image_Reference;
    DatabaseReference Userref = References.User_Reference;
    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("File");
    DatabaseReference user_storage=References.User_Reference; //database.getReference();
    FirebaseAuth auth;
    FirebaseUser user;
    FragmentContainerView layout;
    //MyRecyclerViewAdapter adapter;
    //StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
    //StorageReference  storageReference= storageRef.getReference();
    UploadTask uploadTask;
    ProgressDialog pd;
    String user_id, username, userprofile,user_uuid;
    String totale_size, use_size;
    int tootle, use;
    List<AudioModel> list;
    boolean ads;
    AudioMain mainVideo;
    ProgressBar progressBar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AudioFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AudioFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AudioFragment newInstance(String param1, String param2) {
        AudioFragment fragment = new AudioFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_audio, container, false);
        recyclerView = view.findViewById(R.id.audio_rec);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_id = user.getUid();
        progressBar =view.findViewById(R.id.progress_audio);
        FirebaseRecyclerOptions<AudioModel> options
                = new FirebaseRecyclerOptions.Builder<AudioModel>()
                .setQuery(References.AUDIO_Reference.child(user_id), AudioModel.class)
                .build();

        References.ADS_Reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String ads1= snapshot.child("ads").getValue().toString();
                if (ads1.equals("true")){
                    ads=true;
                }else{
                    ads=false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new AudioAdapter(options);
        list = new ArrayList<>();
        list.clear();
        // = new FileMain()
        mainVideo = new AudioMain(getActivity(), list);

        References.AUDIO_Reference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot ds: snapshot.getChildren()){

                    AudioModel model = ds.getValue(AudioModel.class);
                    //list = new ArrayList<>();
                    //list.clear();
                    list.add(model);
                    mainVideo.notifyDataSetChanged();
                    progressBar.setVisibility(View.GONE);
                    //Toast.makeText(getContext(), list.get(0).getVideo_Name(), Toast.LENGTH_SHORT).show();

                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //recyclerView.setLayoutManager( new GridLayoutManager(getContext(),4, LinearLayoutManager.VERTICAL,false));
        //gridLayoutManager.setReverseLayout(true);
        //recyclerView.setLayoutManager(LinearLayoutManager.HORIZONTAL);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setItemAnimator(null);
        recyclerView.setLayoutManager(mLayoutManager);
        if(ads==true) {
            recyclerView.setAdapter(mainVideo);
        }else{
            recyclerView.setAdapter(adapter);
        }


        return view;
    }
    @Override
    public void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}