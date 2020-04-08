package com.examearn.popularmovies.room;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.examearn.popularmovies.model.Movie;
import com.examearn.popularmovies.model.MovieResult;
import com.examearn.popularmovies.network.RetrofitCall;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MovieRepository {

    private MovieDao mFavouriteMovieDao;
    private LiveData<List<Movie>> mAllFavouriteMovie;
    private MutableLiveData<List<Movie>> allMovies;

    MovieRepository(Application application) {
        MovieRoomDatabase database = MovieRoomDatabase.getDatabase(application);
        mFavouriteMovieDao = database.favouriteMovieDao();
        mAllFavouriteMovie = mFavouriteMovieDao.getFavouriteMovieDetail();
        allMovies = new MutableLiveData<>();
    }

    LiveData<List<Movie>> getmAllFavouriteMovie() {
        return mAllFavouriteMovie;
    }

    void insert(Movie details) {
        MovieRoomDatabase.databaseWriteExecuter.execute(() -> mFavouriteMovieDao.insert(details));
    }

    void delete(Movie details) {
        MovieRoomDatabase.databaseWriteExecuter.execute(() -> mFavouriteMovieDao.delete(details));
    }

    String getMovieId(String id) {
        return mFavouriteMovieDao.getFavouriteMovieId(id);
    }

    LiveData<List<Movie>> getAllPopularMovie(String category, String key,int page) {
        RetrofitCall.getInstance().getApiResponse().getMovies(category, key, page).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                allMovies.postValue(response.body().getResults());
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
        return allMovies;
    }
}
