package com.blueribbondivers.puertogaleradivesites;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jonathan on 15/07/15.
 */
public class FacebookAlbumImageFragment extends Fragment{

    private ImageView mImageView;
    private TextView mTextView;
    private FacebookImageFromGraph mFacebookImageFromGraph;
    private String albumID;
    private ArrayList<FacebookImageFromGraph> mFacebookImages;

    public ArrayList<FacebookImageFromGraph> getmFacebookImages() {
        return mFacebookImages;
    }

    public void setmFacebookImages(ArrayList<FacebookImageFromGraph> mFacebookImages) {
        this.mFacebookImages = mFacebookImages;
    }

    public String getAlbumID() {
        return albumID;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public FacebookImageFromGraph getFacebookImageFromGraph() {
        return mFacebookImageFromGraph;
    }

    public void setFacebookImageFromGraph(FacebookImageFromGraph facebookImageFromGraph) {
        mFacebookImageFromGraph = facebookImageFromGraph;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {




        View v = inflater.inflate(R.layout.facebook_fragment,container,false);
        mImageView = (ImageView)v.findViewById(R.id.facebook_imageview);
        mTextView = (TextView)v.findViewById(R.id.facebook_image_textview);
        mImageView.setImageBitmap(mFacebookImageFromGraph.getPhoto());
        mTextView.setText(mFacebookImageFromGraph.getName());
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tag = getTag();
                Log.d("Tag","Tag is " +tag);
                DiveSites.get(getActivity()).setFragmentImageTapped(tag);
                Intent intent = new Intent(getActivity(), ImagePagerActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }

}
