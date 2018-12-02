package com.myoung.android.popularmovies.data;

import com.google.gson.annotations.SerializedName;

public class VideoItem {
    // Constants
    private static final String JSON_VI_ID = "id";
    private static final String JSON_VI_KEY = "key";
    private static final String JSON_VI_NAME = "name";
    private static final String JSON_VI_SITE = "site";
    private static final String JSON_VI_TYPE = "type";

    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/";
    private static final String YOUTUBE_IMAGE_BASE_URL = "https://img.youtube.com/vi/";

    private static final String YOUTUBE_IMAGE_MQ = "mqdefault.jpg";
    private static final String YOUTUBE_IMAGE_HQ = "hqdefault.jpg";

    private static final String SITE_YOUTUBE = "YouTube";

    public static final int IMAGE_HIGH_QUALITY = 0;
    public static final int IMAGE_MIDDLE_QUALITY = 1;
    public static final int IMAGE_LOW_QUALITY = 3;


    // Variables
    @SerializedName(JSON_VI_ID)
    private String id;
    @SerializedName(JSON_VI_KEY)
    private String key;
    @SerializedName(JSON_VI_NAME)
    private String name;
    @SerializedName(JSON_VI_SITE)
    private String site;
    @SerializedName(JSON_VI_TYPE)
    private String type;


    /************************************************************
     * https://img.youtube.com/vi/<<key>>/mqdefault.jpg
     * https://img.youtube.com/vi/<<key>>/hqdefault.jpg
     * https://www.youtube.com/watch?v=<<key>>
     ************************************************************/
    public String getThumbnailUrl(int imageQuality) {
        String url = null;
        switch (site) {
            case SITE_YOUTUBE :
                String quality = (imageQuality == IMAGE_HIGH_QUALITY) ? YOUTUBE_IMAGE_HQ : YOUTUBE_IMAGE_MQ;
                url = YOUTUBE_IMAGE_BASE_URL + key + "/" + quality;
                break;
        }
        return url;
    }

    public String getVideoUrl() {
        String url = null;
        switch (site) {
            case SITE_YOUTUBE :
                url = YOUTUBE_BASE_URL + "watch?v=" + key;
                break;
        }
        return url;
    }


    // Getter and Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
