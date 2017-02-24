package com.holidaysoffer.holidayofferapp.network_utils;

import com.android.volley.Response;

public interface NetworkResponseListener<T> extends Response.Listener<T>, Response.ErrorListener {
    // wrapper for volley listeners
}
