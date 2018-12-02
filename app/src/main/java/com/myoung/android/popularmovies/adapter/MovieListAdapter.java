package com.myoung.android.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.myoung.android.popularmovies.R;
import com.myoung.android.popularmovies.data.MovieItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // TAG for logging
    private static final String TAG = MovieListAdapter.class.getSimpleName();

    // Constants
    private final int VIEW_TYPE_MOVIE = 0;
    private final int VIEW_TYPE_LOAD = 1;

    // Variables
    private Context context;
    private final MovieListAdapterOnItemClickHandler mItemClickHandler;
    private List<MovieItem> mMovieList;


    // Constructor
    public MovieListAdapter(Context context, MovieListAdapterOnItemClickHandler itemClickHandler) {
        this.context = context;
        this.mItemClickHandler = itemClickHandler;
    }


    // Getter
    public List<MovieItem> getMovieList() {
        return mMovieList;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        int layoutIdForListItem;
        boolean shouldAttachToParentImmediately = false;

        View view = null;
        switch(viewType) {
            case VIEW_TYPE_MOVIE:
                layoutIdForListItem = R.layout.movie_list_item;
                view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
                return new MovieListViewHolder(view);

            case VIEW_TYPE_LOAD:
                layoutIdForListItem = R.layout.progress_bar_item;
                view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
                return new LoadViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        switch(viewType) {
            case VIEW_TYPE_MOVIE:
                MovieItem movieItem = mMovieList.get(position);
                String movieTitle = movieItem.getTitle();
                String moviePosterUrl = movieItem.getPosterFullPath();

                MovieListViewHolder movieViewHolder = (MovieListViewHolder) viewHolder;

                movieViewHolder.mMovieTitleTextView.setText(movieTitle);
                Glide.with(context)
                        .load(moviePosterUrl)
                        .into(movieViewHolder.mMoviePosterImageView);
                break;

            case VIEW_TYPE_LOAD:
                LoadViewHolder loadViewHolder =  (LoadViewHolder) viewHolder;
                loadViewHolder.mProgressBar.setIndeterminate(true);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return (mMovieList == null) ? 0 : mMovieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMovieList.get(position) == null ? VIEW_TYPE_LOAD : VIEW_TYPE_MOVIE;
    }

    public void setMovieListData(List<MovieItem> movieListData) {
        mMovieList = movieListData;
        notifyDataSetChanged();
    }


    // Interface
    public interface MovieListAdapterOnItemClickHandler {
        void onMovieItemClick(MovieItem movieItem);
    }


    // ViewHolder
    public class MovieListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_movie_title) TextView mMovieTitleTextView;
        @BindView(R.id.iv_movie_poster) ImageView mMoviePosterImageView;

        public MovieListViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            MovieItem movieItem = mMovieList.get(position);
            mItemClickHandler.onMovieItemClick(movieItem);
        }
    }

    public class LoadViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pb_load_more_item) ProgressBar mProgressBar;

        public LoadViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
