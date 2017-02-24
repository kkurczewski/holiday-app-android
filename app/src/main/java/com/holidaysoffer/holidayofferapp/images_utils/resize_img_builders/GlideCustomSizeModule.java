package com.holidaysoffer.holidayofferapp.images_utils.resize_img_builders;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

public class GlideCustomSizeModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // not needed
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(RequestModel.class, InputStream.class, new UrlLoader.Factory());
    }
}