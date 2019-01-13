package com.victorpers.restaurantsapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.victorpers.restaurantsapp.models.Result;

import java.util.ArrayList;
import java.util.List;

public class PaginationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<Result> restaurantResults;
    private Context context;

    private boolean isLoadingAdded = false;

    public PaginationAdapter(Context context) {
        this.context = context;
        restaurantResults = new ArrayList<>();
    }

    public List<Result> getRestaurants() {
        return restaurantResults;
    }

    public void setRestaurants(List<Result> restaurantResults) {
        this.restaurantResults = restaurantResults;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.item_list, parent, false);
        viewHolder = new RestaurantVH(v1);
        return viewHolder;
    }

    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Result result = restaurantResults.get(position); // Restaurant

        switch (getItemViewType(position)) {
            case ITEM:
                final RestaurantVH restaurantVH = (RestaurantVH) holder;

                restaurantVH.mRestaurantName.setText(result.getName());

                restaurantVH.mCity.setText(result.getCity());

                restaurantVH.mPrice.setText(result.getPrice());
                restaurantVH.mAddress.setText(result.getAddress());
                restaurantVH.mPhone.setText(result.getPhone());
                restaurantVH.mSite.setText(result.getSite());

                int priceInt = Integer.parseInt(result.getPrice());

                restaurantVH.mPrice.getBackground().setColorFilter(Color.parseColor(getColor(priceInt)), PorterDuff.Mode.SRC_OVER);


                Glide
                        .with(context)
                        .load(result.getImg())
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                // TODO: 08/11/16 handle failure
                                restaurantVH.mProgress.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                // image ready, hide progress now
                                restaurantVH.mProgress.setVisibility(View.GONE);
                                return false;   // return false if you want Glide to handle everything else.
                            }
                        })
                        .diskCacheStrategy(DiskCacheStrategy.ALL)   // cache both original & resized image
                        .centerCrop()
                        .crossFade()
                        .into(restaurantVH.mImg);

                break;

            case LOADING:
                //Do nothing
                break;


        }

    }

    @Override
    public int getItemCount() {
        return restaurantResults == null ? 0 : restaurantResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == restaurantResults.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }


    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Result r) {
        restaurantResults.add(r);
        notifyItemInserted(restaurantResults.size() - 1);
    }

    public void addAll(List<Result> moveResults) {
        for (Result result : moveResults) {
            add(result);
        }
    }

    public void remove(Result r) {
        int position = restaurantResults.indexOf(r);
        if (position > -1) {
            restaurantResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Result());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = restaurantResults.size() - 1;
        Result result = getItem(position);

        if (result != null) {
            restaurantResults.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Result getItem(int position) {
        return restaurantResults.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class RestaurantVH extends RecyclerView.ViewHolder {
        private TextView mRestaurantName;
        private TextView mCity;
        private TextView mPrice;
        private TextView mAddress;
        private TextView mPhone;
        private TextView mSite;
        private ImageView mImg;
        private ProgressBar mProgress;

        public RestaurantVH(View itemView) {
            super(itemView);

            mRestaurantName = (TextView) itemView.findViewById(R.id.restaurant_name);
            mCity = (TextView) itemView.findViewById(R.id.restaurant_city);
            mPrice = (TextView) itemView.findViewById(R.id.restaurant_price);
            mAddress = (TextView) itemView.findViewById(R.id.restaurant_address);
            mPhone = (TextView) itemView.findViewById(R.id.restaurant_phone);
            mSite = (TextView) itemView.findViewById(R.id.restaurant_site);
            mImg = (ImageView) itemView.findViewById(R.id.restaurant_poster);
            mProgress = (ProgressBar) itemView.findViewById(R.id.restaurant_progress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context,DetailsActivity.class);
                    mImg.setDrawingCacheEnabled(true);
                    Bitmap b = mImg.getDrawingCache();
                    i.putExtra("mRestaurantName", mRestaurantName.getText());
                    i.putExtra("mRestaurantPrice", mPrice.getText());
                    i.putExtra("mRestaurantCity", mCity.getText());
                    i.putExtra("mRestaurantAddress", mAddress.getText());
                    i.putExtra("mRestaurantPhone", mPhone.getText());
                    i.putExtra("mRestaurantSite", mSite.getText());
                    i.putExtra("mImg", b);
                    context.startActivity(i);

                }
            });
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }

    public String getColor(int priceInt) {
        switch (priceInt) {
            case 1:
                return "#3498db";
            case 2:
                return "#27ae60";
            case 3:
                return "#e67e22";

                default:
                    return "#c0392b";
        }
    }

}