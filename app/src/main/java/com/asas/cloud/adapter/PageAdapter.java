package com.asas.cloud.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.asas.cloud.fragment.AudioFragment;
import com.asas.cloud.fragment.ContactsFragment;
import com.asas.cloud.fragment.FileFragment;
import com.asas.cloud.fragment.HomeFragment;
import com.asas.cloud.fragment.ImageFragment;

public class PageAdapter extends FragmentPagerAdapter {
    int tabcount;

    public PageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        tabcount = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position ){
            case 0 : return  new HomeFragment();
            //case 1 : return  new NotifictionFragment();
            case 1 : return  new ImageFragment();
            case 2 : return  new FileFragment();
            case 3 : return new AudioFragment();
            case 4 : return new ContactsFragment();
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return tabcount;
    }

}
