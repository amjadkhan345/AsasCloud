package com.asas.cloud.Activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.asas.cloud.R;
import com.asas.cloud.adapter.PageAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseUser user;


    FragmentContainerView fragment;
    ImageView home, notification, create_file, profile;

    TabLayout tabLayout;
    TabItem home_tabe, noti_tab, create_tab, pro_tab;
    ViewPager viewPager;
    PagerAdapter adapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);

        }
        askPermissions();
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
                if (tab.getPosition() == 0 || tab.getPosition() == 1 ) {
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
            Intent intent = new Intent(MainActivity.this, CountactActivity.class);
            startActivity(intent);
            //finishAffinity();
            //getActivity().finish();
        }else if (item_id== R.id.profile){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            }


            //}else if(item_id==R.id.purches){
          //  startActivity(new Intent(MainActivity.this, ProductActivity.class));

        }
        else {
            //Intent intent = new Intent(this, PlayStoreActivity.class);
            //startActivity(intent);
            //getActivity().finish();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static final int STORAGE_PERMISSION_REQUEST_CODE = 1;


    private void askPermissions() {

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
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                STORAGE_PERMISSION_REQUEST_CODE);
                    }
                });
                builder.show();

            } //asking permission for first time
            else {
                // Show permission request popup for the first time
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);

            }

        }
    }

}

