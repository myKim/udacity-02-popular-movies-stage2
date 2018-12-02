package com.myoung.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CastList {
    // Constants
    private static final String JSON_CL_ID = "id";
    private static final String JSON_CL_CAST = "cast";

    // Variables
    @SerializedName(JSON_CL_ID)
    private String id;
    @SerializedName(JSON_CL_CAST)
    private List<CastItem> castList;


    // Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<CastItem> getCastList() {
        return castList;
    }

    public void setCastList(List<CastItem> castList) {
        this.castList = castList;
    }
}
