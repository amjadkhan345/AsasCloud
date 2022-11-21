package com.asas.cloud.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.asas.cloud.Activity.MainActivity;
import com.asas.cloud.Model.FileModel;
import com.asas.cloud.Model.ImageModel;
import com.asas.cloud.Model.VideoModel;
import com.asas.cloud.R;
import com.asas.cloud.adapter.FileAdapter;
import com.asas.cloud.adapter.ImageAdapter;
import com.asas.cloud.classes.ImageResize;
import com.asas.cloud.classes.References;
import com.asas.cloud.classes.Uttilties;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class ImageFragment extends Fragment {
    ImageAdapter adapter;
    FloatingActionButton button;
    RecyclerView recyclerView;
    int  FILE_SELECT_CODE = 101;
    byte[] image;
    String imgString;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = References.Image_Reference;//database.getReference().child("File");
    DatabaseReference videorefrance= References.Video_Reference;
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



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        button = view.findViewById(R.id.plus_image);
        recyclerView = view.findViewById(R.id.image_recycler_view);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_id = user.getUid();

        FirebaseRecyclerOptions<ImageModel> options
                = new FirebaseRecyclerOptions.Builder<ImageModel>()
                .setQuery(databaseReference.child(user_id), ImageModel.class)
                .build();

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager( new GridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false));
        //gridLayoutManager.setReverseLayout(true);
        recyclerView.setItemAnimator(null);

        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ImageAdapter(options);
        //adapter.notifyDataSetChanged();
        //adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/* , video/*");
                String[] mimetypes = {"image/*", "video/*"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, FILE_SELECT_CODE);
                //intent.addCategory(Intent.CATEGORY_OPENABLE);

                /*try {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select a File to Upload"),
                            FILE_SELECT_CODE);
                } catch (android.content.ActivityNotFoundException ex) {
                    // Potentially direct the user to the Market with a Dialog
                    Toast.makeText(getActivity(), "Please install a File Manager.",
                            Toast.LENGTH_SHORT).show();

                }*/

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //switch (requestCode) {
        //case FILE_SELECT_CODE :
        if (resultCode == RESULT_OK && requestCode == FILE_SELECT_CODE) {
            // Get the Uri of the selected file
            Uri uri1 = data.getData();
            String[] columns = { MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.MIME_TYPE };

            Cursor cursor = getActivity().getContentResolver().query(uri1, columns, null, null, null);
            cursor.moveToFirst();

            int pathColumnIndex     = cursor.getColumnIndex( columns[0] );
            int mimeTypeColumnIndex = cursor.getColumnIndex( columns[1] );

            String contentPath = cursor.getString(pathColumnIndex);
            String mimeType    = cursor.getString(mimeTypeColumnIndex);
            cursor.close();
            Log.d(TAG, "File Uri: " + uri1.toString());
            //String filetype = Uttilties.getfiletype(getContext(), uri1);
            if (mimeType.startsWith("video")) {
                StorageReference riversRef = storageRef.child("Video/" + uri1.getLastPathSegment());
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
                                Bitmap bitmap = ImageResize.getThumbVideo(getContext(), uri1);
                                byte[] image = ImageResize.getBytesFromBitmap(bitmap);
                                String imgString = Base64.encodeToString(image, Base64.NO_WRAP);
                                String videoid = Uttilties.getRandomString(10);
                                VideoModel model = new VideoModel();
                                model.setId(videoid);
                                model.setThambnel(imgString);
                                model.setVideo(uri.toString());
                                Date c = Calendar.getInstance().getTime();
                                model.setData(c);
                                model.setVideo_Name(ImageResize.getFileName(getContext(), uri1));
                                videorefrance.child(user_id).child(videoid).setValue(model);
                                pd.dismiss();
                                Toast.makeText(getActivity(), "file name is " + ImageResize.getFileName(getContext(), uri1), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), MainActivity.class));

                            }
                        });


                    }
                });
                //toString().contains("image"
            } else if (mimeType.startsWith("image")) {
                StorageReference riversRef = storageRef.child("image/" + uri1.getLastPathSegment());
                pd = new ProgressDialog(getActivity());
                pd.show();
                uploadTask = riversRef.putFile(uri1);
                uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        System.out.println("done");

                        pd.setMessage(((int) progress) + "% uploaded");
                        //pd.show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                    }

                }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        riversRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri11) {
                                ImageModel model = new ImageModel();
                                //Bitmap bitmap;

                                //try {
                                //  Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri1);
                                // image = ImageResize.getBytesFromBitmap(bitmap);
                                // imgString = Base64.encodeToString(image, Base64.NO_WRAP);
                                //} catch (IOException e) {
                                //  e.printStackTrace();
                                //}
                                model.setImage(uri11.toString());
                                //model.setVideo_Name(uri.toString());
                                String videoid = Uttilties.getRandomString(10);
                                model.setId(videoid);
                                Date c = Calendar.getInstance().getTime();
                                model.setData(c);
                                model.setVideo_Name(ImageResize.getFileName(getContext(), uri1));
                                databaseReference.child(user_id).child(videoid).setValue(model);
                                pd.dismiss();
                                Toast.makeText(getActivity(), "file name is " + ImageResize.getFileName(getContext(), uri1), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), MainActivity.class));
                            }
                        });
                    }
                });
            }
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