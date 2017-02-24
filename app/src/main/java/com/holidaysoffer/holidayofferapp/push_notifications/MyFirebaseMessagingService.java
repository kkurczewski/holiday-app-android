package com.holidaysoffer.holidayofferapp.push_notifications;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.holidaysoffer.holidayofferapp.Offer;
import com.holidaysoffer.holidayofferapp.R;
import com.holidaysoffer.holidayofferapp.activity_details.DetailsActivity;
import com.holidaysoffer.holidayofferapp.network_utils.JsonParser;
import com.holidaysoffer.holidayofferapp.network_utils.UrlManager;
import com.holidaysoffer.holidayofferapp.network_utils.VolleyErrorManager;
import com.holidaysoffer.holidayofferapp.network_utils.VolleySingleton;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static final int NOTIFICATION_OFFER_REQUEST = 1;
    public static final int NOTIFICATION_ID = 1;
    private static final String LOG_TAG = "firebase_service";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(LOG_TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(LOG_TAG, "Message data payload: " + remoteMessage.getData());

            sendRequestForOffer(remoteMessage.getData().get(JsonParser.OFFER_ID));
        }
    }

    private void sendRequestForOffer(String offerId) {
        String offerUrl = UrlManager.getOfferUrl(offerId);
        JsonRequest request = new JsonObjectRequest(offerUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Offer offer = JsonParser.getOffer(response);
                sendNotification(offer);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyErrorManager.logErrors(LOG_TAG, error);
            }
        });
        VolleySingleton.getInstance(this).addToRequestQueue(request);
    }

    private void sendNotification(Offer offer) {

        Intent intent = new Intent(this, DetailsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        intent.putExtra(DetailsActivity.NOTIFICATION_OFFER, offer);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, NOTIFICATION_OFFER_REQUEST, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_notify)
                .setContentTitle(getString(R.string.title_notification))
                .setContentText(offer.getCountry() + "/" + offer.getCity())
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        android.app.NotificationManager notificationManager =
                (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }

}
