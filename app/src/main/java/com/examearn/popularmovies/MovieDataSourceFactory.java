package com.examearn.popularmovies;


import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.examearn.popularmovies.model.Movie;

public class MovieDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Movie>> movieLiveDataSource;
    private String category;

    public MovieDataSourceFactory(String category) {
        movieLiveDataSource = new MutableLiveData<>();
        this.category = category;
    }

    @Override
    public DataSource<Integer, Movie> create() {
        MovieDataSource movieDataSource = new MovieDataSource(category);
        movieLiveDataSource.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Movie>> getItemLiveDataSource() {
        return movieLiveDataSource;
    }
}
