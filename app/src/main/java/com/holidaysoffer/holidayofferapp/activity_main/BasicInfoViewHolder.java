package com.holidaysoffer.holidayofferapp.activity_main;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.holidaysoffer.holidayofferapp.Offer;
import com.holidaysoffer.holidayofferapp.R;

public class BasicInfoViewHolder {

    private TextView priceLabel;
    private TextView dateStartLabel;
    private TextView personsLabel;
    private TextView mealsLabel;
    private TextView transportLabel;
    private ImageView transportIcon;

    public BasicInfoViewHolder(View view) {
        priceLabel = (TextView) view.findViewById(R.id.price_label);
        dateStartLabel = (TextView) view.findViewById(R.id.date_label);
        personsLabel = (TextView) view.findViewById(R.id.person_label);
        mealsLabel = (TextView) view.findViewById(R.id.meals_label);
        transportLabel = (TextView) view.findViewById(R.id.transport_label);
        transportIcon = (ImageView) view.findViewById(R.id.transport_icon);
    }

    public void bind(Offer offer, Context context) {

        priceLabel.setText(String.format("%s zł", String.valueOf(offer.getPrice())));
        personsLabel.setText(offer.getPersonCount());
        mealsLabel.setText(offer.getMealsPreference());
        dateStartLabel.setText(context.getResources().getQuantityString(
                R.plurals.date_and_days,
                offer.getDays(), // argument only specifies correct plural form
                offer.getDateStart(),
                offer.getDays()));
        setTransportIconAndLabel(transportLabel, transportIcon, offer.getTransportType(), context);
    }

    private void setTransportIconAndLabel(TextView transportLabel, ImageView transportIcon, String transportType, Context context) {
        transportLabel.setText(transportType);

        if(transportType.equals(context.getString(R.string.airplane))) {
            transportIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_transport_airplane)
            );
        } else if(transportType.equals(context.getString(R.string.bus)))  {
            transportIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_transport_bus)
            );
        } else if(transportType.equals(context.getString(R.string.ship)))  {
            transportIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_transport_ship)
            );
        } else if(transportType.equals(context.getString(R.string.train)))  {
            transportIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_transport_train)
            );
        } else if(transportType.equals(context.getString(R.string.other)))  {
            transportIcon.setImageDrawable(
                    ContextCompat.getDrawable(context, R.drawable.ic_transport_other)
            );
        } else {
            throw new RuntimeException("Unknown transport type. Is all transport options registered?");
        }
    }

}
