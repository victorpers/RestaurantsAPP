package com.victorpers.restaurantsapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Restaurants {

    @SerializedName("restaurants")
    @Expose
    private List<Result> restaurants = new ArrayList<Result>();


    public List<Result> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Result> restaurants) {
        this.restaurants = restaurants;
    }

}
