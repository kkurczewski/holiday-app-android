package com.holidaysoffer.holidayofferapp.images_utils.resize_img_builders;

import android.content.Context;

import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;

import java.io.InputStream;

class UrlLoader extends BaseGlideUrlLoader<RequestModel> {

    public UrlLoader(Context context) {
        super(context);
    }

    @Override
    protected String getUrl(RequestModel model, int width, int height) {
        return model.buildUrl(width, height);
    }

    static class Factory implements ModelLoaderFactory<RequestModel, InputStream> {

        @Override
        public ModelLoader<RequestModel, InputStream> build(Context context, GenericLoaderFactory factories) {
            return new CustomDataModel(context);
        }

        @Override
        public void teardown() {
            // not needed
        }
    }
}
