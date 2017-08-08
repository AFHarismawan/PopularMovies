package com.harismawan.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("author")
    @Expose
    public String author;

    @SerializedName("content")
    @Expose
    public String content;
}
