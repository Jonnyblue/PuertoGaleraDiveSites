package com.blueribbondivers.puertogaleradivesites;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by jonathan on 12/07/15.
 */
public class DivesiteActivity extends AppCompatActivity  {

    private Divesite mDivesite;
    private TextView mDepthField;
    private TextView mSiteDescription;
    private TextView mDistance;
    private LinearLayout mScrollview;
    private ImageView mThumbnailView;
    private ImageView mImageView;


    /** Bitmap Factory Options for setting up correct memory for mImageView */
    private BitmapFactory.Options options;
    private int imageHeight;
    private int imageWidth;
    private String imageType;
    /**
     * */
    public static final String LOGGEYPOOES = "LOGGEYPOOES";
    public static final String EXTRA_SITE_ID = "com.blueribbondivers.extraSiteID";

    public Context getContext() {
        return mContext;
    }
    public void setContext(Context context) {
        mContext = context;
    }

    private Context mContext;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FrameLayout videoLayout;
    private String accessToken;
    private ArrayList<FacebookImageFromGraph> mFacebookImages;
    private JSONObject faceBookResponse;


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
        mScrollview = (LinearLayout)findViewById(R.id.horizontal_thumbnail_view);

        loginButton = (LoginButton)findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = AccessToken.getCurrentAccessToken().getToken();
                Toast.makeText(getApplicationContext(), "Facebook is connected" + accessToken, Toast.LENGTH_LONG).show();

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
        mDistance = (TextView)findViewById(R.id.divesite_display_distance);
        mDistance.setText(mDivesite.getTravelTime());
        if (mImageView != null) {
            ((BitmapDrawable)mImageView.getDrawable()).getBitmap().recycle();
        }
        mImageView = (ImageView)findViewById(R.id.divesite_display_imageView);
        String imageName = mDivesite.getPhoto();
        /** Get Resource ID for mImageView */
        int resourceID = resources.getIdentifier(imageName,"drawable",mContext.getPackageName());
        /** Get Bitmap Options Setup for mImageView */
        Drawable myDrawable;
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            myDrawable = mContext.getResources().getDrawable(resourceID, mContext.getTheme());
        } else {
            myDrawable = mContext.getResources().getDrawable(resourceID);
        }
        mImageView.setImageDrawable(myDrawable);
        Log.d(LOGGEYPOOES, "Image Height = " + imageHeight + " Image Width = " + imageWidth + " Image Type = " + imageType);

        videoLayout = (FrameLayout)findViewById(R.id.videofragment);
        Boolean internetConnected = this.isOnline();

        if (internetConnected)
        {
            Toast.makeText(getApplicationContext(), "Network Is Connected", Toast.LENGTH_LONG).show();
            loginButton.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            videoLayout.setVisibility(View.VISIBLE);

            /** Start the Facebook Parser */
            Bundle parameters = new Bundle();
            parameters.putString("fields", "picture,images");
            new GraphRequest(

                    AccessToken.getCurrentAccessToken(),
                    mDivesite.getYoutubealbumid() + "/photos",
                    parameters,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            faceBookResponse = response.getJSONObject();
                            new FacebookParser().execute(faceBookResponse);
                        }
                    }
            ).executeAsync();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Network Is NOT Connected", Toast.LENGTH_LONG).show();
            loginButton.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        }
    }


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


    /** Async Facebook Parser */
    class FacebookParser extends AsyncTask<JSONObject, Void, ArrayList<FacebookImageFromGraph>> {
        private ArrayList<FacebookImageFromGraph> mFacebookImages;
        public static final String LOGGEYPOOES = "LOGGEYPOOES";

        @Override
        protected void onPostExecute(ArrayList<FacebookImageFromGraph> facebookImageFromGraphs) {
            Toast.makeText(getApplicationContext(), "Facebook Parsed?", Toast.LENGTH_LONG).show();
          //  ArrayList<Bitmap> images = new ArrayList();
            for (int i = 0;i<mFacebookImages.size();i++) {
                float scale = getResources().getDisplayMetrics().density;
                int dpAsPixels = (int) (200*scale + 0.5f);
                int padding = (int) (8*scale + 0.5f);
                mThumbnailView = new ImageView(DivesiteActivity.this);
                mThumbnailView.setImageBitmap(mFacebookImages.get(i).getPhoto());
                mThumbnailView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                mThumbnailView.getLayoutParams().height = dpAsPixels;
                mThumbnailView.getLayoutParams().width = dpAsPixels;
                mThumbnailView.setPadding(0,0,padding,0);
                mThumbnailView.setScaleType(ImageView.ScaleType.CENTER);
                mScrollview.addView(mThumbnailView);
                mScrollview.requestLayout();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            }

        }

        @Override
        protected ArrayList<FacebookImageFromGraph> doInBackground(JSONObject... params) {
            try {
                JSONObject allPhotoJsonObject = params[0];
                JSONArray dataArray = allPhotoJsonObject.getJSONArray("data");

                mFacebookImages = new ArrayList<>();
                for (int i=0; i< dataArray.length();i++)
                {
                    FacebookImageFromGraph newImage = new FacebookImageFromGraph();
                    JSONObject temp = dataArray.getJSONObject(i);
                    newImage.setPicture(temp.getString("picture"));
                    JSONArray imagesArray = temp.getJSONArray("images");
                    for (int j = 0; j< imagesArray.length(); j++){
                        JSONObject imageTemp = imagesArray.getJSONObject(j);

                        String url = imageTemp.getString("source");
                        String height = imageTemp.getString("height");
                        String width = imageTemp.getString("width");
                        FacebookImageSizes imageSizes = new FacebookImageSizes(width,height,url);
                        newImage.addFacebookImageToArrayList(imageSizes);
                    }
                    newImage.setID(temp.getString("id"));
                    /** Get not the largest or the smallest of the Bitmaps from the Array, might cause issues later...so far so good*/
                    Bitmap srcBmp = (newImage.getBitmapFromURL(newImage.getImageSizesArrayList().get(newImage.getImageSizesArrayList().size() - 3).getUrl()));
                    Bitmap dstBmp;

                    /** Crop images to make square thumbnails*/
                    if (srcBmp.getWidth() >= srcBmp.getHeight()){
                        dstBmp = Bitmap.createBitmap(
                                srcBmp,
                                srcBmp.getWidth()/2 - srcBmp.getHeight()/2,
                                0,
                                srcBmp.getHeight(),
                                srcBmp.getHeight()
                        );

                    }else{

                        dstBmp = Bitmap.createBitmap(
                                srcBmp,
                                0,
                                srcBmp.getHeight()/2 - srcBmp.getWidth()/2,
                                srcBmp.getWidth(),
                                srcBmp.getWidth()
                        );
                    }
                    newImage.setPhoto(dstBmp);
                    mFacebookImages.add(newImage);
                }

                Log.d(LOGGEYPOOES, "Should have a new Object of Images");
            }
            catch (Exception e){
                Log.d(LOGGEYPOOES, "Parsing Error inside FacebookParserClass " + e );
            }
            return mFacebookImages;

        }
    }
    /** Async mImageView Loader */



    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Lifestyle", "On Resume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Lifestyle", "On Pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageView = null;
        Log.d("Lifestyle", "On Destroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Lifestyle", "On Stop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Lifestyle", "On Restart");
    }


    /** Bitmap Processing Methods */

    private void setupBitmapOptions(int resourceID)

    {
        options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(getResources(), resourceID, options);
        imageHeight = options.outHeight;
        imageWidth = options.outWidth;
        imageType = options.outMimeType;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus){
        int mImageViewWidth=mImageView.getWidth();
        int mImageViewHeight=mImageView.getHeight();
        Log.d(LOGGEYPOOES,"mImageview Height = " + mImageViewHeight + " mImageView Width = " + mImageViewWidth);
    }
}


