package com.holidaysoffer.holidayofferapp.activity_main.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.holidaysoffer.holidayofferapp.R;
import com.holidaysoffer.holidayofferapp.activity_search.SearchActivity;
import com.holidaysoffer.holidayofferapp.activity_search.SearchParameters;
import com.holidaysoffer.holidayofferapp.network_utils.UrlManager;

public class SearchListFragment extends BaseListFragment {

    public static final int REQUEST_SEARCH_PARAMS = 222;
    public static final String RETURNED_SEARCH_PARAMS = "search_parameters";

    private SearchParameters searchParameters;

    public static SearchListFragment newInstance(int userId) {
        SearchListFragment fragment = new SearchListFragment();

        Bundle args = new Bundle();
        args.putInt(USER_ID, userId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        View fab = view.findViewById(R.id.search_fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchActivity();
            }
        });
    }

    @Override
    protected String getUrl() {
        return UrlManager.getJsonForOffersUrl(userId, searchParameters);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_SEARCH_PARAMS:
                if (resultCode == Activity.RESULT_OK) {
                    searchParameters = (SearchParameters) data.getSerializableExtra(RETURNED_SEARCH_PARAMS);
                } else {
                    searchParameters = null;
                }
                super.sendRequestForOfferList();
                break;
        }
    }

    private void showSearchActivity() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        if (searchParameters != null) {
            intent.putExtra(RETURNED_SEARCH_PARAMS, searchParameters);
        }
        startActivityForResult(intent, REQUEST_SEARCH_PARAMS);
    }
}
