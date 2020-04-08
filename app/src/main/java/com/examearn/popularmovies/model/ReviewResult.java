package com.examearn.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResult {

    private String id;

    @SerializedName("results")
    private List<Review> results;

    public String getId() {
        return id;
    }

    public List<Review> getResults() {
        return results;
    }
}
