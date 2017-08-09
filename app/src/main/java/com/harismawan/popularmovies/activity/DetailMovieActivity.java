package com.harismawan.popularmovies.activity;

import android.content.ContentValues;
import android.net.Uri;
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
import butterknife.BindView;
import butterknife.ButterKnife;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.adapter.ReviewRecyclerAdapter;
import com.harismawan.popularmovies.adapter.VideoRecyclerAdapter;
import com.harismawan.popularmovies.config.Constants;
import com.harismawan.popularmovies.contentprovider.FavoriteProvider;
import com.harismawan.popularmovies.database.DatabaseHelper;
import com.harismawan.popularmovies.model.ListReviews;
import com.harismawan.popularmovies.model.ListVideos;
import com.harismawan.popularmovies.model.Movie;
import com.harismawan.popularmovies.utils.APIHelper;
import com.harismawan.popularmovies.utils.Utils;
import com.harismawan.popularmovies.viewholder.DialogAlertHolder;
import com.harismawan.popularmovies.viewholder.DialogHeaderHolder;
import com.harismawan.popularmovies.viewholder.DialogListHolder;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailMovieActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.root) RelativeLayout root;
    @BindView(R.id.card_view) CardView cardView;
    @BindView(R.id.progress) ProgressBar progress;
    @BindView(R.id.image) ImageView image;
    @BindView(R.id.movie_title) TextView movieTitle;
    @BindView(R.id.movie_year) TextView movieYear;
    @BindView(R.id.movie_duration) TextView movieDuration;
    @BindView(R.id.movie_rating) TextView movieRating;
    @BindView(R.id.movie_desc) TextView movieDesc;
    @BindView(R.id.button_fav) Button favorite;
    @BindView(R.id.button_videos) Button video;
    @BindView(R.id.movie_reviews) RecyclerView movieReviews;

    private APIHelper helper;
    private Movie movie;
    private ReviewRecyclerAdapter adapter;
    private AlertDialog dialog;
    private DatabaseHelper db;
    private DialogListHolder list;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_movies);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(R.string.app_name);
        }

        id = getIntent().getIntExtra(Constants.EXTRA_KEY_ID, 0);

        db = new DatabaseHelper(this);
        helper = Utils.getAPIHelper();

        cardView.setVisibility(View.GONE);

        int count = getContentResolver().query(Uri.parse(FavoriteProvider.CONTENT_URI + "/favorite/" + id),
                null, null, null, null).getCount();
        Log.d("count", count + "");
        if (count != 0) {
            favorite.setText(getString(R.string.button_remove_favorite));
            favorite.setBackgroundResource(R.drawable.button_background_red);

            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteFavorite();
                }
            });
        } else {
            favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setFavorite();
                }
            });
        }

        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadVideos(id);
            }
        });

        LinearLayoutManager mListLayoutManager = new LinearLayoutManager(this);
        movieReviews.setLayoutManager(mListLayoutManager);

        if (Utils.isConnected(this)) {
            loadData(id);
        } else {
            Snackbar.make(root, R.string.no_connection, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void setFavorite() {
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_ID, movie.id);
        values.put(DatabaseHelper.COLUMN_TITLE, movie.title);
        values.put(DatabaseHelper.COLUMN_IMG_URL, movie.imageUrl);

        getContentResolver().insert(Uri.parse(FavoriteProvider.CONTENT_URI + "/favorite"), values);

        showAlert(getString(R.string.added));

        favorite.setText(getString(R.string.button_remove_favorite));
        favorite.setBackgroundResource(R.drawable.button_background_red);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFavorite();
            }
        });
    }

    private void deleteFavorite() {
        getContentResolver().delete(Uri.parse(FavoriteProvider.CONTENT_URI + "/favorite/delete/" + id),
                null, null);

        showAlert(getString(R.string.removed));

        favorite.setText(getString(R.string.button_favorite));
        favorite.setBackgroundResource(R.drawable.button_background_green);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setFavorite();
            }
        });
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
                list.progressDialog.setVisibility(View.GONE);
                list.videos.setVisibility(View.VISIBLE);
                list.close.setVisibility(View.VISIBLE);

                list.videos.setAdapter(new VideoRecyclerAdapter(response.body().videos));
            }

            @Override
            public void onFailure(Call<ListVideos> call, Throwable t) {
                dialog.cancel();
                Snackbar.make(root, R.string.fail_connect_api, Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    private void showAlert(String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.dialog_header, null, false);

        DialogHeaderHolder header = new DialogHeaderHolder(v);
        header.title.setText(title);

        builder.setCustomTitle(v);

        View vv = getLayoutInflater().inflate(R.layout.dialog_alert, null, false);

        DialogAlertHolder content = new DialogAlertHolder(vv);
        if (title.equals(getString(R.string.added))) {
            content.image.setImageResource(R.mipmap.ic_check);
            content.text.setText(getString(R.string.save_success));
        } else {
            content.image.setImageResource(R.mipmap.ic_cross);
            content.text.setText(getString(R.string.delete_success));
        }

        content.ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        builder.setView(vv);

        dialog = builder.create();
        dialog.show();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View v = getLayoutInflater().inflate(R.layout.dialog_header, null, false);

        DialogHeaderHolder header = new DialogHeaderHolder(v);
        header.title.setText(R.string.button_videos);

        builder.setCustomTitle(v);

        View vv = getLayoutInflater().inflate(R.layout.dialog_list, null, false);

        list = new DialogListHolder(vv);
        LinearLayoutManager mListLayoutManager = new LinearLayoutManager(this);
        list.videos.setLayoutManager(mListLayoutManager);
        list.videos.setVisibility(View.GONE);

        list.close.setVisibility(View.GONE);
        list.close.setOnClickListener(new View.OnClickListener() {
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
