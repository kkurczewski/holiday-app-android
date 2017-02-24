package com.holidaysoffer.holidayofferapp.push_notifications;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.holidaysoffer.holidayofferapp.network_utils.NetworkResponseListener;
import com.holidaysoffer.holidayofferapp.network_utils.UrlManager;
import com.holidaysoffer.holidayofferapp.network_utils.VolleyErrorManager;
import com.holidaysoffer.holidayofferapp.network_utils.VolleySingleton;

import java.util.HashMap;
import java.util.Map;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements NetworkResponseListener<String> {

    private static final String LOG_TAG = "firebase_token";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(LOG_TAG, "Refreshed token: " + token);
        sendTokenToServer(token);
    }

    private void sendTokenToServer(final String token) {

        String url = UrlManager.getTokenRegisterUrl();

        Log.d(LOG_TAG, "Registering token...");

        StringRequest requestTokenRegister = new StringRequest(Request.Method.POST, url, this, this) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("token", token);
                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(requestTokenRegister);
    }

    @Override
    public void onResponse(String response) {
        Log.d(LOG_TAG, "Passed token to server!");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(LOG_TAG, "Failed passing token to server");
        VolleyErrorManager.logErrors(LOG_TAG, error);
    }

}
