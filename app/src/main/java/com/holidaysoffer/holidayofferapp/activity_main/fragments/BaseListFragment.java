package com.holidaysoffer.holidayofferapp.activity_main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.holidaysoffer.holidayofferapp.Offer;
import com.holidaysoffer.holidayofferapp.R;
import com.holidaysoffer.holidayofferapp.activity_details.DetailsActivity;
import com.holidaysoffer.holidayofferapp.activity_main.MainActivity;
import com.holidaysoffer.holidayofferapp.activity_main.OfferListAdapter;
import com.holidaysoffer.holidayofferapp.network_utils.JsonParser;
import com.holidaysoffer.holidayofferapp.network_utils.NetworkResponseListener;
import com.holidaysoffer.holidayofferapp.network_utils.UrlManager;
import com.holidaysoffer.holidayofferapp.network_utils.VolleyErrorManager;
import com.holidaysoffer.holidayofferapp.network_utils.VolleySingleton;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;

public abstract class BaseListFragment extends Fragment
        implements NetworkResponseListener<JSONArray>, ClickCallback {

    public final static String RETURNED_FAVORITE_FLAG = "returned_favorite_flag_key";
    protected final static String USER_ID = "user_id_key";
    protected final static int REQUEST_FAVORITE_FLAG = 111;
    protected final String LOG_TAG = getClass().getSimpleName();

    protected List<Offer> offers = new ArrayList<>();
    protected Offer clickedOffer;
    protected int userId;

    private Snackbar snackbar;
    private OfferListAdapter adapter;
    private RecyclerView recycler;
    private ProgressBar progressBar;
    private TextView emptyListLabel;
    private SwipeRefreshLayout swipeRefreshLayout;

    abstract protected String getUrl();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        userId = getArguments().getInt(USER_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_recycler, container, false);

        recycler = (RecyclerView) root.findViewById(R.id.offerRecycler);
        progressBar = (ProgressBar) root.findViewById(R.id.item_progress_bar);
        emptyListLabel = (TextView) root.findViewById(R.id.recycler_empty_text);

        swipeRefreshLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sendRequestForOfferList();
            }
        });

        adapter = new OfferListAdapter(offers, getContext(), this);
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);

        sendRequestForOfferList();

        return root;
    }

    @Override
    public void onResponse(JSONArray response) {
        addOffers(JsonParser.getOffers(response));
        notifyDataChanged();
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        int errorMessage = VolleyErrorManager.getErrorMessageId(LOG_TAG, error);
        snackbar = Snackbar.make(recycler, errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendRequestForOfferList();
                    }
                });
        snackbar.show();
        notifyDataChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_refresh:
                sendRequestForOfferList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStop() {
        super.onStop();
        VolleySingleton.getInstance(getContext()).getRequestQueue().cancelAll(getContext());
    }

    @Override
    public void onClick(Offer offer) {

        if (isFragmentAdded()) {
            Intent intent = new Intent(getContext(), DetailsActivity.class);
            intent.putExtra(DetailsActivity.OFFER_DETAILS, offer);
            startActivityForResult(intent, REQUEST_FAVORITE_FLAG);

            clickedOffer = offer;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_FAVORITE_FLAG:
                boolean favoriteFlag = data.getBooleanExtra(RETURNED_FAVORITE_FLAG, false);
                clickedOffer.setFavorite(favoriteFlag);
                break;
        }
    }

    protected void sendRequestForOfferList() {
        if (snackbar != null) snackbar.dismiss();
        swipeRefreshLayout.setRefreshing(true);
        String url = getUrl();
        JsonRequest request = new JsonArrayRequest(url, this, this);
        VolleySingleton.getInstance(getContext()).getRequestQueue().add(request);
        Log.d(UrlManager.URL_LOG, url);
    }

    protected void addOffers(List<Offer> actualOffers) {
        offers.clear();
        offers.addAll(actualOffers);
    }

    protected void notifyDataChanged() {

        swipeRefreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);

        int visibility = offers.isEmpty() ? View.VISIBLE : View.GONE;
        emptyListLabel.setVisibility(visibility);
        adapter.notifyDataSetChanged();
    }

    private boolean isFragmentAdded() {
        return isAdded();
    }

}
