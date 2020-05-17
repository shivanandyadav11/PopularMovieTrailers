package com.examearn.popularmovies.room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.examearn.popularmovies.model.Movie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Movie.class}, version = 1, exportSchema = false)
public abstract class MovieRoomDatabase extends RoomDatabase {

    static final ExecutorService databaseWriteExecuter = Executors.newFixedThreadPool(4);
    private static volatile MovieRoomDatabase INSTANCE;

    static MovieRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (MovieRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MovieRoomDatabase.class, "local_movie_details")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract MovieDao favouriteMovieDao();
}
