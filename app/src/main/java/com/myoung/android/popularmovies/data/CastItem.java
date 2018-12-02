package com.myoung.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

public class CastItem {
    // Constants
    private static final String JSON_CI_CAST_ID = "cast_id";
    private static final String JSON_CI_CHARACTER = "character";
    private static final String JSON_CI_NAME = "name";
    private static final String JSON_CI_ORDER = "order";
    private static final String JSON_CI_PROFILE_PATH = "profile_path";

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_PROFILE_PATH = "w185";

    // Variables
    @SerializedName(JSON_CI_CAST_ID)
    private String castId;
    @SerializedName(JSON_CI_CHARACTER)
    private String character;
    @SerializedName(JSON_CI_NAME)
    private String name;
    @SerializedName(JSON_CI_ORDER)
    private String order;
    @SerializedName(JSON_CI_PROFILE_PATH)
    private String profilePath;


    public static String getFullImageUrl(String relativePath, String imageSize) {
        String fullUrl = null;
        if(relativePath != null) {
            fullUrl = IMAGE_BASE_URL + imageSize + relativePath;
        }
        return fullUrl;
    }

    public String getProfilePath() {
        return getFullImageUrl(profilePath, IMAGE_SIZE_PROFILE_PATH);
    }


    // Getter and Setter
    public String getCastId() {
        return castId;
    }

    public void setCastId(String castId) {
        this.castId = castId;
    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public void setProfilePath(String profilePath) {
        this.profilePath = profilePath;
    }
}
