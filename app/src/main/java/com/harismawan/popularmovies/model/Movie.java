package com.harismawan.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("title")
    @Expose
    public String title;

    @SerializedName("overview")
    @Expose
    public String desc;

    @SerializedName("poster_path")
    @Expose
    public String imageUrl;

    @SerializedName("vote_average")
    @Expose
    public float voteAverage;

    @SerializedName("release_date")
    @Expose
    public String releaseDate;

    @SerializedName("runtime")
    @Expose
    public int runtime;
}
