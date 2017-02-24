package com.holidaysoffer.holidayofferapp.activity_details.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class GalleryAdapter extends FragmentStatePagerAdapter {

    private final List<String> imagesUrls;

    public GalleryAdapter(FragmentManager fm, List<String> imagesUrls) {
        super(fm);
        this.imagesUrls = imagesUrls;
    }

    @Override
    public int getCount() {
        return imagesUrls.size();
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = new GalleryFragment();

        Bundle args = new Bundle();
        args.putString(GalleryFragment.IMAGE_URL, imagesUrls.get(position));
        fragment.setArguments(args);

        return fragment;
    }

}
