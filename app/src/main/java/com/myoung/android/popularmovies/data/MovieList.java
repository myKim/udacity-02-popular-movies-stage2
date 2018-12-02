package com.myoung.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieList {
    // Constants
    private static final String JSON_ML_RESULTS = "results";

    // Variables
    @SerializedName(JSON_ML_RESULTS)
    private List<MovieItem> results;


    // Getter and Setter
    public List<MovieItem> getResults() {
        return results;
    }

    public void setResults(List<MovieItem> results) {
        this.results = results;
    }
}
