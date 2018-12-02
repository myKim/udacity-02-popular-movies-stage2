package com.myoung.android.popularmovies.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.Date;


@Entity(tableName = "favorite_movie")
public class MovieItem implements Parcelable {
    // Constants
    public static final String JSON_MI_ID = "id";
    public static final String JSON_MI_TITLE = "title";
    public static final String JSON_MI_POSTER_PATH = "poster_path";
    public static final String JSON_MI_BACKDROP_PATH = "backdrop_path";
    public static final String JSON_MI_VOTE_AVERAGE = "vote_average";
    public static final String JSON_MI_OVERVIEW = "overview";
    public static final String JSON_MI_RELEASE_DATE = "release_date";

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_POSTER = "w185";
    private static final String IMAGE_SIZE_BACKDROP = "w780";

    // Variables
    @NonNull
    @PrimaryKey
    @ColumnInfo(name = JSON_MI_ID)
    @SerializedName(JSON_MI_ID)
    private String id;
    @ColumnInfo(name = JSON_MI_TITLE)
    @SerializedName(JSON_MI_TITLE)
    private String title;
    @ColumnInfo(name = JSON_MI_POSTER_PATH)
    @SerializedName(JSON_MI_POSTER_PATH)
    private String posterPath;
    @ColumnInfo(name = JSON_MI_OVERVIEW)
    @SerializedName(JSON_MI_OVERVIEW)
    private String overview;
    @ColumnInfo(name = JSON_MI_VOTE_AVERAGE)
    @SerializedName(JSON_MI_VOTE_AVERAGE)
    private String voteAverage;
    @ColumnInfo(name = JSON_MI_RELEASE_DATE)
    @SerializedName(JSON_MI_RELEASE_DATE)
    private String releaseDate;
    @ColumnInfo(name = JSON_MI_BACKDROP_PATH)
    @SerializedName(JSON_MI_BACKDROP_PATH)
    private String backdropPath;
    @ColumnInfo(name = "updated_at")
    private Date updatedAt;


    // Constructor
    public MovieItem(
            String id,
            String title,
            String posterPath,
            String overview,
            String voteAverage,
            String releaseDate,
            String backdropPath,
            Date updatedAt) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this. voteAverage = voteAverage;
        this.releaseDate = releaseDate;
        this.backdropPath = backdropPath;
        this.updatedAt = updatedAt;
    }

    @Ignore
    public MovieItem(MovieItem movie, Date updatedAt) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.posterPath = movie.getPosterPath();
        this.overview = movie.getOverview();
        this.voteAverage = movie.getVoteAverage();
        this.releaseDate = movie.getReleaseDate();
        this.backdropPath = movie.getBackdropPath();
        this.updatedAt = updatedAt;
    }


    public static String getFullImageUrl(String relativePath, String imageSize) {
        String fullUrl = null;
        if(relativePath != null) {
            fullUrl = IMAGE_BASE_URL + imageSize + relativePath;
        }
        return fullUrl;
    }

    public String getBackdropFullPath() {
        return getFullImageUrl(backdropPath, IMAGE_SIZE_BACKDROP);
    }

    public String getPosterFullPath() {
        return getFullImageUrl(posterPath, IMAGE_SIZE_POSTER);
    }


    protected MovieItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        voteAverage = in.readString();
        releaseDate = in.readString();
        backdropPath = in.readString();
    }

    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(voteAverage);
        dest.writeString(releaseDate);
        dest.writeString(backdropPath);
    }


    // Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(String voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
