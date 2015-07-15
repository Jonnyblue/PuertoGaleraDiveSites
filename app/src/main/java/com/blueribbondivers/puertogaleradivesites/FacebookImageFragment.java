package com.blueribbondivers.puertogaleradivesites;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by jonathan on 15/07/15.
 */
public class FacebookImageFragment extends Fragment{

    private ImageView mImageView;
    private TextView mTextView;
    private FacebookImageFromGraph mFacebookImageFromGraph;

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

        return v;
    }
}
