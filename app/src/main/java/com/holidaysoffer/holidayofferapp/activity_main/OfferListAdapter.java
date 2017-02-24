package com.holidaysoffer.holidayofferapp.activity_main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.holidaysoffer.holidayofferapp.Offer;
import com.holidaysoffer.holidayofferapp.R;
import com.holidaysoffer.holidayofferapp.images_utils.CustomImageSizeRequestModel;
import com.holidaysoffer.holidayofferapp.images_utils.ImageLoader;
import com.holidaysoffer.holidayofferapp.images_utils.resize_img_builders.RequestModel;
import com.holidaysoffer.holidayofferapp.activity_main.fragments.ClickCallback;
import com.holidaysoffer.holidayofferapp.network_utils.UrlManager;

import java.util.List;

public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.MyViewHolder> {

    private static ClickCallback clickCallback;

    private final Context context;
    private final List<Offer> offers;

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        Offer offer;
        ImageView imageView;
        TextView countryCityLabel;
        ProgressBar progressBar;
        BasicInfoViewHolder basicInfoViewHolder;

        MyViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.image);
            countryCityLabel = (TextView) view.findViewById(R.id.country_city_label);
            progressBar = (ProgressBar) view.findViewById(R.id.item_progress_bar);
            basicInfoViewHolder = new BasicInfoViewHolder(view);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickCallback.onClick(offer);
        }
    }

    public OfferListAdapter(List<Offer> offers, Context context, ClickCallback clickCallback) {
        this.context = context;
        this.offers = offers;
        OfferListAdapter.clickCallback = clickCallback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.offer = offers.get(position);
        holder.countryCityLabel.setText(holder.offer.getCountry() + " / " + holder.offer.getCity());
        holder.basicInfoViewHolder.bind(holder.offer, context);

        String imageUrl = UrlManager.getImageUrl(offers.get(position).getGalleryUrls().get(0));
        RequestModel requestModel = new CustomImageSizeRequestModel(imageUrl);

        holder.progressBar.setVisibility(View.VISIBLE);
        ImageLoader.loadImage(requestModel, holder.imageView, holder.progressBar, context);
    }

    @Override
    public void onViewRecycled(MyViewHolder holder) {
        super.onViewRecycled(holder);
        holder.imageView.setImageDrawable(null);
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

}

