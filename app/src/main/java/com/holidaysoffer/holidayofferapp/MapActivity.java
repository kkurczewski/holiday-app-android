package com.holidaysoffer.holidayofferapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String MAP_MARKER = "map_marker";
    private static final int BOUNDS_SIZE = 30;

    private LatLng marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        getSupportActionBar();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        marker = getIntent().getParcelableExtra(MAP_MARKER);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions().position(marker));
        googleMap.getUiSettings().setRotateGesturesEnabled(false);

        LatLngBounds markerBounds = new LatLngBounds(
                new LatLng(marker.latitude - BOUNDS_SIZE, marker.longitude - BOUNDS_SIZE),
                new LatLng(marker.latitude + BOUNDS_SIZE, marker.longitude + BOUNDS_SIZE)
        );

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerBounds.getCenter(), 15));
    }
}
