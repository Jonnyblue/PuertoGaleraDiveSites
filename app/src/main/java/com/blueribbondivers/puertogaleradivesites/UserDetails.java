package com.blueribbondivers.puertogaleradivesites;

import java.util.ArrayList;

/**
 * Created by jonathan on 24/07/15.
 *
 * Facebook Graph
 * me?fields=id,name,email,first_name,gender,last_name,location
 */
public class UserDetails {
    private static UserDetails mUserDetailsInstance;
    private String mFacebookID;
    private String mName;
    private String mEmail;
    private String mFirst_name;
    private String mLast_name;
    private String mLast_updated;
    private String mLongitude;
    private String mLatitude;
    private String mGender;
    private ArrayList<String> friendlist;
    private ArrayList<String> friendNames;


    public ArrayList<String> getFriendNames() {
        return friendNames;
    }

    public void setFriendNames(ArrayList<String> friendNames) {
        this.friendNames = friendNames;
    }

    public ArrayList<String> getFriendlist() {
        return friendlist;
    }

    public void setFriendlist(ArrayList<String> friendlist) {
        this.friendlist = friendlist;
    }

    public static UserDetails getmUserDetailsInstance() {
        return mUserDetailsInstance;
    }

    public static void setmUserDetailsInstance(UserDetails mUserDetailsInstance) {
        UserDetails.mUserDetailsInstance = mUserDetailsInstance;
    }

    public int getFriendListSize()
    {
        int size = friendlist.size();
        return size;


    }


    public void addFriend(String friend)
    {
        friendlist.add(friend);
    }

    public void addFriendName(String friend)
    {
        friendNames.add(friend);
    }

    private UserDetails()
    {
        mFacebookID = "Fooked";
        friendlist = new ArrayList();
        friendNames = new ArrayList();
    }

    public static UserDetails getUserDetails()
    {
        // Return the instance
        return mUserDetailsInstance;
    }

    public static void initUser(){
        if (mUserDetailsInstance == null)
        {
            mUserDetailsInstance = new UserDetails();
        }
    }

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
