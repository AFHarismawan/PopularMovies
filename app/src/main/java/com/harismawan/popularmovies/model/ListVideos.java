package com.harismawan.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListVideos {

    @SerializedName("results")
    @Expose
    public ArrayList<Video> videos = new ArrayList<>();
}
