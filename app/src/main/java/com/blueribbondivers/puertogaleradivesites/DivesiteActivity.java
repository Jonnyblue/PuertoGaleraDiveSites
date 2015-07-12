package com.blueribbondivers.puertogaleradivesites;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.UUID;


/**
 * Created by jonathan on 12/07/15.
 */
public class DivesiteActivity extends AppCompatActivity  {

    private Divesite mDivesite;
    private TextView mDepthField;
    private TextView mSiteDescription;
    private TextView mDistance;
    private ProportionalImageView mImageView;
    public static final String EXTRA_SITE_ID = "com.blueribbondivers.extraSiteID";
    private Context mContext;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FrameLayout videoLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divesite_activity);
        UUID crimeId = (UUID)getIntent()
                .getSerializableExtra(EXTRA_SITE_ID);

        mDivesite = DiveSites.get(this).getDiveSite(crimeId);
        mContext = getApplicationContext();
        setTitle(mDivesite.getTitle());

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getApplicationContext(), "Facebook is connected", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Facebook connection Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "Facebook did not Connect", Toast.LENGTH_LONG).show();
            }
        });

        Resources resources = mContext.getResources();

        VideoFragment f = VideoFragment.newInstance(mDivesite.getYoutube());
        getFragmentManager().beginTransaction().replace(R.id.videofragment,f).commit();


        mDepthField = (TextView)findViewById(R.id.divesite_display_depth);
        mDepthField.setText(mDivesite.getMaxDepth());
        mSiteDescription = (TextView)findViewById(R.id.divesite_display_description);
        mSiteDescription.setText(mDivesite.getSiteDescription());
        //mSiteDescription.setMovementMethod(new ScrollingMovementMethod());
        mDistance = (TextView)findViewById(R.id.divesite_display_distance);
        mDistance.setText(mDivesite.getTravelTime());
        if (mImageView != null) {
            ((BitmapDrawable)mImageView.getDrawable()).getBitmap().recycle();
        }
        mImageView = (ProportionalImageView)findViewById(R.id.divesite_display_imageView);
        String imageName = mDivesite.getPhoto();
        final int resourceID = resources.getIdentifier(imageName,"drawable",mContext.getPackageName());
        mImageView.setImageResource(resourceID);

        videoLayout = (FrameLayout)findViewById(R.id.videofragment);
        Boolean internetConnected = this.isOnline();

        if (internetConnected)
        {
            Toast.makeText(getApplicationContext(), "Network Is Connected", Toast.LENGTH_LONG).show();
            loginButton.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            videoLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Network Is NOT Connected", Toast.LENGTH_LONG).show();
            loginButton.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);

        }



    }
/*
    public static DivesiteActivity newInstance(UUID divesiteID) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SITE_ID, divesiteID);
        DivesiteFragment fragment = new DivesiteFragment();
        fragment.setArguments(args);
        return fragment;
    }
*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}

