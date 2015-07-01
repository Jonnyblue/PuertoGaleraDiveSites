package com.blueribbondivers.puertogaleradivesites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.UUID;

/**
 * Created by jonathan on 24/06/15.
 */
public class DivesiteFragment extends Fragment {
    private Divesite mDivesite;
    private TextView mTitleField;
    private TextView mDepthField;
    private TextView mSiteDescription;
    private TextView mDistance;
    public static final String EXTRA_SITE_ID = "com.blueribbondivers.extraSiteID";




    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getActivity().getIntent()
                .getSerializableExtra(EXTRA_SITE_ID);
        mDivesite = DiveSites.get(getActivity()).getDiveSite(crimeId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_divesite,parent,false);

        mTitleField = (TextView)v.findViewById(R.id.divesite_display_name);
        mTitleField.setText(mDivesite.getName());
        mDepthField = (TextView)v.findViewById(R.id.divesite_display_depth);
        mDepthField.setText(mDivesite.getMaxDepth());
        mSiteDescription = (TextView)v.findViewById(R.id.divesite_display_description);
        mSiteDescription.setText(mDivesite.getSiteDescription());
        mDistance = (TextView)v.findViewById(R.id.divesite_display_distance);
        mDistance.setText(mDivesite.getTravelTime());


        return v;


    }
}
