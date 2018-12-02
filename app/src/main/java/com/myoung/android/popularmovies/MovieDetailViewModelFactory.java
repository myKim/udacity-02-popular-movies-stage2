package com.myoung.android.popularmovies;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.myoung.android.popularmovies.data.MovieItem;
import com.myoung.android.popularmovies.database.AppDatabase;

public class MovieDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase mDb;
    private final MovieItem mMovie;


    public MovieDetailViewModelFactory(AppDatabase database, MovieItem movie) {
        mDb = database;
        mMovie = movie;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new MovieDetailViewModel(mDb, mMovie);
    }
}
