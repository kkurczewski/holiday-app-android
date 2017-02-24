package com.holidaysoffer.holidayofferapp.images_utils.resize_img_builders;

import android.content.Context;

import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

class CustomDataModel extends BaseGlideUrlLoader<RequestModel> {

    CustomDataModel(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(RequestModel model, int width, int height) {
        return model.buildUrl(width, height);
    }
}
