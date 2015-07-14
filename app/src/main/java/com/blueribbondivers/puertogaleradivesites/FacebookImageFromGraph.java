package com.blueribbondivers.puertogaleradivesites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by jonathan on 13/07/15.
 */
public class FacebookImageFromGraph {
    private ArrayList<FacebookImageSizes> mImageSizesArrayList;
    private String mID;
    private String mPicture;
    private Bitmap mPhoto;
    private Bitmap mThumbnail;

    public Bitmap getPhoto() {
        return mPhoto;
    }

    public void setPhoto(Bitmap photo) {
        mPhoto = photo;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getPicture() {
        return mPicture;
    }

    public void setPicture(String picture) {
        mPicture = picture;
    }

    public String getID() {
        return mID;
    }

    public void setID(String ID) {
        mID = ID;
    }

    public ArrayList<FacebookImageSizes> getImageSizesArrayList() {
        return mImageSizesArrayList;
    }

    public void setImageSizesArrayList(ArrayList<FacebookImageSizes> imageSizesArrayList) {
        mImageSizesArrayList = imageSizesArrayList;
    }
    FacebookImageFromGraph()
    {
        mID = "";
        mImageSizesArrayList = new ArrayList<>();
    }

    public void addFacebookImageToArrayList(FacebookImageSizes imageSize)
    {
        mImageSizesArrayList.add(imageSize);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }
}


