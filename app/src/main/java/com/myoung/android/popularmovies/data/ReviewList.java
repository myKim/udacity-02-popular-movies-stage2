package com.myoung.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewList {
    // Constants
    private static final String JSON_RL_ID = "id";
    private static final String JSON_RL_PAGE = "page";
    private static final String JSON_RL_RESULTS = "results";
    private static final String JSON_RL_TOTAL_PAGES = "total_pages";
    private static final String JSON_RL_TOTAL_RESULTS = "total_results";

    // Variables
    @SerializedName(JSON_RL_ID)
    private String id;
    @SerializedName(JSON_RL_PAGE)
    private String page;
    @SerializedName(JSON_RL_RESULTS)
    private List<ReviewItem> results;
    @SerializedName(JSON_RL_TOTAL_PAGES)
    private String totalPages;
    @SerializedName(JSON_RL_TOTAL_RESULTS)
    private String totalResults;


    // Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<ReviewItem> getResults() {
        return results;
    }

    public void setResults(List<ReviewItem> results) {
        this.results = results;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }
}
