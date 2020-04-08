package com.examearn.popularmovies.network;

import com.examearn.popularmovies.model.MovieResult;
import com.examearn.popularmovies.model.ReviewResult;
import com.examearn.popularmovies.model.TrailerResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiResponse {

    String BASE_URL = "https://api.themoviedb.org/3/movie/";

    @GET("{category}")
    Call<MovieResult> getMovies(@Path("category") String category,
                                @Query("api_key") String key, @Query("page") int page);

    @GET("{id}/videos")
    Call<TrailerResult> getMovieTrailers(@Path("id") String id, @Query("api_key") String key);

    @GET("{id}/reviews")
    Call<ReviewResult> getMovieReview(@Path("id") String id, @Query("api_key") String key);

}
