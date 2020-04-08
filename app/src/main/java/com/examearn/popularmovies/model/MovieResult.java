package com.examearn.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResult {

    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

}
