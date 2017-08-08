package com.harismawan.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import com.harismawan.popularmovies.activity.DetailMovieActivity;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.config.Constants;
import com.harismawan.popularmovies.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private ImageView image;
    private Movie movie;

    public MovieHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.image);
        image.setOnClickListener(this);
    }

    public void bind(Movie movie) {
        this.movie = movie;
        Picasso.with(image.getContext()).load(Constants.IMAGE_BASE_URL + this.movie.imageUrl)
                .placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_error_placeholder).into(image);
    }

    @Override
    public void onClick(View view) {
        Context context = view.getContext();
        Intent change = new Intent(context, DetailMovieActivity.class);
        change.putExtra(Constants.EXTRA_KEY_ID, this.movie.id);
        context.startActivity(change);
    }
}
