package com.holidaysoffer.holidayofferapp.activity_main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.holidaysoffer.holidayofferapp.Offer;
import com.holidaysoffer.holidayofferapp.network_utils.UrlManager;

import java.util.List;

public class FavoriteListFragment extends BaseListFragment {

    public static FavoriteListFragment newInstance(int userId) {
        FavoriteListFragment fragment = new FavoriteListFragment();

        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected String getUrl() {
        return UrlManager.getJsonForUserFavoritesUrl(userId);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // extends default behavior by removing offers no longer marked as favorite
        switch (requestCode) {
            case REQUEST_FAVORITE_FLAG:
                boolean favoriteFlag = data.getBooleanExtra(RETURNED_FAVORITE_FLAG, false);
                if ( ! favoriteFlag) {
                    offers.remove(clickedOffer);
                    super.notifyDataChanged();
                }
                break;
            default:
                Log.d(LOG_TAG, "Unexpected request code: " + requestCode);
                break;
        }
    }

    @Override
    protected void addOffers(List<Offer> actualOffers) {
        super.addOffers(actualOffers);

        for (Offer o : offers) o.setFavorite(true);
    }

}
