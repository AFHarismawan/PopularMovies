package com.harismawan.popularmovies.utils;

import com.harismawan.popularmovies.model.ListMovies;
import com.harismawan.popularmovies.model.Movie;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIHelper {

    @GET("movie/{cat}")
    Call<ListMovies> getMovies(@Path("cat") String category, @Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<Movie> getMovieDetail(@Path("id") int id, @Query("api_key") String apiKey);
}