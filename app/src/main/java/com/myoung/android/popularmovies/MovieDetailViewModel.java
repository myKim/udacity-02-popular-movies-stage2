package com.myoung.android.popularmovies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.myoung.android.popularmovies.data.CastItem;
import com.myoung.android.popularmovies.data.CastList;
import com.myoung.android.popularmovies.data.MovieItem;
import com.myoung.android.popularmovies.data.ReviewItem;
import com.myoung.android.popularmovies.data.ReviewList;
import com.myoung.android.popularmovies.data.VideoItem;
import com.myoung.android.popularmovies.data.VideoList;
import com.myoung.android.popularmovies.database.AppDatabase;
import com.myoung.android.popularmovies.utils.AppExecutors;
import com.myoung.android.popularmovies.utils.RetrofitUtils;
import com.myoung.android.popularmovies.utils.TheMovieDbService;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailViewModel extends ViewModel {
    // TAG for logging
    private static final String TAG = MovieDetailViewModel.class.getSimpleName();

    // Variables
    private AppDatabase database;
    private MovieItem movie;
    private LiveData<MovieItem> favoriteMovie;

    private MutableLiveData<List<CastItem>> castList;
    private MutableLiveData<List<VideoItem>> videoList;
    private MutableLiveData<List<ReviewItem>> reviewList;


    // Constructor
    public MovieDetailViewModel(AppDatabase database, MovieItem movie) {
        this.database = database;
        this.movie = movie;

        favoriteMovie = database.favoriteMovieDao().loadFavoriteMovieById(movie.getId());
    }


    public void loadVideoList() {
        if(movie == null) return;

        String movieId = movie.getId();
        String apiKey = BuildConfig.API_KEY;

        TheMovieDbService service = RetrofitUtils.getInstance().getService();
        Call<VideoList> call = service.getVideoList(movieId, apiKey);
        call.enqueue(new Callback<VideoList>() {
            @Override
            public void onResponse(Call<VideoList> call, Response<VideoList> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                List<VideoItem> loadedVideoList = response.body().getResults();
                videoList.setValue(loadedVideoList);
            }

            @Override
            public void onFailure(Call<VideoList> call, Throwable t) {

            }
        });
    }

    public void loadReviewList() {
        if(movie == null) return;

        String movieId = movie.getId();
        String apiKey = BuildConfig.API_KEY;

        TheMovieDbService service = RetrofitUtils.getInstance().getService();
        Call<ReviewList> call = service.getReviewList(movieId, apiKey);
        call.enqueue(new Callback<ReviewList>() {
            @Override
            public void onResponse(Call<ReviewList> call, Response<ReviewList> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                List<ReviewItem> loadedReviewList = response.body().getResults();
                reviewList.setValue(loadedReviewList);
            }

            @Override
            public void onFailure(Call<ReviewList> call, Throwable t) {

            }
        });
    }


    /********************
     * Database Request *
     ********************/
    public void insertFavorite() {
        Date date = new Date();

        final MovieItem favorite = new MovieItem(movie, date);
        AppExecutors.getsInstance().diskIO().execute(() -> {
            database.favoriteMovieDao().insertFavoriteMovie(favorite);
        });
    }

    public void deleteFavorite() {
        Date date = new Date();

        final MovieItem favorite = new MovieItem(movie, date);
        AppExecutors.getsInstance().diskIO().execute(() -> {
            database.favoriteMovieDao().deleteFavoriteMovie(favorite);
        });
    }

    /*******************
     * Network Request *
     *******************/
    public void loadCastList() {
        if(movie == null) return;

        String movieId = movie.getId();
        String apiKey = BuildConfig.API_KEY;

        TheMovieDbService service = RetrofitUtils.getInstance().getService();
        Call<CastList> call = service.getCastList(movieId, apiKey);
        call.enqueue(new Callback<CastList>() {
            @Override
            public void onResponse(Call<CastList> call, Response<CastList> response) {
                if(!response.isSuccessful()) {
                    return;
                }

                List<CastItem> loadedCastList = response.body().getCastList();
                castList.setValue(loadedCastList);
            }

            @Override
            public void onFailure(Call<CastList> call, Throwable t) {

            }
        });
    }


    // Getter
    public LiveData<MovieItem> getFavoriteMovie() {
        return favoriteMovie;
    }

    public LiveData<List<CastItem>> getCastList() {
        if(castList == null) {
            castList = new MutableLiveData<>();
        }
        return castList;
    }

    public LiveData<List<VideoItem>> getVideoList() {
        if(videoList == null) {
            videoList = new MutableLiveData<>();
        }
        return videoList;
    }

    public LiveData<List<ReviewItem>> getReviewList() {
        if(reviewList == null) {
            reviewList = new MutableLiveData<>();
        }
        return reviewList;
    }

}
