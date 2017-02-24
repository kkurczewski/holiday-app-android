package com.holidaysoffer.holidayofferapp.network_utils;

import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.holidaysoffer.holidayofferapp.R;

public class VolleyErrorManager {

    public static void logErrors(String logTag, VolleyError error) {
        if (error.getMessage() != null) Log.e(logTag, error.getMessage());
        else Log.e(logTag, "Error object has no message. Is server turned on?");
    }

    public static int getErrorMessageId(String logTag, VolleyError error) {
        VolleyErrorManager.logErrors(logTag, error);

        int errorMessage;
        if (error instanceof NoConnectionError) errorMessage = R.string.connection_error;
        else if (error instanceof NetworkError) errorMessage = R.string.network_error;
        else errorMessage = R.string.server_error;

        return errorMessage;
    }

}
