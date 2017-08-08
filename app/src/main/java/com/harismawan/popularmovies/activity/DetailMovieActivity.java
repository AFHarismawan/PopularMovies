package com.harismawan.popularmovies.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.config.Constants;
import com.harismawan.popularmovies.model.Movie;
import com.harismawan.popularmovies.utils.APIHelper;
import com.harismawan.popularmovies.utils.Utils;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMovieActivity extends AppCompatActivity {

    private APIHelper helper;

    private Movie movie;
    private RelativeLayout root;
    private ProgressBar progress;
    private ImageView image;
    private TextView movieTitle, movieYear, movieDuration, movieRating, movieDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movies);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(R.string.app_name);
        }

        int id = getIntent().getIntExtra(Constants.EXTRA_KEY_ID, 0);

        helper = Utils.getAPIHelper();

        root = (RelativeLayout) findViewById(R.id.root);
        progress = (ProgressBar) findViewById(R.id.progress);

        image = (ImageView) findViewById(R.id.image);
        movieTitle = (TextView) findViewById(R.id.movie_title);
        movieYear = (TextView) findViewById(R.id.movie_year);
        movieDuration = (TextView) findViewById(R.id.movie_duration);
        movieRating = (TextView) findViewById(R.id.movie_rating);
        movieDesc = (TextView) findViewById(R.id.movie_desc);

        if (Utils.isConnected(this)) {
            loadData(id);
        } else {
            Snackbar.make(root, R.string.no_connection, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void initView() {
        Picasso.with(this).load(Constants.IMAGE_BASE_URL + this.movie.imageUrl).into(image);
        movieTitle.setText(movie.title);
        movieYear.setText(movie.releaseDate.split("-")[0]);
        movieDuration.setText(movie.runtime + " min");
        movieRating.setText(movie.voteAverage + "/10");
        movieDesc.setText(movie.desc);

        hideProgress();
    }

    private void loadData(int id) {
        showProgress();
        helper.getMovieDetail(id, Constants.API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Log.d("response", response.toString());
                movie = response.body();
                initView();
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                hideProgress();
                Snackbar.make(root, R.string.fail_connect_api, Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        image.setVisibility(View.GONE);
        movieTitle.setVisibility(View.GONE);
        movieYear.setVisibility(View.GONE);
        movieDuration.setVisibility(View.GONE);
        movieRating.setVisibility(View.GONE);
        movieDesc.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progress.setVisibility(View.GONE);
        image.setVisibility(View.VISIBLE);
        movieTitle.setVisibility(View.VISIBLE);
        movieYear.setVisibility(View.VISIBLE);
        movieDuration.setVisibility(View.VISIBLE);
        movieRating.setVisibility(View.VISIBLE);
        movieDesc.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
