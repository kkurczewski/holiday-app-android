package com.holidaysoffer.holidayofferapp.images_utils;

import com.holidaysoffer.holidayofferapp.images_utils.resize_img_builders.RequestModel;

public class CustomImageSizeRequestModel implements RequestModel {

    private final String baseUrl;

    public CustomImageSizeRequestModel(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @Override
    public String buildUrl(int width, int height) {
        return baseUrl + "?w=" + width + "&h=" + height;
    }

}
