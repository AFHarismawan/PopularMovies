package com.harismawan.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListMovies {

    @SerializedName("results")
    @Expose
    public ArrayList<Movie> movies = new ArrayList<>();
}
