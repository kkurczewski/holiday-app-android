package com.holidaysoffer.holidayofferapp.activity_main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.holidaysoffer.holidayofferapp.BuildConfig;
import com.holidaysoffer.holidayofferapp.R;
import com.holidaysoffer.holidayofferapp.network_utils.NetworkResponseListener;
import com.holidaysoffer.holidayofferapp.network_utils.UrlManager;
import com.holidaysoffer.holidayofferapp.network_utils.VolleyErrorManager;
import com.holidaysoffer.holidayofferapp.network_utils.VolleySingleton;

public class MainActivity extends AppCompatActivity implements NetworkResponseListener<String> {

    public static final String SHARED_PREF_KEY = "com.holidaysoffer.holidayofferapp.shared_preferences";
    public static final String USER_ID_PREF = "user_id_preference_key";

    private final String LOG_TAG = getClass().getSimpleName();

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.container);

        preferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);
        int userId = preferences.getInt(USER_ID_PREF, -1);

        if (userId == -1) {
            sendUserIdRequest();
            return;
        }

        setupViewPager(userId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity, menu);
        createDebugMenu(menu);

        return true;
    }

    @Override
    public void onResponse(String response) {
        int userId = Integer.valueOf(response);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(USER_ID_PREF, userId);
        editor.apply();

        setupViewPager(userId);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        int errorMessage = VolleyErrorManager.getErrorMessageId(LOG_TAG, error);

        Snackbar.make(findViewById(R.id.main_activity_root), errorMessage, Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        sendUserIdRequest();
                    }
                }).show();
    }

    private void setupViewPager(int userId) {

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true); // fixes appcompat vector-drawables
        }

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), userId);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void sendUserIdRequest() {
        StringRequest userIdRequest = new StringRequest(UrlManager.getUserRegisterUrl(), this, this);
        VolleySingleton.getInstance(MainActivity.this).addToRequestQueue(userIdRequest);
    }

    private void createDebugMenu(Menu menu) {
        if (BuildConfig.DEBUG) {
            menu.add("Dev: Set server IP").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {

                    final String[] debugServers = getResources().getStringArray(R.array.servers);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Choose server:")
                            .setSingleChoiceItems(debugServers, 0, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    UrlManager._debug_setUrl(debugServers[i]);
                                    dialogInterface.dismiss();
                                    Log.d(UrlManager.URL_LOG, UrlManager.getHostUrl());
                                }
                            });
                    builder.create().show();
                    return false;
                }
            });
        }
    }

}
