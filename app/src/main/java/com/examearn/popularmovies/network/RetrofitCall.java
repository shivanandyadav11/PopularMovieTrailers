package com.examearn.popularmovies.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitCall {

    private static RetrofitCall mRetroFitInstance;
    private Retrofit retrofit;

    private RetrofitCall() {
        retrofit = new Retrofit.Builder()
                .baseUrl(ApiResponse.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static synchronized RetrofitCall getInstance() {
        if (mRetroFitInstance == null) {
            mRetroFitInstance = new RetrofitCall();
        }
        return mRetroFitInstance;
    }

    public ApiResponse getApiResponse() {
        return retrofit.create(ApiResponse.class);
    }

}
