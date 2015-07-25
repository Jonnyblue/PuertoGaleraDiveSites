package com.blueribbondivers.puertogaleradivesites;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Created by jonathan on 12/07/15.
 */
public class DivesiteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MyLocationListener mylistener;

    private String provider;
    private LocationManager locationManager;
    private Criteria criteria;
    private UserDetails newUser;
    private Divesite mDivesite;
    private TextView mDepthField;
    private TextView mSiteDescription;
    private TextView mDistance;
    private LinearLayout mScrollview;
    private ImageView mThumbnailView;
    private ImageView mImageView;
    private GoogleMap mMap;
    private ProgressDialog pDialog;
    private Boolean facebookLoggedIn;
    public static final String LOGGEYPOOES = "LOGGEYPOOES";
    public static final String EXTRA_SITE_ID = "com.blueribbondivers.extraSiteID";

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }

    FacebookParser photoparser;
    private Context mContext;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FrameLayout videoLayout;
    private String accessToken;
    private ArrayList<FacebookImageFromGraph> mFacebookImages;
    private JSONObject faceBookResponse;
    JSONParser jsonParser;
    JSONParser locationJsonParser;
    // url to create new product
    private static String url_create_user= "http://www.design-logic.net/blueribbonapp/create_user.php";
    private static String url_update_user_location= "http://www.design-logic.net/blueribbonapp/update_user_location.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";


    @Override
    public void onMapReady(GoogleMap map) {
        LatLng diveSiteLocation = new LatLng(Double.parseDouble(mDivesite.getLatitude()), Double.parseDouble(mDivesite.getLongitude()));
        map.setMyLocationEnabled(true);

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.addMarker(new MarkerOptions()
                .title(mDivesite.getName())
                .snippet(mDivesite.getMaxDepth())
                .position(diveSiteLocation));

        LatLng centerOfPg = new LatLng(13.516, 120.974);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerOfPg, 13));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.divesite_activity);
        UUID crimeId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_SITE_ID);

        mDivesite = DiveSites.get(this).getDiveSite(crimeId);
        mContext = getApplicationContext();
        setTitle(mDivesite.getTitle());
        mScrollview = (LinearLayout) findViewById(R.id.horizontal_thumbnail_view);

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                accessToken = AccessToken.getCurrentAccessToken().getToken();
                //Toast.makeText(getApplicationContext(), "Facebook is connected " + accessToken, Toast.LENGTH_LONG).show();


            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Facebook connection Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "Facebook did not Connect " + exception, Toast.LENGTH_LONG).show();
            }
        });

        Resources resources = mContext.getResources();

        VideoFragment f = VideoFragment.newInstance(mDivesite.getYoutube());
        getFragmentManager().beginTransaction().replace(R.id.videofragment, f).commit();


        mDepthField = (TextView) findViewById(R.id.divesite_display_depth);
        mDepthField.setText(mDivesite.getMaxDepth());
        mSiteDescription = (TextView) findViewById(R.id.divesite_display_description);
        mSiteDescription.setText(mDivesite.getSiteDescription());
        mDistance = (TextView) findViewById(R.id.divesite_display_distance);
        mDistance.setText(mDivesite.getTravelTime());
        if (mImageView != null) {
            ((BitmapDrawable) mImageView.getDrawable()).getBitmap().recycle();
        }
        mImageView = (ImageView) findViewById(R.id.divesite_display_imageView);
        String imageName = mDivesite.getPhoto();
        /** Get Resource ID for mImageView */
        int resourceID = resources.getIdentifier(imageName, "drawable", mContext.getPackageName());
        /** Get Bitmap Options Setup for mImageView */
        Drawable myDrawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            myDrawable = mContext.getResources().getDrawable(resourceID, mContext.getTheme());
        } else {
            myDrawable = mContext.getResources().getDrawable(resourceID);
        }
        mImageView.setImageDrawable(myDrawable);

        videoLayout = (FrameLayout) findViewById(R.id.videofragment);
        Boolean internetConnected = this.isOnline();

        if (internetConnected) {
            parseFacebookUserData();
            //Toast.makeText(getApplicationContext(), "Network Is Connected", Toast.LENGTH_LONG).show();
            loginButton.setVisibility(View.VISIBLE);
            mImageView.setVisibility(View.GONE);
            videoLayout.setVisibility(View.VISIBLE);

            /** Start the Facebook Album ID Parser
             Bundle parameters = new Bundle();
             parameters.putString("fields", "picture,images,name");
             new GraphRequest(

             AccessToken.getCurrentAccessToken(),
             mDivesite.getFacebookAlbumID() + "/photos",
             parameters,
             HttpMethod.GET,
             new GraphRequest.Callback() {
             public void onCompleted(GraphResponse response) {
             faceBookResponse = response.getJSONObject();

             photoparser = new FacebookParser();
             photoparser.execute(faceBookResponse);
             }
             }
             ).executeAsync();

             */

            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);


        } else {
            Toast.makeText(getApplicationContext(), "Network Is NOT Connected", Toast.LENGTH_LONG).show();
            loginButton.setVisibility(View.GONE);
            mImageView.setVisibility(View.VISIBLE);
            videoLayout.setVisibility(View.GONE);
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);

        }
    }

    private void parseFacebookUserData() {
        Bundle userParameters = new Bundle();
        userParameters.putString("fields", "id,name,email,first_name,gender,last_name,location");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                userParameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject userJsonObject = response.getJSONObject();
                        newUser = new UserDetails();
                        try {
                            if (userJsonObject.getString("id") != null) {
                                JSONObject locationObject = userJsonObject.getJSONObject("location");
                                newUser.setName(userJsonObject.getString("name"));
                                newUser.setFacebookID(userJsonObject.getString("id"));
                                newUser.setFirst_name(userJsonObject.getString("first_name"));
                                newUser.setLast_name(userJsonObject.getString("last_name"));
                                newUser.setEmail(userJsonObject.getString("email"));
                                newUser.setGender(userJsonObject.getString("gender"));
                                newUser.setLocationID(locationObject.getString("id"));
                                newUser.setLocationName(locationObject.getString("name"));
                                //new CreateNewUser().execute();
                                //startLocationService();


                            }
                        } catch (Exception e) {
                            Log.d(LOGGEYPOOES, "Something fucked up with Facebook User Parsing");

                        }
                        new PostUserAsync().execute();

                        Log.d(LOGGEYPOOES, "Breakpoint");
                    }
                }
        ).executeAsync();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Online Function
     */
    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    /**
     * Async Facebook Parser
     */
    class FacebookParser extends AsyncTask<JSONObject, Void, ArrayList<FacebookImageFromGraph>> {
        private ArrayList<FacebookImageFromGraph> mFacebookImages;
        public static final String LOGGEYPOOES = "LOGGEYPOOES";

        @Override
        protected void onPostExecute(ArrayList<FacebookImageFromGraph> facebookImageFromGraphs) {
            Toast.makeText(getApplicationContext(), "Facebook Parsed?", Toast.LENGTH_LONG).show();
            if (!photoparser.isCancelled()) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                for (int i = mFacebookImages.size() - 1; (i >= 0) && i != (mFacebookImages.size() - 6); i--) {
                    FacebookAlbumImageFragment f = new FacebookAlbumImageFragment();
                    f.setFacebookImageFromGraph(mFacebookImages.get(i));
                    f.setAlbumID(mDivesite.getFacebookAlbumID());
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.add(R.id.horizontal_thumbnail_view, f, "fragment_" + i).commit();
                }

            } else
                Toast.makeText(getApplicationContext(), "Facebook Parser was Cancelled", Toast.LENGTH_LONG).show();

        }

        @Override
        protected ArrayList<FacebookImageFromGraph> doInBackground(JSONObject... params) {

            try {
                JSONObject allPhotoJsonObject = params[0];
                JSONArray dataArray = allPhotoJsonObject.getJSONArray("data");

                mFacebookImages = new ArrayList<>();
                for (int i = 0; i < dataArray.length(); i++) {
                    FacebookImageFromGraph newFacebookImageFromGraph = new FacebookImageFromGraph();
                    JSONObject temp = dataArray.getJSONObject(i);
                    newFacebookImageFromGraph.setPicture(temp.getString("picture"));
                    JSONArray imagesArray = temp.getJSONArray("images");
                    String name = " ";
                    try {
                        name = temp.getString("name");
                    } catch (Exception e) {
                        Log.d(LOGGEYPOOES, "No Comment available " + e);
                    }

                    newFacebookImageFromGraph.setName(name);

                    for (int j = 0; j < imagesArray.length(); j++) {
                        JSONObject imageTemp = imagesArray.getJSONObject(j);
                        String url = imageTemp.getString("source");
                        String height = imageTemp.getString("height");
                        String width = imageTemp.getString("width");
                        FacebookImageSizes imageSizes = new FacebookImageSizes(width, height, url);
                        newFacebookImageFromGraph.addFacebookImageToArrayList(imageSizes);

                    }


                    /** Get not the largest or the smallest of the Bitmaps from the Array, might cause issues later...so far so good*/
                    Bitmap srcBmp = (newFacebookImageFromGraph.getBitmapFromURL(newFacebookImageFromGraph.getImageSizesArrayList().get(newFacebookImageFromGraph.getImageSizesArrayList().size() - 3).getUrl()));
                    Bitmap dstBmp;

                    /** Crop images to make square thumbnails*/
                    if (srcBmp.getWidth() >= srcBmp.getHeight()) {
                        dstBmp = Bitmap.createBitmap(
                                srcBmp,
                                srcBmp.getWidth() / 2 - srcBmp.getHeight() / 2,
                                0,
                                srcBmp.getHeight(),
                                srcBmp.getHeight()
                        );

                    } else {

                        dstBmp = Bitmap.createBitmap(
                                srcBmp,
                                0,
                                srcBmp.getHeight() / 2 - srcBmp.getWidth() / 2,
                                srcBmp.getWidth(),
                                srcBmp.getWidth()
                        );
                    }
                    newFacebookImageFromGraph.setPhoto(dstBmp);
                    mFacebookImages.add(newFacebookImageFromGraph);
                }

                Log.d(LOGGEYPOOES, "Should have a new Object of Images");
            } catch (Exception e) {
                Log.d(LOGGEYPOOES, "Parsing Error inside FacebookParserClass " + e);
            }
            return mFacebookImages;

        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        int mImageViewWidth = mImageView.getWidth();
        int mImageViewHeight = mImageView.getHeight();
        Log.d(LOGGEYPOOES, "mImageview Height = " + mImageViewHeight + " mImageView Width = " + mImageViewWidth);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.divesitemenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.webViewOptionMenu:
                Intent intent = new Intent(this, WeatherWebviewActivity.class);
                startActivity(intent);
                return true;

            case R.id.googleMapButtons:
                Intent mapIntent = new Intent(this, MapActivity.class);
                startActivity(mapIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);


        }

    }

    private void startLocationService() {
        // Get the location manager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setCostAllowed(false);
        // get the best provider depending on the criteria
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        mylistener = new MyLocationListener();

        if (location != null) {
            mylistener.onLocationChanged(location);
        } else {
            // leads to the settings because there is no last known location
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // location updates: at least 1 meter and 200millsecs change
        locationManager.requestLocationUpdates(provider, 20000L, 10.0f, mylistener);
    }

    private class MyLocationListener implements android.location.LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            // Initialize the location fields
            String latitude = String.valueOf(location.getLatitude());
            String longitude = String.valueOf(location.getLongitude());
            newUser.setLongitude(longitude);
            newUser.setLatitude(latitude);
            new UpdateLocationAsync().execute();

            Toast.makeText(DivesiteActivity.this, "Location changed!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Toast.makeText(DivesiteActivity.this, provider + "'s status changed to " + status + "!",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(DivesiteActivity.this, "Provider " + provider + " enabled!",
                    Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(DivesiteActivity.this, "Provider " + provider + " disabled!",
                    Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (photoparser != null) photoparser.cancel(true);
        AppEventsLogger.deactivateApp(this);
    }

    private Boolean isFacebookLoggedIn() {
        if (AccessToken.getCurrentAccessToken() != null)
            return facebookLoggedIn = true;
        else return false;
    }

    class PostUserAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url_create_user);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
                nameValuePairs.add(new BasicNameValuePair("name", newUser.getName()));
                nameValuePairs.add(new BasicNameValuePair("facebook_id", newUser.getFacebookID()));
                nameValuePairs.add(new BasicNameValuePair("email", newUser.getEmail()));
                nameValuePairs.add(new BasicNameValuePair("gender", newUser.getGender()));
                nameValuePairs.add(new BasicNameValuePair("first_name", newUser.getFirst_name()));
                nameValuePairs.add(new BasicNameValuePair("last_name", newUser.getLast_name()));
                nameValuePairs.add(new BasicNameValuePair("location_name", newUser.getLocationName()));
                nameValuePairs.add(new BasicNameValuePair("location_id", newUser.getLocationID()));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                Log.d(LOGGEYPOOES,"Posted..");

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                Log.d(LOGGEYPOOES, e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.d(LOGGEYPOOES, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            startLocationService();

        }
    }

    class UpdateLocationAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url_update_user_location);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(8);
                nameValuePairs.add(new BasicNameValuePair("latitude", newUser.getLatitude()));
                nameValuePairs.add(new BasicNameValuePair("facebook_id", newUser.getFacebookID()));
                nameValuePairs.add(new BasicNameValuePair("longitude", newUser.getLongitude()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                Log.d(LOGGEYPOOES,"Posted..");

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                Log.d(LOGGEYPOOES, e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.d(LOGGEYPOOES, e.toString());
            }
            return null;
        }
    }



}











