package com.examearn.popularmovies.room;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.examearn.popularmovies.MovieDataSource;
import com.examearn.popularmovies.MovieDataSourceFactory;
import com.examearn.popularmovies.model.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mMovieRepository;
    private LiveData<List<Movie>> listFavouriteData;

    private LiveData<PagedList<Movie>> moviePageList;
    private LiveData<PageKeyedDataSource<Integer, Movie>> liveDataSource;


    public MovieViewModel(@NonNull Application application) {
        super(application);
        mMovieRepository = new MovieRepository(application);
        listFavouriteData = mMovieRepository.getmAllFavouriteMovie();
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory("popular");
        liveDataSource = movieDataSourceFactory.getItemLiveDataSource();

        PagedList.Config pageListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(MovieDataSource.PAGE_SIZE).build();

        moviePageList = (new LivePagedListBuilder(movieDataSourceFactory, pageListConfig)).build();


    }

    public LiveData<List<Movie>> getListFavouriteData() {
        return listFavouriteData;
    }

    public void insert(Movie details) {
        mMovieRepository.insert(details);
    }

    public void delete(Movie details) {
        mMovieRepository.delete(details);
    }

    public String getMovieId(String id) {
        return mMovieRepository.getMovieId(id);
    }

    public LiveData<List<Movie>> getPopularMovies(String category, String key, int page) {
        return mMovieRepository.getAllPopularMovie(category, key, page);
    }

    public LiveData<PagedList<Movie>> getMoviePageList(String category) {
        MovieDataSourceFactory movieDataSourceFactory = new MovieDataSourceFactory(category);
        liveDataSource = movieDataSourceFactory.getItemLiveDataSource();

        PagedList.Config pageListConfig = (new PagedList.Config.Builder())
                .setEnablePlaceholders(false)
                .setPageSize(MovieDataSource.PAGE_SIZE).build();

        moviePageList = (new LivePagedListBuilder(movieDataSourceFactory, pageListConfig)).build();

        return moviePageList;
    }

    public LiveData<PagedList<Movie>> getInitialMoviePageList() {

        return moviePageList;
    }
}
