package com.examearn.popularmovies.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrailerResult {

    private String id;

    @SerializedName("results")
    private List<Trailer> results;

    public String getId() {
        return id;
    }

    public List<Trailer> getResults() {
        return results;
    }
}
