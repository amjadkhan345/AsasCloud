package com.asas.cloud.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.asas.cloud.Model.AudioModel;
import com.asas.cloud.Model.ContactModel;
import com.asas.cloud.Model.FileModel;
import com.asas.cloud.Model.ImageModel;
import com.asas.cloud.Model.VideoModel;
import com.asas.cloud.R;
import com.asas.cloud.adapter.PageAdapter;
import com.asas.cloud.classes.ImageResize;
import com.asas.cloud.classes.References;
import com.asas.cloud.classes.Uttilties;
import com.facebook.login.LoginManager;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final int IMAGE_CODE=100;
    final int VIDEO_CODE=101;
    final int AUDIO_CODE=102;
    final int FILE_CODE=103;
    final int CONTACT_CODE=104;
    String ContctMobVar;
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
    long tootle, use;

    FloatingActionButton button;

    //FirebaseAuth auth;
    //FirebaseUser user;
    AdView adView;
    AdView mAdView;
    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions= new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_EXTERNAL_STORAGE};


    FragmentContainerView fragment;
    ImageView home, notification, create_file, profile;

    TabLayout tabLayout;
    TabItem home_tabe, noti_tab, create_tab, pro_tab;
    ViewPager viewPager;
    PagerAdapter adapter;
    Toolbar toolbar;
    boolean ads = false;
    DatabaseReference reference= References.User_Reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        user_id = user.getUid();
        button=findViewById(R.id.popup);



        if (!checkPermissions()){
            checkPermissions();
        }
        //  permissions  granted.


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
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //Toast.makeText(MainActivity.this, " successful ", Toast.LENGTH_SHORT).show();
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        if(ads) {
            mAdView.loadAd(adRequest);
        }

        References.User_Reference.child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    totale_size = snapshot.child("user_storage").getValue().toString();
                    tootle = Long.parseLong(totale_size);
                    use_size = snapshot.child("size").getValue().toString();
                    use = Long.parseLong(use_size);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (user == null) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);

        }

        button.setOnClickListener(new View.OnClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.Q)
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                if (use>tootle ){
                    Toast.makeText(getApplicationContext(), " Your don't have Free space", Toast.LENGTH_SHORT).show();
                }else {
                    //Creating the instance of PopupMenu
                    PopupMenu popup = new PopupMenu(MainActivity.this, button);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.popuo_menu, popup.getMenu());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        popup.setForceShowIcon(true);
                    }
                    //try {
                    //  Field mFieldPopup=popup.getClass().getDeclaredField("mPopup");
                    // mFieldPopup.setAccessible(true);
                    // MenuPopupHelper mPopup = (MenuPopupHelper) mFieldPopup.get(popup);
                    // mPopup.setForceShowIcon(true);
                    //} catch (Exception e) {}

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.p_file:
                                    //uploadeFile()
                                /*Intent intent = new Intent();//Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                intent.setType("pdf/*");
                                //String[] mimetypes = {"image/*", "video/*"};
                                //intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                //startActivityForResult(intent, FILE_SELECT_CODE);
                                startActivityForResult(Intent.createChooser(intent, "Select file"), 100);*/
                                    chosefile();
                                    break;
                                case R.id.p_audio:
                                    Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                                    //intent.setType("image/* , video/*");
                                    //String[] mimetypes1 = {"audio/*"};
                                    intent1.setType("audio/*");//putExtra(Intent.EXTRA_MIME_TYPES, mimetypes1);
                                    intent1.setAction(Intent.ACTION_GET_CONTENT);
                                    //intent1.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                    startActivityForResult(intent1, AUDIO_CODE);
                                    //startActivityForResult(Intent.createChooser(intent1, "Select Picture"), 101);
                                    break;
                                case R.id.p_contact:
                                    Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                                    pickContact.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE);
                                    //startActivityForResult(pickContact, 1);
                                    startActivityForResult(Intent.createChooser(pickContact, "Select Picture"), CONTACT_CODE);
                                    break;
                                case R.id.p_image:
                                    Intent intent3 = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    //intent.setType("image/* , video/*");
                                    //String[] mimetypes3 = {"image/*"};
                                    //intent3.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes3);
                                    intent3.setType("image/*");
                                    intent3.setAction(Intent.ACTION_GET_CONTENT);
                                    intent3.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                    startActivityForResult(intent3, IMAGE_CODE);
                                    //startActivityForResult(Intent.createChooser(intent3, "Select Picture"), 103);
                                    break;
                                case R.id.p_video:
                                    Intent intent4 = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                                    intent4.setType("video/*");
                                    //String[] mimetypes4 = {"video/*"};
                                    //intent4.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes4);
                                    //intent4.setType()
                                    intent4.setAction(Intent.ACTION_GET_CONTENT);
                                    intent4.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                    startActivityForResult(intent4, VIDEO_CODE);
                                    //startActivityForResult(Intent.createChooser(intent4, "Select Picture"), 104);

                                    break;

                            }
                            return true;
                        }
                    });
                    popup.show(); //showing popup menu
                }
            }
        });

        reference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                }else {
                    Intent intent = new Intent(MainActivity.this, CreateUserActivity.class);
                    intent.putExtra("name", user.getDisplayName());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("image","2");
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //askPermissions();
        //home = findViewById(R.id.home);
        //notification = findViewById(R.id.notification);
        //create_file = findViewById(R.id.create_file);
        //profile = findViewById(R.id.profile);
        //fragment = findViewById(R.id.recyclerview);
        home_tabe = findViewById(R.id.home_tab);
        //noti_tab = findViewById(R.id.notification_tab);
        //create_tab = findViewById(R.id.upload_tab);
        pro_tab = findViewById(R.id.profile_tab);
        viewPager = findViewById(R.id.vpeger);
        toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabLayout);
        adapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        setSupportActionBar(toolbar);
        toolbar.inflateMenu(R.menu.mainmenu);
        //MenuItem menuItem = toolbar.getMenu().findItem(R.id.search_bar);
        //menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItem.SHOW_AS_ACTION_ALWAYS);
        //SearchView searchView = (SearchView) menuItem.getActionView();
        //searchView.setQueryHint("Search Book");
        //setHasOptionsMenu(true);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Asas Cloud");
            //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0 || tab.getPosition() == 1 || tab.getPosition() == 2 || tab.getPosition() == 3 || tab.getPosition() == 4 ) {
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
    }

    private void chosefile() {

        String[] mimeTypes =
                {"application/msword","application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                        "application/vnd.ms-powerpoint","application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                        "application/vnd.ms-excel","application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                        "text/plain",
                        "application/pdf",
                        "application/zip"};

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
        if (mimeTypes.length > 0) {
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        }
        startActivityForResult(Intent.createChooser(intent,"ChooseFile"), FILE_CODE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id = item.getItemId();
        if (item_id == R.id.seating) {
            Intent intent = new Intent(this, SeetingActivity.class);
            startActivity(intent);
            //getActivity().finish();

        } else if (item_id == R.id.countect_us) {
            Intent intent = new Intent(MainActivity.this, PravicyActivity.class);
            startActivity(intent);
            //finishAffinity();
            //getActivity().finish();
        }else if (item_id== R.id.profile){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }


            //}else if(item_id==R.id.purches){
          //  startActivity(new Intent(MainActivity.this, ProductActivity.class));

        }else if(item_id== R.id.invite){
            final String appPackageName = "https://play.google.com/store/apps/details?id=" + getPackageName();//getPackageName();//"com."; // your application package name
            String send_string= "Asascloud provide you 20 GB free space to store your data. i recommend this app \n" + appPackageName;
            //startActivity(new Intent(Intent.ACTION_VIEW,
            // Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, send_string);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        }else if(item_id== R.id.rate){
            final String appPackageName = "https://play.google.com/store/apps/details?id=" + getPackageName();//getPackageName();//"com."; // your application package name
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appPackageName)));
        }
        else {
            //Intent intent = new Intent(this, PlayStoreActivity.class);
            //startActivity(intent);
            //getActivity().finish();
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static final int STORAGE_PERMISSION_REQUEST_CODE = 1;

    private  boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p:permissions) {
            result = ContextCompat.checkSelfPermission(getApplicationContext(),p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissionsList, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    String permissionsDenied = "";
                    for (String per : permissionsList) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            permissionsDenied += "\n" + per;

                        }

                    }
                    // Show permissionsDenied
                    //updateViews();
                }
                return;
            }
        }
    }


    private void askPermissions() {
        //Manifest.permission[]
        //Array per[] = new Array[Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_CONTACTS,
            //    Manifest.permission.READ_CONTACTS,
             //   Manifest.permission.READ_EXTERNAL_STORAGE];

        int permissionCheckStorage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // we already asked for permisson & Permission granted, call camera intent
        if (permissionCheckStorage == PackageManager.PERMISSION_GRANTED) {

            //do what you want

        } else {

            // if storage request is denied
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("You need to give permission to access storage in order to work this feature.");
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setPositiveButton("GIVE PERMISSION", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        // Show permission request popup
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                                STORAGE_PERMISSION_REQUEST_CODE);
                    }
                });
                builder.show();

            } //asking permission for first time
            else {
                // Show permission request popup for the first time
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                        STORAGE_PERMISSION_REQUEST_CODE);

            }

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK && requestCode== VIDEO_CODE){
            Uri uri1 = data.getData();
            StorageReference riversRef = storageRef.child("Video/" + uri1.getLastPathSegment());
            pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setTitle("Upload File");
            pd.show();
            uploadTask = riversRef.putFile(uri1);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("done");

                    pd.setMessage(((int) progress) + "% Uploading...");
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
                            long fileSize = Uttilties.getvideosize(MainActivity.this, uri1);
                            String mb = Uttilties.FileSize(fileSize);
                            //Toast.makeText(getActivity(), mb, Toast.LENGTH_LONG).show();
                            Userref.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        long filesize = snapshot.child("size").getValue(long.class);
                                        long total_size = filesize + fileSize;
                                        //Toast.makeText(getContext(), String.valueOf(fileSize), Toast.LENGTH_SHORT).show();
                                        Userref.child(user_id).child("size").setValue(total_size);
                                        //name.setText((int) i);//snapshot.child("name").getValue().toString());
                                        Bitmap bitmap = ImageResize.getThumbVideo(MainActivity.this, uri1);
                                        byte[] image = ImageResize.getBytesFromBitmap(bitmap);
                                        String imgString = Base64.encodeToString(image, Base64.NO_WRAP);
                                        String videoid = Uttilties.getRandomString(10);
                                        VideoModel model = new VideoModel();
                                        model.setId(videoid);
                                        model.setThambnel(imgString);
                                        model.setVideo(uri.toString());
                                        Date c = Calendar.getInstance().getTime();
                                        model.setData(c);
                                        model.setVideo_Name(ImageResize.getFileName(MainActivity.this, uri1));
                                        databaseReference.child(user_id).child(videoid).setValue(model);
                                        pd.dismiss();
                                        //Toast.makeText(getActivity(), "file name is " + ImageResize.getFileName(getContext(), uri1), Toast.LENGTH_SHORT).show();
                                        //startActivity(new Intent(getActivity(), MainActivity.class));
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            });




        }else if (resultCode==RESULT_OK && requestCode== IMAGE_CODE){
            Uri uri1= data.getData();
            StorageReference riversRef = storageRef.child("image/" + uri1.getLastPathSegment());
            pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setTitle("Upload File");
            pd.show();
            uploadTask = riversRef.putFile(uri1);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("done");

                    pd.setMessage(((int) progress) + "% Uploading...");
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
                            long bitmap_size = Uttilties.getvideosize(MainActivity.this,uri1);
                            String e= Uttilties.FileSize(bitmap_size);
                            //Toast.makeText(getActivity(), e, Toast.LENGTH_LONG).show();
                            Userref.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        long filesize = snapshot.child("size").getValue(long.class);
                                        long total_size = filesize + bitmap_size;
                                        Userref.child(user_id).child("size").setValue(total_size);
                                        //Toast.makeText(getContext(), String.valueOf(bitmap_size), Toast.LENGTH_SHORT).show();
                                        ImageModel model = new ImageModel();
                                        model.setImage(uri11.toString());
                                        //model.setVideo_Name(uri.toString());
                                        String videoid = Uttilties.getRandomString(10);
                                        model.setId(videoid);
                                        Date c = Calendar.getInstance().getTime();
                                        model.setData(c);
                                        model.setVideo_Name(ImageResize.getFileName(MainActivity.this, uri1));
                                        imagerefrance.child(user_id).child(videoid).setValue(model);
                                        pd.dismiss();
                                        //Toast.makeText(getActivity(), "file name is " + ImageResize.getFileName(getContext(), uri1), Toast.LENGTH_SHORT).show();
                                        //startActivity(new Intent(getActivity(), MainActivity.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }


            });

        }else if (resultCode==RESULT_OK && requestCode== FILE_CODE){

            Uri uri1= data.getData();
            String filename = ImageResize.getFileName(MainActivity.this, uri1);
            StorageReference riversRef = storageRef.child("file/" + filename);
            pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setTitle("Upload File");
            pd.show();
            uploadTask = riversRef.putFile(uri1);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("done");

                    pd.setMessage(((int) progress) + "% Uploading...");
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
                            long bitmap_size = Uttilties.getfilesize(MainActivity.this, uri1);//getvideosize(MainActivity.this,uri1);
                            String e= Uttilties.FileSize(bitmap_size);
                            Toast.makeText(MainActivity.this, e, Toast.LENGTH_LONG).show();
                            Userref.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        long filesize = snapshot.child("size").getValue(long.class);
                                        long total_size = filesize + bitmap_size;
                                        Userref.child(user_id).child("size").setValue(total_size);
                                        //Toast.makeText(getContext(), String.valueOf(bitmap_size), Toast.LENGTH_SHORT).show();
                                        FileModel model = new FileModel();
                                        model.setFile_Path(uri11.toString());
                                        //model.setVideo_Name(uri.toString());
                                        String videoid = Uttilties.getRandomString(10);
                                        model.setId(videoid);
                                        Date c = Calendar.getInstance().getTime();
                                        model.setData(c);
                                        model.setFile_Name(ImageResize.getFileName(MainActivity.this, uri1));
                                        References.File_Reference.child(user_id).child(videoid).setValue(model);
                                        pd.dismiss();
                                        //Toast.makeText(getActivity(), "file name is " + ImageResize.getFileName(getContext(), uri1), Toast.LENGTH_SHORT).show();
                                        //startActivity(new Intent(getActivity(), MainActivity.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }


            });


        }else if (resultCode==RESULT_OK && requestCode== AUDIO_CODE){
            Uri uri1= data.getData();
            String filename = ImageResize.getFileName(MainActivity.this, uri1);
            StorageReference riversRef = storageRef.child("audio/" + filename);
            pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setTitle("Upload File");
            pd.show();
            uploadTask = riversRef.putFile(uri1);
            uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    System.out.println("done");

                    pd.setMessage(((int) progress) + "% Uploading...");
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
                            long bitmap_size = Uttilties.getvideosize(MainActivity.this,uri1);
                            String e= Uttilties.FileSize(bitmap_size);
                            //Toast.makeText(getActivity(), e, Toast.LENGTH_LONG).show();
                            Userref.child(user_id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        long filesize = snapshot.child("size").getValue(long.class);
                                        long total_size = filesize + bitmap_size;
                                        Userref.child(user_id).child("size").setValue(total_size);
                                        //Toast.makeText(getContext(), String.valueOf(bitmap_size), Toast.LENGTH_SHORT).show();
                                        AudioModel model = new AudioModel();
                                        model.setAudioPath(uri11.toString());
                                        //model.setVideo_Name(uri.toString());
                                        String videoid = Uttilties.getRandomString(10);
                                        model.setId(videoid);
                                        Date c = Calendar.getInstance().getTime();
                                        model.setData(c);
                                        model.setVideo_Name(ImageResize.getFileName(MainActivity.this, uri1));
                                        References.AUDIO_Reference.child(user_id).child(videoid).setValue(model);
                                        pd.dismiss();
                                        //Toast.makeText(getActivity(), "file name is " + ImageResize.getFileName(getContext(), uri1), Toast.LENGTH_SHORT).show();
                                        //startActivity(new Intent(getActivity(), MainActivity.class));
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }


            });


        }else if (resultCode==RESULT_OK && requestCode== CONTACT_CODE){
            pd = new ProgressDialog(MainActivity.this);
            pd.setCancelable(false);
            pd.setTitle("Upload Data");
            pd.show();
            Uri contctDataVar = data.getData();

            Cursor contctCursorVar = getContentResolver().query(contctDataVar, null,
                    null, null, null);
            if (contctCursorVar.getCount() > 0)
            {
                while (contctCursorVar.moveToNext())

                {
                    int phone = contctCursorVar.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);
                    //int phoneIndex = getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String ContctUidVar = contctCursorVar.getString(phone);//contctCursorVar.getColumnIndex(ContactsContract.Contacts._ID));
                    int name = contctCursorVar.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                    String ContctNamVar = contctCursorVar.getString(name);

                    //Log.i("Names", ContctNamVar);



                    if (Integer.parseInt(contctCursorVar.getString(phone)) > 0)
                    {
                        int my22=  contctCursorVar.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                        ContctMobVar = contctCursorVar.getString(my22);
                        //Log.i("Number", ContctMobVar);
                    }
                    ContactModel model=new ContactModel();
                    model.setContact_Name(ContctNamVar);
                    model.setContact_Number(ContctMobVar);
                    Date c = Calendar.getInstance().getTime();
                    model.setData(c);
                    String fileid = Uttilties.getRandomString(10);
                    model.setContact_Id(fileid);
                    References.CONTACT_Reference.child(user_id).child(fileid).setValue(model);
                    pd.dismiss();

                }
            }
        }








    }
}

