package com.holidaysoffer.holidayofferapp.activity_details.gallery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.holidaysoffer.holidayofferapp.R;
import com.holidaysoffer.holidayofferapp.images_utils.CustomImageSizeRequestModel;
import com.holidaysoffer.holidayofferapp.images_utils.ImageLoader;
import com.holidaysoffer.holidayofferapp.images_utils.resize_img_builders.RequestModel;
import com.holidaysoffer.holidayofferapp.network_utils.UrlManager;

public class GalleryFragment extends Fragment {

    public static final String IMAGE_URL = "image_url_key";

    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_details_gallery, container, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.fragment_image);
        progressBar = (ProgressBar) view.findViewById(R.id.item_progress_bar);

        String imageURL = UrlManager.getImageUrl(getArguments().getString(IMAGE_URL));
        RequestModel requestModel = new CustomImageSizeRequestModel(imageURL);

        ImageLoader.loadImage(requestModel, imageView, progressBar, getContext());

        return view;
    }

}
