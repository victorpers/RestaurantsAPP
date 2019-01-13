package com.victorpers.restaurantsapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

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
        initView();
        getDetailsData();
    }

    public void initView(){
        name =(TextView) findViewById(R.id.nameTxt);
        price =(TextView) findViewById(R.id.price_detail);
        city =(TextView) findViewById(R.id.city_detail);
        address =(TextView) findViewById(R.id.address_detail);
        phone =(TextView) findViewById(R.id.phone_detail);
        img =(ImageView) findViewById(R.id.imgdetail);
    }

    public void getDetailsData(){

        Bundle extras = getIntent().getExtras();
        String mTitle = null, mPrice = null, mCity = null, mAddress = null, mPhone = null;


        if (extras != null) {
            mTitle = extras.getString("mRestaurantName");
            mPrice = extras.getString("mRestaurantPrice");
            mCity = extras.getString("mRestaurantCity");
            mAddress = extras.getString("mRestaurantAddress");
            mPhone = extras.getString("mRestaurantPhone");
        }

        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("mImg");
        CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsingtoolbar_id);
        collapsingToolbarLayout.setTitleEnabled(true);

        int priceInt = Integer.parseInt(mPrice);

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



        name.setText(mTitle.toString());
        collapsingToolbarLayout.setTitle(mTitle);
        price.setText(mPrice.toString());
        city.setText(mCity.toString());
        address.setText(mAddress.toString());
        phone.setText(mPhone.toString());
        img.setImageBitmap(bitmap);

    }

    public void browser1(View view) {
        String lienSite = getIntent().getExtras().getString("mRestaurantSite");
        Intent browerIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(lienSite));
        startActivity(browerIntent);
    }
}