package com.victorpers.restaurantsapp;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.victorpers.restaurantsapp.models.Result;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    TextView name;
    TextView price;
    TextView city;
    TextView address;
    TextView phone;
    ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initView();
        getDetailsData();
    }

    //Récupère la data du restaurant sélectionné
    public Result getData() {
        return (Result) getIntent().getSerializableExtra("restaurant");
    }

    //Initialisation de nos vues
    public void initView(){
        name =(TextView) findViewById(R.id.nameTxt);
        price =(TextView) findViewById(R.id.price_detail);
        city =(TextView) findViewById(R.id.city_detail);
        address =(TextView) findViewById(R.id.address_detail);
        phone =(TextView) findViewById(R.id.phone_detail);
        img =(ImageView) findViewById(R.id.imgdetail);
    }

    public void getDetailsData(){

        PaginationAdapter.class.getName();

        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        String nameData = getData().getName();
        String priceData = getData().getPrice();
        String cityData = getData().getCity();
        String adressData = getData().getAddress();
        String phoneData = getData().getPhone();


        name.setText(nameData);
        collapsingToolbarLayout.setTitle(nameData);
        price.setText(priceData);
        city.setText(cityData);
        address.setText(adressData);
        phone.setText(phoneData);
        Glide
                .with(this)
                .load(getData().getImg())
                .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                .centerCrop()
                .crossFade()
                .into(img);

        colorPriceButton();

    }

    //Modification de la couleur bulle prix en fonction du prix restaurant
    public void colorPriceButton() {
        int priceInt = Integer.parseInt(getData().getPrice());
        if (priceInt == 1) {
            price.getBackground().setColorFilter(Color.parseColor("#3498db"), PorterDuff.Mode.SRC_OVER);
        }
        else if (priceInt == 2) {
            price.getBackground().setColorFilter(Color.parseColor("#27ae60"), PorterDuff.Mode.SRC_OVER);
        }
        else if (priceInt == 3) {
            price.getBackground().setColorFilter(Color.parseColor("#e67e22"), PorterDuff.Mode.SRC_OVER);
        }
        else {
            price.getBackground().setColorFilter(Color.parseColor("#c0392b"), PorterDuff.Mode.SRC_OVER);
        }
    }

    //Ouvrir le site du restaurant
    public void openRestaurantWebsite(View view) {
        String linkWebsite = getData().getSite();
        Intent browerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkWebsite));
        startActivity(browerIntent);
    }

    //Affichage des markers sur la map
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        double lat = getData().getLat();
        double lng = getData().getLng();
        String nameMarker = getData().getName();

        LatLng restaurantSpot = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(restaurantSpot).title(nameMarker));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantSpot, 15));
    }
}