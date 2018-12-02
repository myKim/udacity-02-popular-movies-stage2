package com.myoung.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

public class ReviewItem {
    // Constants
    private static final String JSON_RI_AUTHOR = "author";
    private static final String JSON_RI_CONTENT = "content";
    private static final String JSON_RI_ID = "id";
    private static final String JSON_RI_URL = "url";

    // Variables
    @SerializedName(JSON_RI_AUTHOR)
    private String author;
    @SerializedName(JSON_RI_CONTENT)
    private String content;
    @SerializedName(JSON_RI_ID)
    private String id;
    @SerializedName(JSON_RI_URL)
    private String url;
    private boolean expanded;


    // Getter and Setter
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void toggleExpanded() {
        expanded = !expanded;
    }
}
