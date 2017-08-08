package com.harismawan.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.model.Review;

import java.util.ArrayList;

public class ReviewRecyclerAdapter extends RecyclerView.Adapter<ReviewHolder> {

    private ArrayList<Review> reviews;

    public ReviewRecyclerAdapter(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_reviews, parent, false);
        return new ReviewHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(ReviewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.bind(review);
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}
