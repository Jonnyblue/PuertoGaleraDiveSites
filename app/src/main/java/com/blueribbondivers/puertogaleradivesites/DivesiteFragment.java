package com.blueribbondivers.puertogaleradivesites;


import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by jonathan on 24/06/15.
 */
public class DivesiteFragment extends Fragment {
    private Divesite mDivesite;
    private TextView mDepthField;
    private TextView mSiteDescription;
    private TextView mDistance;
    private ProportionalImageView mImageView;
    public static final String EXTRA_SITE_ID = "com.blueribbondivers.extraSiteID";
    private Context mContext;







    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getActivity().getIntent()
                .getSerializableExtra(EXTRA_SITE_ID);
        mDivesite = DiveSites.get(getActivity()).getDiveSite(crimeId);
        mContext = getActivity().getApplicationContext();
        //getActivity().getActionBar().setTitle(Html.fromHtml("<font color=\"#1c3565\">" + mDivesite.getName() + "</font>"));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_divesite, parent, false);
        Resources resources = mContext.getResources();
        //setHasOptionsMenu(true);
        mDepthField = (TextView)v.findViewById(R.id.divesite_display_depth);
        mDepthField.setText(mDivesite.getMaxDepth());
        mSiteDescription = (TextView)v.findViewById(R.id.divesite_display_description);
        mSiteDescription.setText(mDivesite.getSiteDescription());
        mSiteDescription.setMovementMethod(new ScrollingMovementMethod());
        mDistance = (TextView)v.findViewById(R.id.divesite_display_distance);
        mDistance.setText(mDivesite.getTravelTime());
        mImageView = (ProportionalImageView)v.findViewById(R.id.divesite_display_imageView);
        String imageName = mDivesite.getPhoto();
        final int resourceID = resources.getIdentifier(imageName,"drawable",mContext.getPackageName());
        mImageView.setImageResource(resourceID);
        setHasOptionsMenu(true);

        return v;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.divesitemenu, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
}
