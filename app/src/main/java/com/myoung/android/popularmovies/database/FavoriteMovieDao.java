package com.myoung.android.popularmovies.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.myoung.android.popularmovies.data.MovieItem;

import java.util.List;

@Dao
public interface FavoriteMovieDao {
    @Query("SELECT * FROM favorite_movie ORDER BY updated_at DESC")
    LiveData<List<MovieItem>> loadAllFavorites();

    @Query("SELECT * FROM favorite_movie WHERE id = :id")
    LiveData<MovieItem> loadFavoriteMovieById(String id);

    @Insert
    void insertFavoriteMovie(MovieItem favoriteMovieEntry);

    @Delete
    void deleteFavoriteMovie(MovieItem favoriteMovieEntry);
}
