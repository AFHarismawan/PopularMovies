package com.harismawan.popularmovies.activity;

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
import com.harismawan.popularmovies.R;
import com.harismawan.popularmovies.adapter.RecyclerAdapter;
import com.harismawan.popularmovies.config.Constants;
import com.harismawan.popularmovies.model.ListMovies;
import com.harismawan.popularmovies.model.Movie;
import com.harismawan.popularmovies.utils.APIHelper;
import com.harismawan.popularmovies.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private APIHelper helper;

    private ArrayList<Movie> movies = new ArrayList<>();
    private RelativeLayout root;
    private ProgressBar progress;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mGridLayoutManager;
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle(R.string.app_name);

        helper = Utils.getAPIHelper();

        root = (RelativeLayout) findViewById(R.id.root);
        progress = (ProgressBar) findViewById(R.id.progress);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_grid);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mGridLayoutManager);

        adapter = new RecyclerAdapter(movies);
        mRecyclerView.setAdapter(adapter);

        if (Utils.isConnected(this)) {
            loadData(Constants.CATEGORY_POPULAR);
        } else {
            Snackbar.make(root, R.string.no_connection, Snackbar.LENGTH_INDEFINITE).show();
        }
    }

    private void loadData(String category) {
        showProgress();
        helper.getMovies(category, getString(R.string.api_key)).enqueue(new Callback<ListMovies>() {

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

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    private void hideProgress() {
        progress.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
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
                loadData(Constants.CATEGORY_POPULAR);
                return true;
            case R.id.top_rated_movies:
                loadData(Constants.CATEGORY_TOP_RATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
