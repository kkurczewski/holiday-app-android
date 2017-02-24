package com.holidaysoffer.holidayofferapp.activity_main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.holidaysoffer.holidayofferapp.activity_main.fragments.FavoriteListFragment;
import com.holidaysoffer.holidayofferapp.activity_main.fragments.SearchListFragment;

class SectionsPagerAdapter extends FragmentStatePagerAdapter {

    private final int userId;

    SectionsPagerAdapter(FragmentManager fm, int userId) {
        super(fm);
        this.userId = userId;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return SearchListFragment.newInstance(userId);
            case 1:
                return FavoriteListFragment.newInstance(userId);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Search";
            case 1:
                return "Favorites";
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}