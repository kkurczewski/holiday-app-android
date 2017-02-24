package com.holidaysoffer.holidayofferapp.network_utils;

import android.util.Log;

import com.holidaysoffer.holidayofferapp.Offer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JsonParser {

    public static final String OFFER_ID = "offerId";
    private static final String LOG_TAG = JsonParser.class.getSimpleName();

    public static List<Offer> getOffers(JSONArray jsonArray) {
        try {
            return fetchJsonOffers(jsonArray);
        } catch (JSONException e) {
            Log.e(LOG_TAG, "An exception occurred when parsing json from server: " + e);
        }
        return null;
    }

    public static Offer getOffer(JSONObject jsonObject) {
        try {
            return new Offer(
                    jsonObject.getString("dateStart"),
                    jsonObject.getInt("days"),
                    jsonObject.getString("personCount"),
                    jsonObject.getString("mealsPreference"),
                    jsonObject.getString("transportType"),
                    jsonObject.getString("climateType"),
                    jsonObject.getString("country"),
                    jsonObject.getString("city"),
                    BigDecimal.valueOf(jsonObject.getDouble("price")),
                    jsonObject.getString("descriptionUrl"),
                    fetchJsonGalleryLinks(jsonObject.getJSONArray("galleryUrls")),
                    jsonObject.getString(OFFER_ID),
                    jsonObject.getDouble("mapLatitude"),
                    jsonObject.getDouble("mapLongitude")
            );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Offer> fetchJsonOffers(JSONArray jsonArray) throws JSONException {

        int length = jsonArray.length();
        List<Offer> offers = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            offers.add(getOffer(jsonObject));
        }
        return offers;
    }

    private static ArrayList<String> fetchJsonGalleryLinks(JSONArray jsonArray) throws JSONException {

        int length = jsonArray.length();
        ArrayList<String> galleryLinks = new ArrayList<>(length);

        for (int i = 0; i < length; i++) {
            galleryLinks.add(jsonArray.getString(i));
        }
        return galleryLinks;
    }
}
