package com.holidaysoffer.holidayofferapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.LinearLayout;

import com.google.android.gms.maps.model.LatLng;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Offer implements Parcelable {

    final private String offerId;
    final private String dateStart;
    final private int days;
    final private String personCount;
    final private String mealsPreference;
    final private String transportType;
    final private String climateType;
    final private String country;
    final private String city;
    final private BigDecimal price;
    final private String descriptionUrl;
    final private ArrayList<String> galleryUrls = new ArrayList<>();
    final private LatLng mapMarker;

    private boolean isFavorite = false;

    public Offer(String dateStart, int days, String personCount, String mealsPreference,
                 String transportType, String climateType, String country, String city, BigDecimal price,
                 String descriptionUrl, ArrayList<String> galleryUrls, String offerId, double mapLatitude, double mapLongitude) {

        this.offerId = offerId;
        this.dateStart = dateStart;
        this.days = days;
        this.personCount = personCount;
        this.mealsPreference = mealsPreference;
        this.transportType = transportType;
        this.climateType = climateType;
        this.country = country;
        this.city = city;
        this.price = price;
        this.descriptionUrl = descriptionUrl;
        this.galleryUrls.addAll(galleryUrls);
        mapMarker = hasMapLocation(mapLatitude, mapLongitude) ? (new LatLng(mapLatitude, mapLongitude)) : null;
    }

    public Offer(Parcel parcel) {
        offerId = parcel.readString();
        dateStart = parcel.readString();
        days = parcel.readInt();
        personCount = parcel.readString();
        mealsPreference = parcel.readString();
        transportType = parcel.readString();
        climateType = parcel.readString();
        country = parcel.readString();
        city = parcel.readString();
        price = (BigDecimal) parcel.readValue(BigDecimal.class.getClassLoader());
        descriptionUrl = parcel.readString();
        mapMarker = parcel.readParcelable(LatLng.class.getClassLoader());

        parcel.readStringList(galleryUrls);
        isFavorite = readBooleanFromParcel(parcel);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(offerId);
        parcel.writeString(dateStart);
        parcel.writeInt(days);
        parcel.writeString(personCount);
        parcel.writeString(mealsPreference);
        parcel.writeString(transportType);
        parcel.writeString(climateType);
        parcel.writeString(country);
        parcel.writeString(city);
        parcel.writeValue(price);
        parcel.writeString(descriptionUrl);
        parcel.writeParcelable(mapMarker, flags);

        parcel.writeStringList(galleryUrls);
        writeBooleanToParcel(parcel);
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getOfferId() {
        return offerId;
    }

    public String getDateStart() {
        return dateStart;
    }

    public int getDays() {
        return days;
    }

    public String getPersonCount() {
        return personCount;
    }

    public String getMealsPreference() {
        return mealsPreference;
    }

    public String getTransportType() {
        return transportType;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getDescriptionUrl() {
        return descriptionUrl;
    }

    public List<String> getGalleryUrls() {
        return galleryUrls;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public LatLng getMapMarker() {
        return mapMarker;
    }

    private boolean hasMapLocation(double mapLatitude, double mapLongitude) {
        return mapLatitude != 0 && mapLongitude != 0;
    }

    public static final Parcelable.Creator<Offer> CREATOR = new Parcelable.Creator<Offer>() {

        @Override
        public Offer createFromParcel(Parcel parcel) {
            return new Offer(parcel);
        }

        @Override
        public Offer[] newArray(int size) {
            return new Offer[size];
        }
    };

    private void writeBooleanToParcel(Parcel parcel) {
        parcel.writeByte((byte) (isFavorite ? 1 : 0));
    }

    private boolean readBooleanFromParcel(Parcel parcel) {
        return isFavorite = (parcel.readByte() == 1);
    }
}
