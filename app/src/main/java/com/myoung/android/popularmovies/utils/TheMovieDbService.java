package com.myoung.android.popularmovies.utils;

import com.myoung.android.popularmovies.data.CastList;
import com.myoung.android.popularmovies.data.MovieList;
import com.myoung.android.popularmovies.data.ReviewList;
import com.myoung.android.popularmovies.data.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheMovieDbService {
    // Path
    static final String PATH_DISCOVER = "discover";
    static final String PATH_MOVIE = "movie";
    static final String PATH_VIDEOS = "videos";
    static final String PATH_REVIEWS = "reviews";
    static final String PATH_POPULAR = "popular";
    static final String PATH_TOP_RATE = "top_rated";
    static final String PATH_CREDITS = "credits";

    // Query
    static final String PARAM_API_KEY = "api_key";
    static final String PARAM_PAGE = "page";
    static final String PARAM_SORT_BY = "sort_by";


    @GET(PATH_MOVIE + "/{sortOption}")
    Call<MovieList> getMovieList(
            @Path("sortOption") String sortOption,
            @Query(PARAM_API_KEY) String apiKeyV3,
            @Query(PARAM_PAGE) String page
    );

    // https://api.themoviedb.org/3/movie/<<movie_id>>/videos?api_key=<<api_key>>
    @GET(PATH_MOVIE + "/{movieId}/" + PATH_VIDEOS)
    Call<VideoList> getVideoList(
            @Path("movieId") String movieId,
            @Query(PARAM_API_KEY) String apiKeyV3
    );

    // https://api.themoviedb.org/3/movie/<<movie_id>>/credits?api_key=<<api_key>>
    @GET(PATH_MOVIE + "/{movieId}/" + PATH_CREDITS)
    Call<CastList> getCastList(
            @Path("movieId") String movieId,
            @Query(PARAM_API_KEY) String apiKeyV3
    );

    // https://api.themoviedb.org/3/movie/<<movie_id>>/reviews?api_key=<<api_key>>
    @GET(PATH_MOVIE + "/{movieId}/" + PATH_REVIEWS)
    Call<ReviewList> getReviewList(
            @Path("movieId") String movieId,
            @Query(PARAM_API_KEY) String apiKeyV3
    );
}
