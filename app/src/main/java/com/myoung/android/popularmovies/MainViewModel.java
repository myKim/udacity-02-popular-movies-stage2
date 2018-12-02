package com.myoung.android.popularmovies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.myoung.android.popularmovies.data.MovieItem;
import com.myoung.android.popularmovies.data.MovieList;
import com.myoung.android.popularmovies.database.AppDatabase;
import com.myoung.android.popularmovies.lib.SingleLiveEvent;
import com.myoung.android.popularmovies.utils.RetrofitUtils;
import com.myoung.android.popularmovies.utils.TheMovieDbService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends AndroidViewModel {
    // TAG for logging
    private static final String TAG = MainViewModel.class.getSimpleName();

    // Constants
    public static final String SORT_OPTION_POPULAR = "popular";
    public static final String SORT_OPTION_TOP_RATE = "top_rated";
    public static final String SORT_OPTION_FAVORITES = "favorites";

    // Variables
    private MutableLiveData<List<MovieItem>> movieList;
    private SingleLiveEvent<Void> error;
    private LiveData<List<MovieItem>> favorites;

    private String sortOption;
    private int currentPage;


    // Constructor
    public MainViewModel(@NonNull Application application) {
        super(application);

        // Initialize variables
        this.sortOption = SORT_OPTION_POPULAR;
        this.currentPage = 0;
    }


    public void loadMovieList(boolean isRefresh) {
        if(isRefresh) {
            currentPage = 0;
        }

        switch (sortOption) {
            case SORT_OPTION_POPULAR:
            case SORT_OPTION_TOP_RATE:
                loadMovieListByRetrofit(isRefresh);
                break;

            case SORT_OPTION_FAVORITES:
                movieList.setValue(favorites.getValue());
                break;
        }
    }


    /*******************
     * Network Request *
     *******************/
    private void loadMovieListByRetrofit(boolean isRefresh) {
        TheMovieDbService service = RetrofitUtils.getInstance().getService();

        String apiKey = BuildConfig.API_KEY;
        String page = String.valueOf(++currentPage);

        Call<MovieList> call = service.getMovieList(sortOption, apiKey, page);
        call.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if(!response.isSuccessful()) {
                    error.call();
                    return;
                }

                List<MovieItem> loadedMovieList = response.body().getResults();
                List<MovieItem> newMovieList = new ArrayList<>();

                if(!isRefresh) {
                    List<MovieItem> oldMovieList = movieList.getValue();

                    if(oldMovieList != null) {
                        newMovieList.addAll(oldMovieList);
                        int lastPosition = newMovieList.size() - 1;
                        if(newMovieList.get(lastPosition) == null) {
                            newMovieList.remove(lastPosition);
                        }
                    }
                }
                newMovieList.addAll(loadedMovieList);

                movieList.setValue(newMovieList);
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                error.call();
            }
        });
    }


    // Getter and Setter
    public LiveData<List<MovieItem>> getMovieList() {
        if(movieList == null) {
            movieList = new MutableLiveData<>();
        }
        return movieList;
    }

    public LiveData<Void> getError() {
        if(error == null) {
            error = new SingleLiveEvent<>();
        }
        return error;
    }

    public LiveData<List<MovieItem>> getFavorites() {
        if(favorites == null) {
            AppDatabase database = AppDatabase.getInstance(this.getApplication());
            favorites = database.favoriteMovieDao().loadAllFavorites();
        }
        return favorites;
    }

    public String getSortOption() {
        return sortOption;
    }

    public void setSortOption(String sortOption) {
        this.sortOption = sortOption;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

}
