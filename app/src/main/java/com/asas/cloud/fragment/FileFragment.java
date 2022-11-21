package com.asas.cloud.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asas.cloud.Activity.MainActivity;
import com.asas.cloud.Model.FileModel;
import com.asas.cloud.R;
import com.asas.cloud.adapter.FileAdapter;
import com.asas.cloud.classes.ImageResize;
import com.asas.cloud.classes.References;
import com.asas.cloud.classes.Uttilties;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URISyntaxException;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class FileFragment extends Fragment {
    FileAdapter adapter;
    FloatingActionButton button;
    RecyclerView recyclerView;
    int  FILE_SELECT_CODE = 101;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = References.File_Reference;//database.getReference().child("File");
    StorageReference storageRef = storage.getReference();
    StorageReference imagesRef = storageRef.child("File");
    FirebaseAuth auth;
    FirebaseUser user;
    FragmentContainerView layout;
    //MyRecyclerViewAdapter adapter;
    //StorageReference riversRef = storageRef.child("images/"+file.getLastPathSegment());
    //StorageReference  storageReference= storageRef.getReference();
    UploadTask uploadTask;
    ProgressDialog pd;
    String user_id, username, userprofile,user_uuid;
    //RecyclerView recyclerView;
    ////FirebaseDatabase database;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_file, container, false);
        button = view.findViewById(R.id.plus_file);
        recyclerView = view.findViewById(R.id.file_recycler_view);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_id = user.getUid();

        FirebaseRecyclerOptions<FileModel> options
                = new FirebaseRecyclerOptions.Builder<FileModel>()
                .setQuery(databaseReference.child(user_id), FileModel.class)
                .build();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager( new GridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false));
        //gridLayoutManager.setReverseLayout(true);
        recyclerView.setItemAnimator(null);

        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new FileAdapter(options);
        //adapter.notifyDataSetChanged();
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    //intent.setType("image/*");
                    //intent.addCategory(Intent.CATEGORY_OPENABLE);


                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.addCategory(Intent.CATEGORY_OPENABLE);

                try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(getActivity(), "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();

                }

            }
        });


        return view;//inflater.inflate(R.layout.fragment_file, container, false);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //switch (requestCode) {
            //case FILE_SELECT_CODE :
                if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CODE) {
                    // Get the Uri of the selected file
                    Uri uri1 = data.getData();
                    Log.d(TAG, "File Uri: " + uri1.toString());
                    //String path = null;
                    // Get the path
                    //String path = PathUtil.getPath(getActivity(), uri);
                    //storageRef.child("pdf").setValue(path);
                    StorageReference riversRef = storageRef.child("File/"+uri1.getLastPathSegment());
                    pd = new ProgressDialog(getActivity());
                    pd.show();
                    uploadTask = riversRef.putFile(uri1);
                    uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            System.out.println("done");
                            //Toast.makeText(this, "upload is " + progress + "% done", Toast.LENGTH_LONG).show();
                            //ProgressDialog progressDialog = new ProgressDialog(progress);
                            //ProgressBar progressBar = new ProgressBar();
                            // pd = new ProgressDialog(CreateFileActivity.this);
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
                            riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    FileModel model = new FileModel();
                                    model.setFile_Path(uri.toString());
                                    Date c = Calendar.getInstance().getTime();
                                    String videoid = Uttilties.getRandomString(15);
                                    model.setId(videoid);
                                    model.setData(c);
                                    model.setFile_Name(ImageResize.getFileName(getContext(), uri1));
                                    databaseReference.child(user_id).push().setValue(model);
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "file name is " + ImageResize.getFileName(getContext(), uri1), Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), MainActivity.class));

                                }
                            });


                        }
                    });
                }

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