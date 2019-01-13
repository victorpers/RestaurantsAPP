package com.victorpers.restaurantsapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.victorpers.restaurantsapp.api.RestaurantApi;
import com.victorpers.restaurantsapp.api.RestaurantService;
import com.victorpers.restaurantsapp.models.Result;
import com.victorpers.restaurantsapp.models.Restaurants;
import com.victorpers.restaurantsapp.utils.PaginationScrollListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    PaginationAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    RecyclerView rv;
    ProgressBar progressBar;
    private ImageView mapIcon;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 10;
    private int currentPage = PAGE_START;

    private RestaurantService restaurantService;

    public List<Result> lstRestaurant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lstRestaurant = new ArrayList<>();

        rv = findViewById(R.id.main_recycler);
        progressBar = findViewById(R.id.main_progress);
        mapIcon = findViewById(R.id.map_icon);

        adapter = new PaginationAdapter(this);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.setAdapter(adapter);

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        //init service and load data
        restaurantService = RestaurantApi.getClient().create(RestaurantService.class);

        mapIcon = (ImageView) findViewById(R.id.map_icon);
        mapIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMapsActivity();
            }
        });

        loadFirstPage();
    }

    //Route vers l'activité Map
    public void openMapsActivity() {
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }

    //Chargement de la première page des restaurant
    private void loadFirstPage() {
        callRestaurantsApi().enqueue(new Callback<Restaurants>() {
            @Override
            public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {

                List<Result> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }

            @Override
            public void onFailure(Call<Restaurants> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    //Chargement de toute les autres pages après la première
    private void loadNextPage() {
        callRestaurantsApi().enqueue(new Callback<Restaurants>() {
            @Override
            public void onResponse(Call<Restaurants> call, Response<Restaurants> response) {
                adapter.removeLoadingFooter();
                isLoading = false;

                List<Result> results = fetchResults(response);
                adapter.addAll(results);

                if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
                else isLastPage = true;
            }
            @Override
            public void onFailure(Call<Restaurants> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //Permet de récupérer la data des restaurants
    private List<Result> fetchResults(Response<Restaurants> response) {
        Restaurants restaurants = response.body();
        return restaurants.getRestaurants();
    }

    //Appel de l'API en lui passant les paramètres souhaités
    private Call<Restaurants> callRestaurantsApi() {
        return restaurantService.getAllRestaurants(
                "US",
                currentPage
        );
    }

}
