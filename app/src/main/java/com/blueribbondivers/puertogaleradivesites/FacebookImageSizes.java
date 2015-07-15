package com.blueribbondivers.puertogaleradivesites;

/**
 * Created by jonathan on 13/07/15.
 */
public class FacebookImageSizes {

    private String mHeight;
    private String mWidth;
    private String mUrl;
    private String mName;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getHeight() {
        return mHeight;
    }

    public void setHeight(String height) {
        mHeight = height;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getWidth() {
        return mWidth;
    }

    public void setWidth(String width) {
        mWidth = width;
    }


    public FacebookImageSizes(String width, String height, String url) {
        mWidth = width;
        mHeight = height;
        mUrl = url;

    }
}