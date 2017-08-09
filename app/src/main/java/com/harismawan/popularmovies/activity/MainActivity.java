package com.harismawan.popularmovies.activity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.adapter.MovieRecyclerAdapter;
import com.harismawan.popularmovies.config.Constants;
import com.harismawan.popularmovies.contentprovider.FavoriteProvider;
import com.harismawan.popularmovies.database.DatabaseHelper;
import com.harismawan.popularmovies.model.ListMovies;
import com.harismawan.popularmovies.model.Movie;
import com.harismawan.popularmovies.utils.APIHelper;
import com.harismawan.popularmovies.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.root) RelativeLayout root;
    @BindView(R.id.progress) ProgressBar progress;
    @BindView(R.id.recycler_grid) RecyclerView mRecyclerView;

    private APIHelper helper;
    private MovieRecyclerAdapter adapter;
    private String instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        helper = Utils.getAPIHelper();

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        adapter = new MovieRecyclerAdapter(null);
        mRecyclerView.setAdapter(adapter);

        if (Utils.isConnected(this)) {
            if (savedInstanceState == null) {
                instance = Constants.CATEGORY_POPULAR;
            } else {
                instance = savedInstanceState.getString(Constants.EXTRA_KEY_SAVED_INSTANCE, Constants.CATEGORY_POPULAR);
            }

            if (instance.equals(Constants.CATEGORY_FAVORITE)) {
                adapter.updateData(getFavorites());
                hideProgress();
            } else {
                loadData(instance);
            }
        } else {
            Snackbar.make(root, R.string.no_connection, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void loadData(String category) {
        showProgress();
        helper.getMovies(category, Constants.API_KEY).enqueue(new Callback<ListMovies>() {

            @Override
            public void onResponse(Call<ListMovies> call, Response<ListMovies> response) {
                Log.d("response", response.toString());
                adapter.updateData(response.body().movies);
                hideProgress();
            }

            @Override
            public void onFailure(Call<ListMovies> call, Throwable t) {
                hideProgress();
                Snackbar.make(root, R.string.fail_connect_api, Snackbar.LENGTH_INDEFINITE).show();
            }
        });
    }

    private ArrayList<Movie> getFavorites() {
        ArrayList<Movie> list = new ArrayList<>();
        Cursor c = getContentResolver().query(Uri.parse(FavoriteProvider.CONTENT_URI + "/favorite"),
                null, null, null, null);

        if (c.moveToFirst()) {
            do{
                Movie entry = new Movie();
                entry.id = c.getInt(c.getColumnIndex(DatabaseHelper.COLUMN_ID));
                entry.title = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_TITLE));
                entry.imageUrl = c.getString(c.getColumnIndex(DatabaseHelper.COLUMN_IMG_URL));
                list.add(entry);
            } while (c.moveToNext());
        }

        c.close();
        return list;
    }

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.EXTRA_KEY_SAVED_INSTANCE, instance);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_movies:
                instance = Constants.CATEGORY_POPULAR;
                loadData(instance);
                return true;
            case R.id.top_rated_movies:
                instance = Constants.CATEGORY_TOP_RATED;
                loadData(instance);
                return true;
            case R.id.favorite_movies:
                instance = Constants.CATEGORY_FAVORITE;
                adapter.updateData(getFavorites());
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
