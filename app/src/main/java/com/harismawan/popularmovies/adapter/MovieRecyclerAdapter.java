package com.harismawan.popularmovies.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.model.Movie;
import com.harismawan.popularmovies.viewholder.MovieHolder;

import java.util.ArrayList;

public class MovieRecyclerAdapter extends RecyclerView.Adapter<MovieHolder> {

    private ArrayList<Movie> movies;

    public MovieRecyclerAdapter(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflatedView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_movies, parent, false);
        return new MovieHolder(inflatedView);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.bind(movie);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public void updateData(ArrayList<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
