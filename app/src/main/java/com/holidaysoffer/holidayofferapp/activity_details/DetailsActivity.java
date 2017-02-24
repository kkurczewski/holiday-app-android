package com.holidaysoffer.holidayofferapp.activity_details;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.holidaysoffer.holidayofferapp.MapActivity;
import com.holidaysoffer.holidayofferapp.Offer;
import com.holidaysoffer.holidayofferapp.R;
import com.holidaysoffer.holidayofferapp.activity_details.gallery.GalleryAdapter;
import com.holidaysoffer.holidayofferapp.activity_main.BasicInfoViewHolder;
import com.holidaysoffer.holidayofferapp.activity_main.MainActivity;
import com.holidaysoffer.holidayofferapp.activity_main.fragments.BaseListFragment;
import com.holidaysoffer.holidayofferapp.network_utils.UrlManager;
import com.holidaysoffer.holidayofferapp.network_utils.VolleyErrorManager;
import com.holidaysoffer.holidayofferapp.network_utils.VolleySingleton;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

import at.blogc.android.views.ExpandableTextView;

public class DetailsActivity extends AppCompatActivity {

    public static final String OFFER_DETAILS = "offer_details_key";
    public static final String NOTIFICATION_OFFER = "notification_offer_key";

    private final String LOG_TAG = this.getClass().getSimpleName();

    private Offer offer;
    private int userId;

    private View snackBarRoot;
    private TextView description;
    private MenuItem favoriteMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        offer = getIntent().getParcelableExtra(OFFER_DETAILS);
        if (offer == null) {
            Log.d(LOG_TAG, "Retrieving offer from notification...");
            offer = getIntent().getParcelableExtra(NOTIFICATION_OFFER);
        }

        bindGallery(offer.getGalleryUrls());

        ActionBar actionBar = getSupportActionBar();
        if (isLandscapeOrientation()) {
            setFullscreenFlags();
            actionBar.hide();
            return;
        }

        actionBar.setTitle(offer.getCountry());
        actionBar.setSubtitle(offer.getCity());
        actionBar.show();

        snackBarRoot = findViewById(R.id.details_root);
        FloatingActionButton mapFab = (FloatingActionButton) findViewById(R.id.fab_map);
        BasicInfoViewHolder basicInfoViewHolder = new BasicInfoViewHolder(findViewById(R.id.basic_info_root));

        if (isTablet()) {
            bindExpandableInfo();
        }

        basicInfoViewHolder.bind(offer, this);

        bindOrHideMapButton(mapFab);
        bindDescription();
        sendRequestForDescription();

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true); // appcompat vector-drawables fix
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        VolleySingleton.getInstance(this).getRequestQueue().cancelAll(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details_activity, menu);
        favoriteMenu = menu.findItem(R.id.action_favorite);
        setFavoriteIcon();

        userId = getSharedPreferences(MainActivity.SHARED_PREF_KEY, MODE_PRIVATE).getInt(MainActivity.USER_ID_PREF, -1);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite:
                boolean newFavoriteState = !offer.isFavorite();
                String url = UrlManager.getFavoritesUrl(userId, offer.getOfferId(), newFavoriteState);
                sendFavoriteActionRequest(url);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent();
        intent.putExtra(BaseListFragment.RETURNED_FAVORITE_FLAG, offer.isFavorite());
        setResult(RESULT_OK, intent);

        // super must be called after setResult, otherwise intent will be not executed and remain null
        super.onBackPressed();
    }

    private void sendRequestForDescription() {
        String url = UrlManager.getDescriptionUrl(offer.getDescriptionUrl());
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            @SuppressWarnings("deprecation")
            public void onResponse(String response) {
                if (Build.VERSION.SDK_INT >= 24) {
                    int flags = Html.FROM_HTML_MODE_LEGACY;
                    description.setText(Html.fromHtml(response, flags));
                } else {
                    description.setText(Html.fromHtml(response));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError networkError) {
                description.setText(R.string.load_error);
                showSnackbar(networkError);
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void sendFavoriteActionRequest(String url) {
        StringRequest request = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                offer.setFavorite(!offer.isFavorite());
                setFavoriteIcon();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError networkError) {
                showSnackbar(networkError);
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void bindDescription() {

        description = (TextView) findViewById(R.id.desc);

        if (!isTablet()) {
            final Button showMoreBtn = (Button) findViewById(R.id.show_more);
            final ExpandableTextView expandableDesc = (ExpandableTextView) description;

            expandableDesc.setInterpolator(new FastOutSlowInInterpolator());
            showMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {
                    expandableDesc.toggle();
                    showMoreBtn.setText(getResources().getString(
                            expandableDesc.isExpanded() ? R.string.show_more : R.string.show_less
                    ));
                }
            });
        }
    }

    private void bindGallery(List<String> galleryUrls) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        CirclePageIndicator circlePageIndicator = (CirclePageIndicator) findViewById(R.id.page_indicator);

        viewPager.setAdapter(new GalleryAdapter(getSupportFragmentManager(), galleryUrls));
        circlePageIndicator.setViewPager(viewPager);
    }

    private void bindExpandableInfo() {
        final View infoShowed = findViewById(R.id.info_showed);
        final View infoHidden = findViewById(R.id.info_hidden);

        infoShowed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoShowed.setVisibility(View.GONE);
                infoHidden.setVisibility(View.VISIBLE);
            }
        });
        infoHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoHidden.setVisibility(View.GONE);
                infoShowed.setVisibility(View.VISIBLE);
            }
        });
    }

    private void bindOrHideMapButton(FloatingActionButton fabMap) {

        final LatLng mapMarker = offer.getMapMarker();
        if (mapMarker == null) {
            fabMap.hide();
            return;
        }

        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), MapActivity.class);
                intent.putExtra(MapActivity.MAP_MARKER, mapMarker);
                startActivity(intent);
            }
        });
    }

    private void showSnackbar(VolleyError networkError) {
        int errorMessageId = VolleyErrorManager.getErrorMessageId(LOG_TAG, networkError);
        Snackbar.make(snackBarRoot, errorMessageId, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendRequestForDescription();
                    }
                })
                .show();
    }

    private void setFullscreenFlags() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            View decorView = getWindow().getDecorView();
            int visibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE // fill screen
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY; // show temporarily bars

            decorView.setSystemUiVisibility(visibility);

        } else {
            int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN; // fixed hidden bars
            getWindow().setFlags(flag, flag); // 1arg: flags, 2arg: mask
        }
    }

    private void setFavoriteIcon() {
        int icon = offer.isFavorite() ? R.drawable.ic_favorite_selected : R.drawable.ic_favorite;
        favoriteMenu.setIcon(icon);
    }

    private boolean isLandscapeOrientation() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.is_tablet);
    }

}
