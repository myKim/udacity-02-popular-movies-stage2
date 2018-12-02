package com.myoung.android.popularmovies.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtils {
    // Constants
    private static final String THEMOVIEDB_BASE_URL = "http://api.themoviedb.org/3/";

    // Variables
    private static RetrofitUtils instance = null;
    private Retrofit retrofit;
    private TheMovieDbService service;

    // Constructor
    private RetrofitUtils() {
        retrofit = new Retrofit.Builder()
                .baseUrl(THEMOVIEDB_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(TheMovieDbService.class);
    }

    public static RetrofitUtils getInstance() {
        if(instance == null) {
            instance = new RetrofitUtils();
        }
        return instance;
    }

    public TheMovieDbService getService() {
        return service;
    }

}
