package com.holidaysoffer.holidayofferapp.network_utils;

import com.holidaysoffer.holidayofferapp.activity_search.SearchParameters;

public class UrlManager {

    public static final String URL_LOG = "NetworkConnection";
    private static String HOST_URL = "http://server-address.com";

    public static void _debug_setUrl(String hostUrl) {
        UrlManager.HOST_URL = hostUrl;
    }

    public static String getHostUrl() {
        return UrlManager.HOST_URL;
    }

    public static String getJsonForOffersUrl(int userId, SearchParameters searchParameters) {
        return HOST_URL + "offers?user=" + userId + getSearchQuery(searchParameters);
    }

    public static String getJsonForUserFavoritesUrl(int userId) {
        return HOST_URL + "users/favorites?user=" + userId;
    }

    public static String getUserRegisterUrl() {
        return HOST_URL + "users/register";
    }

    public static String getImageUrl(String imagePath) {
        return HOST_URL + imagePath;
    }

    public static String getDescriptionUrl(String descriptionUrl) {
        return HOST_URL + descriptionUrl;
    }

    public static String getTokenRegisterUrl() {
        return HOST_URL + "notification/register";
    }

    public static String getFavoritesUrl(int userId, String offerId, boolean newFavoriteState) {
        String action = newFavoriteState ? "add" : "remove";
        return UrlManager.HOST_URL + "users/favorites/" + action + "?user=" + userId + "&id=" + offerId;
    }

    public static String getOfferUrl(String offerId) {
        return HOST_URL + "offers/" + offerId;
    }

    private static String getSearchQuery(SearchParameters parameters) {
        String queryParameters = "";
        if (parameters != null) {
            queryParameters = "&date=" + parameters.getDate() +
                    "&price=" + parameters.getPrice() +
                    "&persons=" + parameters.getPersons() +
                    "&meals=" + parameters.getMealPreference() +
                    "&climate=" + parameters.getClimateFlag();
        }
        return queryParameters;
    }
}
