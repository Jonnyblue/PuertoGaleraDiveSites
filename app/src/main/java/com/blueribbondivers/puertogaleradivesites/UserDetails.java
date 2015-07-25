package com.blueribbondivers.puertogaleradivesites;

/**
 * Created by jonathan on 24/07/15.
 *
 * Facebook Graph
 * me?fields=id,name,email,first_name,gender,last_name,location
 */
public class UserDetails {
    private String mFacebookID;
    private String mName;
    private String mEmail;
    private String mFirst_name;
    private String mLast_name;
    private String mLocationName;
    private String mLocationID;
    private String mLast_updated;
    private String mLongitude;
    private String mLatitude;
    private String mGender;

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getFacebookID() {
        return mFacebookID;
    }

    public void setFacebookID(String facebookID) {
        mFacebookID = facebookID;
    }

    public String getFirst_name() {
        return mFirst_name;
    }

    public void setFirst_name(String first_name) {
        mFirst_name = first_name;
    }

    public String getLast_name() {
        return mLast_name;
    }

    public void setLast_name(String last_name) {
        mLast_name = last_name;
    }

    public String getLast_updated() {
        return mLast_updated;
    }

    public void setLast_updated(String last_updated) {
        mLast_updated = last_updated;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLocationID() {
        return mLocationID;
    }

    public void setLocationID(String locationID) {
        mLocationID = locationID;
    }

    public String getLocationName() {
        return mLocationName;
    }

    public void setLocationName(String locationName) {
        mLocationName = locationName;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }
}
