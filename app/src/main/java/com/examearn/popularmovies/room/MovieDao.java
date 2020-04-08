package com.examearn.popularmovies.room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.examearn.popularmovies.model.Movie;

import java.util.List;

@Dao
public interface MovieDao {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    void insert(Movie favouriteMovieDetails);

    @Delete
    void delete(Movie favouriteMovieDetails);

    @Query("SELECT * from local_movie_details")
    LiveData<List<Movie>> getFavouriteMovieDetail();

    @Query("SELECT id from local_movie_details WHERE id = :movieId")
    String getFavouriteMovieId(String movieId);
}
