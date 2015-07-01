package com.blueribbondivers.puertogaleradivesites;

import java.util.UUID;

/**
 * Created by jonathan on 24/06/15.
 */
public class Divesite {

    @Override
    public String toString() {
        return mName;
    }

    private UUID mId;

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    private String mName;

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getMaxDepth() {
        return mMaxDepth;
    }

    public void setMaxDepth(String maxDepth) {
        mMaxDepth = maxDepth;
    }

    public String getPhoto() {
        return mPhoto;
    }

    public void setPhoto(String photo) {
        mPhoto = photo;
    }

    public String getSiteDescription() {
        return mSiteDescription;
    }

    public void setSiteDescription(String siteDescription) {
        mSiteDescription = siteDescription;
    }

    public String getYoutube() {
        return mYoutube;
    }

    public void setYoutube(String youtube) {
        mYoutube = youtube;
    }

    public String getTravelTime() {
        return mTravelTime;
    }

    public void setTravelTime(String travelTime) {
        mTravelTime = travelTime;
    }

    private String mLatitude;
    private String mLongitude;
    private String mMaxDepth;
    private String mPhoto;
    private String mSiteDescription;
    private String mTravelTime;
    private String mYoutube;


    public Divesite()
    {
        mName = "";
        mLatitude = "";
        mLongitude = "";
        mMaxDepth = "";
        mPhoto = "";
        mSiteDescription = "";
        mTravelTime = "";
        mYoutube = "";

    }

    public String getTitle() {
        return mName;
    }
    public void setTitle(String title) {
        this.mName = title;
    }


}
