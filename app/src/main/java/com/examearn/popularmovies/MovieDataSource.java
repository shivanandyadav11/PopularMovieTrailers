package com.examearn.popularmovies;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.examearn.popularmovies.model.Movie;
import com.examearn.popularmovies.model.MovieResult;
import com.examearn.popularmovies.network.ApiResponse;
import com.examearn.popularmovies.network.RetrofitCall;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, Movie> {

    public static final int PAGE_SIZE = 500;
    private static final int FIRST_PAGE = 1;
    private String category;

    MovieDataSource(String category) {
        this.category = category;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Movie> callback) {
        RetrofitCall.getInstance().getApiResponse().getMovies(category, BuildConfig.API_KEY, FIRST_PAGE).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                callback.onResult(response.body().getResults(), null, FIRST_PAGE + 1);
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Movie> callback) {
        RetrofitCall.getInstance().getApiResponse().getMovies(category, BuildConfig.API_KEY, params.key).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                callback.onResult(response.body().getResults(),  params.key + 1);
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
