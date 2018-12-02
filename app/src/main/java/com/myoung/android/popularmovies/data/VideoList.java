package com.myoung.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VideoList {
    // Constants
    private static final String JSON_VL_ID = "id";
    private static final String JSON_VL_RESULTS = "results";

    // Variables
    @SerializedName(JSON_VL_ID)
    private String id;
    @SerializedName(JSON_VL_RESULTS)
    private List<VideoItem> results;


    // Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<VideoItem> getResults() {
        return results;
    }

    public void setResults(List<VideoItem> results) {
        this.results = results;
    }
}
