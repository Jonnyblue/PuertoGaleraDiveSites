package com.blueribbondivers.puertogaleradivesites;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;

import java.io.FileDescriptor;
import java.io.PrintWriter;
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
    private LoginButton loginButton;
    private CallbackManager callbackManager;




    @Override
    public void onCreate(Bundle savedInstanceState){
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        super.onCreate(savedInstanceState);
        UUID crimeId = (UUID)getActivity().getIntent()
                .getSerializableExtra(EXTRA_SITE_ID);
        mDivesite = DiveSites.get(getActivity()).getDiveSite(crimeId);
        mContext = getActivity().getApplicationContext();
        getActivity().setTitle(mDivesite.getTitle());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.divesite_activity, parent, false);
        /*
        loginButton = (LoginButton)v.findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        loginButton.setFragment(this);
        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getActivity().getApplicationContext(), "Facebook is connected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getActivity().getApplicationContext(), "Facebook connection Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getActivity().getApplicationContext(), "Facebook did not Connect", Toast.LENGTH_LONG).show();
            }
        });
*/
        Resources resources = mContext.getResources();

        //VideoFragment f = VideoFragment.newInstance(mDivesite.getYoutube());
        //getFragmentManager().beginTransaction().replace(R.id.videofragment,f).commit();


        mDepthField = (TextView)v.findViewById(R.id.divesite_display_depth);
        mDepthField.setText(mDivesite.getMaxDepth());
        mSiteDescription = (TextView)v.findViewById(R.id.divesite_display_description);
        mSiteDescription.setText(mDivesite.getSiteDescription());
        //mSiteDescription.setMovementMethod(new ScrollingMovementMethod());
        mDistance = (TextView)v.findViewById(R.id.divesite_display_distance);
        mDistance.setText(mDivesite.getTravelTime());
        if (mImageView != null) {
            ((BitmapDrawable)mImageView.getDrawable()).getBitmap().recycle();
        }
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

        //super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(prefix, fd, writer, args);
    }

    public static DivesiteFragment newInstance(UUID divesiteID) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SITE_ID, divesiteID);
        DivesiteFragment fragment = new DivesiteFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
