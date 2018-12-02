package com.myoung.android.popularmovies;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myoung.android.popularmovies.adapter.CastListAdapter;
import com.myoung.android.popularmovies.adapter.ReviewListAdapter;
import com.myoung.android.popularmovies.adapter.VideoListAdapter;
import com.myoung.android.popularmovies.data.MovieItem;
import com.myoung.android.popularmovies.data.VideoItem;
import com.myoung.android.popularmovies.database.AppDatabase;
import com.myoung.android.popularmovies.utils.DateFormatUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieDetailActivity extends AppCompatActivity implements VideoListAdapter.VideoListAdapterOnItemClickHandler,
                                                                            VideoListAdapter.VideoListAdapterOnShareButtonClickHandler {
    // TAG for logging
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    // Views
    @BindView(R.id.toolbar) Toolbar toolbar;

    @BindView(R.id.tv_md_year) TextView mTextViewReleaseDate;
    @BindView(R.id.tv_md_vote_average) TextView mTextViewVoteAverage;
    @BindView(R.id.tv_md_overview) TextView mTextViewOverview;
    @BindView(R.id.iv_md_poster) ImageView mImageViewPoster;
    @BindView(R.id.backdrop_image) ImageView mImageBackdrop;
    @BindView(R.id.iv_favorite) ImageView mButtonFavorite;

    @BindView(R.id.rv_cast_list) RecyclerView mRecyclerViewCastList;
    @BindView(R.id.ll_cast_loading_state) View mLayoutCastLoadingState;
    @BindView(R.id.tv_cast_not_found) TextView mTextViewCastNotFound;
    @BindView(R.id.pb_cast_loading) ProgressBar mProgressbarCastLoading;

    @BindView(R.id.rv_video_list) RecyclerView mRecyclerViewVideoList;
    @BindView(R.id.ll_video_loading_state) View mLayoutVideosLoadingState;
    @BindView(R.id.tv_video_not_found) TextView mTextViewVideosNotFound;
    @BindView(R.id.pb_video_loading) ProgressBar mProgressbarVideosLoading;

    @BindView(R.id.rv_review_list) RecyclerView mRecyclerViewReviewList;
    @BindView(R.id.ll_review_loading_state) View mLayoutReviewsLoadingState;
    @BindView(R.id.tv_review_not_found) TextView mTextViewReviewsNotFound;
    @BindView(R.id.pb_review_loading) ProgressBar mProgressbarReviewsLoading;

    // Variables
    private MovieDetailViewModel viewModel;
    private VideoListAdapter mVideoListAdapter;
    private CastListAdapter mCastListAdapter;
    private ReviewListAdapter mReviewListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        MovieItem movieItem = intent.getParcelableExtra(intent.EXTRA_TEXT);

        initActivity(movieItem);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch(id) {
            case android.R.id.home: // up button action
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initActivity(MovieItem movieItem) {
        // Set custom toolbar
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup RecyclerViews
        setupRecyclerViews();

        // Setup ViewModel
        setupViewModel(movieItem);

        // Set and load Data
        setMovieDetailData(movieItem);
        loadAdditionalData();
    }


    private void setupRecyclerViews() {
        // Setup Cast & Crew RecyclerView
        mCastListAdapter = new CastListAdapter(this);
        mRecyclerViewCastList.setAdapter(mCastListAdapter);
        final LinearLayoutManager castListLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        castListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerViewCastList.setLayoutManager(castListLayoutManager);


        // Setup Videos RecyclerView
        mVideoListAdapter = new VideoListAdapter(this, this, this);
        mRecyclerViewVideoList.setAdapter(mVideoListAdapter);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        mRecyclerViewVideoList.setLayoutManager(layoutManager);


        // Setup Reviews RecyclerView
        mReviewListAdapter = new ReviewListAdapter(this, mRecyclerViewReviewList);
        mRecyclerViewReviewList.setAdapter(mReviewListAdapter);
        final LinearLayoutManager reviewlayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }

            @Override
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        mRecyclerViewReviewList.setLayoutManager(reviewlayoutManager);
    }

    private void setupViewModel(MovieItem movie) {
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        MovieDetailViewModelFactory factory = new MovieDetailViewModelFactory(database, movie);

        // Initialize ViewModel
        viewModel = ViewModelProviders.of(this, factory).get(MovieDetailViewModel.class);

        // Observe Favorite
        viewModel.getFavoriteMovie().observe(this, (favoriteMovieEntry) -> {
            mButtonFavorite.setActivated((favoriteMovieEntry != null));
        });

        // Observe Cast & Crew
        viewModel.getCastList().observe(this, (castList) -> {
            if(castList == null || castList.size() == 0) {
                showCastNotFoundMessage();
            }
            else {
                showCastListView();
            }
            mCastListAdapter.setCastList(castList);
        });

        // Observe Videos
        viewModel.getVideoList().observe(this, (videoList) -> {
            if(videoList == null || videoList.size() == 0) {
                showVideoNotFoundMessage();
            }
            else {
                showVideoListView();
            }
            mVideoListAdapter.setVideoList(videoList);
        });

        // Reviews
        viewModel.getReviewList().observe(this, (reviewList) -> {
            if(reviewList == null || reviewList.size() == 0) {
                showReviewNotFoundMessage();
            }
            else {
                showReviewListView();
            }
            mReviewListAdapter.setReviewList(reviewList);
        });
    }

    private void setMovieDetailData(MovieItem movieItem) {
        if(movieItem == null) return;

        String title = movieItem.getTitle();
        String voteAverage = movieItem.getVoteAverage();
        String overview = movieItem.getOverview();
        String releaseDate = movieItem.getReleaseDate();
        String posterPath = movieItem.getPosterFullPath();
        String backdropPath = movieItem.getBackdropFullPath();
        String year = DateFormatUtils.convertDatePattern(releaseDate,
                "yyyy-MM-dd",
                "yyyy");
        releaseDate = DateFormatUtils.convertDatePattern(releaseDate,
                "yyyy-MM-dd",
                "dd MMM yyyy",
                new Locale("en", "US"));

        if(year != null && year.length() > 0) {
            title = title + " (" + year + ")";
        }

        getSupportActionBar().setTitle(title);
        mTextViewVoteAverage.setText(voteAverage);
        mTextViewOverview.setText(overview);
        mTextViewReleaseDate.setText(releaseDate);

        Glide.with(this)
                .load(posterPath)
                .into(mImageViewPoster);

        // Apply color filter to image to make it darker.
        int color = ContextCompat.getColor(this, R.color.colorFilterBackDrop);
        mImageBackdrop.setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        Glide.with(this)
                .load(backdropPath)
                .into(mImageBackdrop);
    }

    private void loadAdditionalData() {
        // Load Cast & Crew
        showCastLoading();
        viewModel.loadCastList();

        // Load Videos
        showVideoLoading();
        viewModel.loadVideoList();

        // Load Reviews
        showReviewLoading();
        viewModel.loadReviewList();
    }


    public void onFavoriteButtonClicked(View view) {
        ImageView imageView = (ImageView) view;

        if(view.isActivated()) {
            viewModel.deleteFavorite();
        }
        else {
            viewModel.insertFavorite();
        }
    }

    @Override
    public void onItemClick(VideoItem videoItem) {
        String videoUrl = videoItem.getVideoUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(videoUrl))
                .setPackage("com.google.android.youtube");
        startActivity(intent);
    }

    @Override
    public void onShareButtonClick(VideoItem videoItem) {
        String videoUrl = videoItem.getVideoUrl();
        shareText(videoUrl);
    }

    public void shareText(String textToShare) {
        String mimeType = "text/plain";

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setText(textToShare)
                .startChooser();
    }


    private void showCastListView() {
        mRecyclerViewCastList.setVisibility(View.VISIBLE);
        mLayoutCastLoadingState.setVisibility(View.GONE);
    }

    private void showCastNotFoundMessage() {
        mRecyclerViewCastList.setVisibility(View.GONE);
        mLayoutCastLoadingState.setVisibility(View.VISIBLE);
        mTextViewCastNotFound.setVisibility(View.VISIBLE);
        mProgressbarCastLoading.setVisibility(View.GONE);
    }

    private void showCastLoading() {
        mRecyclerViewCastList.setVisibility(View.GONE);
        mLayoutCastLoadingState.setVisibility(View.VISIBLE);
        mTextViewCastNotFound.setVisibility(View.GONE);
        mProgressbarCastLoading.setVisibility(View.VISIBLE);
    }


    private void showVideoListView() {
        mRecyclerViewVideoList.setVisibility(View.VISIBLE);
        mLayoutVideosLoadingState.setVisibility(View.GONE);
    }

    private void showVideoNotFoundMessage() {
        mRecyclerViewVideoList.setVisibility(View.GONE);
        mLayoutVideosLoadingState.setVisibility(View.VISIBLE);
        mTextViewVideosNotFound.setVisibility(View.VISIBLE);
        mProgressbarVideosLoading.setVisibility(View.GONE);
    }

    private void showVideoLoading() {
        mRecyclerViewVideoList.setVisibility(View.GONE);
        mLayoutVideosLoadingState.setVisibility(View.VISIBLE);
        mTextViewVideosNotFound.setVisibility(View.GONE);
        mProgressbarVideosLoading.setVisibility(View.VISIBLE);
    }


    private void showReviewListView() {
        mRecyclerViewReviewList.setVisibility(View.VISIBLE);
        mLayoutReviewsLoadingState.setVisibility(View.GONE);
    }

    private void showReviewNotFoundMessage() {
        mRecyclerViewReviewList.setVisibility(View.GONE);
        mLayoutReviewsLoadingState.setVisibility(View.VISIBLE);
        mTextViewReviewsNotFound.setVisibility(View.VISIBLE);
        mProgressbarReviewsLoading.setVisibility(View.GONE);
    }

    private void showReviewLoading() {
        mRecyclerViewReviewList.setVisibility(View.GONE);
        mLayoutReviewsLoadingState.setVisibility(View.VISIBLE);
        mTextViewReviewsNotFound.setVisibility(View.GONE);
        mProgressbarReviewsLoading.setVisibility(View.VISIBLE);
    }

}
