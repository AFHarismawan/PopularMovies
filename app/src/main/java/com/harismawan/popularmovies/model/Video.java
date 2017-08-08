package com.harismawan.popularmovies.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("key")
    @Expose
    public String key;
}
