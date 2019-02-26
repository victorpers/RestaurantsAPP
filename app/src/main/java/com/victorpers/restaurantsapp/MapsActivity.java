package com.victorpers.restaurantsapp;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.victorpers.restaurantsapp.api.RestaurantApi;
import com.victorpers.restaurantsapp.models.Restaurants;
import com.victorpers.restaurantsapp.models.Result;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        for (int i = 0; i < 25; i++) {
            LatLng restaurantSpot = new LatLng(PaginationAdapter.getRestaurants().get(i).getLat(), PaginationAdapter.getRestaurants().get(i).getLng());
            mMap.addMarker(new MarkerOptions().position(restaurantSpot).title(PaginationAdapter.getRestaurants().get(i).getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantSpot, 8));
        }

        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker arg0) {
                Intent i = new Intent(context, DetailsActivity.class);
                context.startActivity(i);
            }
        });
    }
}