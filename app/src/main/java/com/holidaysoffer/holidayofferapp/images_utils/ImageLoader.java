package com.holidaysoffer.holidayofferapp.images_utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.holidaysoffer.holidayofferapp.images_utils.resize_img_builders.RequestModel;

public class ImageLoader {

    public static void loadImage(RequestModel requestModel, ImageView imageView, final ProgressBar progressBar, Context context) {
        Glide.with(context)
                .load(requestModel)
                .listener(new RequestListener<RequestModel, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, RequestModel model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, RequestModel model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(imageView);
    }

}
