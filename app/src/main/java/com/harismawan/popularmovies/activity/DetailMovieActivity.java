package com.harismawan.popularmovies.activity;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.adapter.ReviewRecyclerAdapter;
import com.harismawan.popularmovies.adapter.VideoRecyclerAdapter;
import com.harismawan.popularmovies.config.Constants;
import com.harismawan.popularmovies.database.DatabaseHelper;
import com.harismawan.popularmovies.model.ListReviews;
import com.harismawan.popularmovies.model.ListVideos;
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
    private CardView cardView;
    private ProgressBar progress, progressDialog;
    private ImageView image;
    private TextView movieTitle, movieYear, movieDuration, movieRating, movieDesc;
    private ReviewRecyclerAdapter adapter;
    private RecyclerView videos;
    private AlertDialog dialog;
    private Button close;
    private RecyclerView movieReviews;
    private DatabaseHelper db;

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

        final int id = getIntent().getIntExtra(Constants.EXTRA_KEY_ID, 0);

        db = new DatabaseHelper(this);
        helper = Utils.getAPIHelper();

        root = (RelativeLayout) findViewById(R.id.root);
        cardView = (CardView) findViewById(R.id.card_view);
        cardView.setVisibility(View.GONE);
        progress = (ProgressBar) findViewById(R.id.progress);

        image = (ImageView) findViewById(R.id.image);
        movieTitle = (TextView) findViewById(R.id.movie_title);
        movieYear = (TextView) findViewById(R.id.movie_year);
        movieDuration = (TextView) findViewById(R.id.movie_duration);
        movieRating = (TextView) findViewById(R.id.movie_rating);
        movieDesc = (TextView) findViewById(R.id.movie_desc);

        Button favorite = (Button) findViewById(R.id.button_fav);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.insertFavorite(movie);
                showAlert();
            }
        });

        Button video = (Button) findViewById(R.id.button_videos);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadVideos(id);
            }
        });

        movieReviews = (RecyclerView) findViewById(R.id.movie_reviews);
        LinearLayoutManager mListLayoutManager = new LinearLayoutManager(this);
        movieReviews.setLayoutManager(mListLayoutManager);

        if (Utils.isConnected(this)) {
            loadData(id);
        } else {
            Snackbar.make(root, R.string.no_connection, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void initView() {
        Picasso.with(this).load(Constants.IMAGE_BASE_URL + this.movie.imageUrl)
                .placeholder(R.mipmap.ic_placeholder).error(R.mipmap.ic_error_placeholder).into(image);
        movieTitle.setText(movie.title);
        movieYear.setText(movie.releaseDate.split("-")[0]);
        movieDuration.setText(movie.runtime + " min");
        movieRating.setText(movie.voteAverage + "/10");
        movieDesc.setText(movie.desc);

        hideProgress();
    }

    private void loadData(final int id) {
        showProgress();

        helper.getMovieDetail(id, Constants.API_KEY).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                Log.d("response", response.toString());
                movie = response.body();

                helper.getMovieReviews(id, Constants.API_KEY).enqueue(new Callback<ListReviews>() {
                    @Override
                    public void onResponse(Call<ListReviews> call, Response<ListReviews> response) {
                        Log.d("response", response.toString());
                        adapter = new ReviewRecyclerAdapter(response.body().reviews);
                        movieReviews.setAdapter(adapter);
                        initView();
                    }

                    @Override
                    public void onFailure(Call<ListReviews> call, Throwable t) {
                        hideProgress();
                        Snackbar.make(root, R.string.fail_connect_api, Snackbar.LENGTH_INDEFINITE).show();
                    }
                });
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                hideProgress();
                Snackbar.make(root, R.string.fail_connect_api, Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    private void loadVideos(int id) {
        showDialog();
        helper.getMovieVideos(id, Constants.API_KEY).enqueue(new Callback<ListVideos>() {
            @Override
            public void onResponse(Call<ListVideos> call, Response<ListVideos> response) {
                progressDialog.setVisibility(View.GONE);
                videos.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);

                videos.setAdapter(new VideoRecyclerAdapter(response.body().videos));
            }

            @Override
            public void onFailure(Call<ListVideos> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(root, R.string.fail_connect_api, Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.dialog_header, null, false);
        TextView title = v.findViewById(R.id.title);
        title.setText(R.string.success);
        builder.setCustomTitle(v);

        View vv = getLayoutInflater().inflate(R.layout.dialog_alert, null, false);
        builder.setView(vv);

        Button ok = vv.findViewById(R.id.button_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.dialog_header, null, false);
        TextView title = v.findViewById(R.id.title);
        title.setText(R.string.button_videos);
        builder.setCustomTitle(v);

        View vv = getLayoutInflater().inflate(R.layout.dialog_list, null, false);

        progressDialog = vv.findViewById(R.id.progress_dialog);

        videos = vv.findViewById(R.id.movie_videos);
        LinearLayoutManager mListLayoutManager = new LinearLayoutManager(this);
        videos.setLayoutManager(mListLayoutManager);
        videos.setVisibility(View.GONE);

        close = vv.findViewById(R.id.button_close);
        close.setVisibility(View.GONE);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        builder.setView(vv);

        dialog = builder.create();
        dialog.show();
    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progress.setVisibility(View.GONE);
        cardView.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
