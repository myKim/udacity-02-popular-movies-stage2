package com.myoung.android.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myoung.android.popularmovies.adapter.MovieListAdapter;
import com.myoung.android.popularmovies.data.MovieItem;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieListAdapter.MovieListAdapterOnItemClickHandler {
    // TAG for logging
    private static final String TAG = MainActivity.class.getSimpleName();

    // Constants
    private static final int VISIBLE_THRESHOLD = 6;
    private static final int SPAN_COUNT_PORTRAIT = 2;
    private static final int SPAN_COUNT_LANDSCAPE = 4;
    private static final String KEY_ISLOADING = "isLoading";
    private static final String KET_MENU_SELECTED = "menuSelected";

    // Views
    @BindView(R.id.rv_movie_list) RecyclerView mRecyclerView;
    @BindView(R.id.pb_load_data) ProgressBar mProgressBarLoadData;
    @BindView(R.id.tv_error_message) TextView mErrorMessage;

    // Variables
    private MovieListAdapter mMovieListAdapter;
    private AtomicBoolean isLoading;
    private MainViewModel viewModel;
    private int menuSelected = -1;  // to save sort option


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initActivity(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        // Restore saved sort option
        if(menuSelected != -1) {
            MenuItem menuItem = (MenuItem) menu.findItem(menuSelected);
            menuItem.setChecked(true);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_ISLOADING, isLoading.get());
        outState.putInt(KET_MENU_SELECTED, menuSelected);

        super.onSaveInstanceState(outState);
    }


    private void initActivity(Bundle savedInstanceState) {
        // Restore saved data
        boolean loading = false;
        if(savedInstanceState != null) {
            loading = savedInstanceState.getBoolean(KEY_ISLOADING, loading);
            menuSelected = savedInstanceState.getInt(KET_MENU_SELECTED, -1);
        }
        isLoading = new AtomicBoolean(loading);

        // Setup RecyclerViews
        setupRecyclerViews();

        // Setup ViewModel
        setupViewModel();

        // Load Data
        if(viewModel.getMovieList().getValue() == null) {
            loadMovieList(true);
        }
    }


    private void setupRecyclerViews() {
        // Setup Movie List RecyclerView
        mMovieListAdapter = new MovieListAdapter(this, this);

        int orientation = getResources().getConfiguration().orientation;
        int spanCount = (orientation == Configuration.ORIENTATION_PORTRAIT) ? SPAN_COUNT_PORTRAIT : SPAN_COUNT_LANDSCAPE;
        final GridLayoutManager layoutManager = new GridLayoutManager(this, spanCount);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                List<MovieItem> items = mMovieListAdapter.getMovieList();
                if(items != null) {
                    if(items.get(position) == null) {
                        return layoutManager.getSpanCount();
                    }
                    else {
                        return 1;
                    }
                }
                return 0;
            }
        });
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mMovieListAdapter);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Sort option Favorites don't use infinite scroll
                if(viewModel.getSortOption() == MainViewModel.SORT_OPTION_FAVORITES) {
                    return;
                }

                int currentItems = layoutManager.getChildCount();
                int totalItems = layoutManager.getItemCount();
                int scrollOutItems = layoutManager.findFirstVisibleItemPosition();

                if(!isLoading.get() && (currentItems + scrollOutItems + VISIBLE_THRESHOLD >= totalItems)) {
                    loadMovieList(false);
                }
            }
        });
    }

    private void setupViewModel() {
        // Initialize ViewModel
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        // Observe MovieList
        viewModel.getMovieList().observe(this, (movieList) -> {
            mProgressBarLoadData.setVisibility(View.INVISIBLE);
            showMovieListView();
            mMovieListAdapter.setMovieListData(movieList);
            isLoading.compareAndSet(true, false);
        });

        // Observe Error
        viewModel.getError().observe(this, (v) -> {
            mProgressBarLoadData.setVisibility(View.INVISIBLE);
            showErrorMessage();
            isLoading.compareAndSet(true, false);
        });

        // Observe Favorites
        viewModel.getFavorites().observe(this, (favorites) -> {
            if(viewModel.getSortOption() == MainViewModel.SORT_OPTION_FAVORITES) {
                viewModel.loadMovieList(true);
            }
        });
    }

    private void loadMovieList(boolean isRefresh) {
        if(!isLoading.compareAndSet(false, true)) return;

        showMovieListView();

        if(isRefresh) {
            // Show a progress bar in the center of the view.
            mProgressBarLoadData.setVisibility(View.VISIBLE);
            // Clear adapter
            mMovieListAdapter.setMovieListData(null);
        }
        else {
            // Add a progress bar item.
            mMovieListAdapter.getMovieList().add(null);
            int lastPosition = mMovieListAdapter.getMovieList().size() - 1;
            mMovieListAdapter.notifyItemInserted(lastPosition);
        }
        viewModel.loadMovieList(isRefresh);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case R.id.menu_sort_popular:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    menuSelected = item.getItemId();
                    viewModel.setSortOption(MainViewModel.SORT_OPTION_POPULAR);
                    loadMovieList(true);
                    return true;
                }
                break;

            case R.id.menu_sort_top_rate:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    menuSelected = item.getItemId();
                    viewModel.setSortOption(MainViewModel.SORT_OPTION_TOP_RATE);
                    loadMovieList(true);
                    return true;
                }
                break;

            case R.id.menu_favorites:
                if(!item.isChecked()) {
                    item.setChecked(true);
                    menuSelected = item.getItemId();
                    viewModel.setSortOption(MainViewModel.SORT_OPTION_FAVORITES);
                    loadMovieList(true);
                    return true;
                }
                break;

            case R.id.menu_refresh:
                loadMovieList(true);
                return true;

            case R.id.menu_license_notice:
                startLicenseActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieItemClick(MovieItem movieItem) {
        // Movie item click event
        startMovieDetailActivity(movieItem);
    }

    private void startMovieDetailActivity(MovieItem movieItem) {
        Context context = this;
        Class destinationClass = MovieDetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, movieItem);  // Put movie data into Extra

        startActivity(intentToStartDetailActivity);
    }

    private void startLicenseActivity() {
        Context context = this;
        Class destinationClass = LicenseActivity.class;
        Intent intent = new Intent(context, destinationClass);

        startActivity(intent);
    }

    private void showMovieListView() {
        mErrorMessage.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

}