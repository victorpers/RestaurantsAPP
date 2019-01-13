package com.victorpers.restaurantsapp.api;

import com.victorpers.restaurantsapp.models.Restaurants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestaurantService {

    @GET("restaurants")
    Call<Restaurants> getAllRestaurants(
            @Query("country") String country,
            @Query("page") int pageIndex
    );
}
