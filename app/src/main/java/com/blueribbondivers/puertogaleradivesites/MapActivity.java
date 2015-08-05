package com.blueribbondivers.puertogaleradivesites;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by jonathan on 21/07/15.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private ArrayList<Divesite> diveSites;
    private DiveSites diveSitesClass;
    private UserDetails userDetails;
    private static String url_get_location = "http://design-logic.net/blueribbonapp/locate_facebookid.php?facebook_id=";
    public static final String LOGGEYPOOES = "LOGGEYPOOES";
    private RequestQueue queue;
    private ArrayList<MarkerOptions> markerOptionsArrayList;
    private ArrayList<MarkerOptions> markerOptionsArrayBuffer;
    public GoogleMap mapp;

    Timer timer;
    TimerTask timerTask;

    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        markerOptionsArrayList = new ArrayList();
        markerOptionsArrayBuffer = new ArrayList();
        diveSitesClass = DiveSites.get(getApplicationContext());
        diveSites = diveSitesClass.getDivesites();
        userDetails = UserDetails.getUserDetails();
        setContentView(R.layout.map_activity);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);
        queue = Volley.newRequestQueue(this);
        queue.getCache().clear();
        mapp = mapFragment.getMap();


// Request a string response from the provided URL.


    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        LatLng centerOfPg = new LatLng(13.516, 120.974);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerOfPg, 13));
        updateDiveSites();

    }

    public void updateDiveSites() {
        for (int i = 0; i < diveSites.size(); i++) {
            LatLng diveSite = new LatLng(Double.parseDouble(diveSites.get(i).getLatitude()), Double.parseDouble(diveSites.get(i).getLongitude()));

            mapp.addMarker(new MarkerOptions()
                    .title(diveSites.get(i).getName())
                    .snippet(diveSites.get(i).getMaxDepth())
                    .position(diveSite));
        }
        mapp.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int siteID = 0;
                for (int i = 0; i < diveSites.size(); i++) {
                    String name = marker.getTitle();

                    if (diveSites.get(i).getName().equals(name)) siteID = i;

                }
                Divesite c = diveSites.get(siteID);

                // Start CrimePagerActivity with this crime
                Intent i = new Intent(getApplicationContext(), DivesiteActivity.class);
                i.putExtra(DivesiteActivity.EXTRA_SITE_ID, c.getId());
                startActivity(i);


            }
        });
    }

    private void updateFriends() {
        markerOptionsArrayBuffer.clear();
        for (int i = 0; i < userDetails.getFriendListSize(); i++) {

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url_get_location + userDetails.getFriendlist().get(i),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(LOGGEYPOOES, "Response is: " + response);
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray productArray = jsonObject.getJSONArray("product");
                                JSONObject locationObject = productArray.getJSONObject(0);
                                String longitude = locationObject.getString("longitude");
                                String latitude = locationObject.getString("latitude");
                                String name = locationObject.getString("name");
                                MarkerOptions markerOptions = new MarkerOptions()
                                        .title(name)
                                        .position(new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude)))
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                                markerOptionsArrayBuffer.add(markerOptions);
                            } catch (Exception e) {
                                Log.d(LOGGEYPOOES, "Friend Array Error somewhere " + e);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(LOGGEYPOOES, "That didn't work!");
                }
            });
            queue.add(stringRequest);
            queue.getCache().clear();


        }
    }

    public void addFriendsToMap() {
        if (markerOptionsArrayBuffer.size() == userDetails.getFriendListSize()) {
            markerOptionsArrayList.clear();

            for (int i = 0; i < markerOptionsArrayBuffer.size(); i++)
            {
                markerOptionsArrayList.add(markerOptionsArrayBuffer.get(i));
                mapp.addMarker(markerOptionsArrayList.get(i));
            }
        }

        if (markerOptionsArrayBuffer.size() < userDetails.getFriendListSize())
        {
            for (int i=0; i < markerOptionsArrayBuffer.size();i++)
            {
                String nameAtIBuffer = markerOptionsArrayBuffer.get(i).getTitle();
                String nameAtArrayList = markerOptionsArrayList.get(i).getTitle();
                if (nameAtArrayList.equals(nameAtIBuffer))
                {
                    markerOptionsArrayList.remove(i);
                    markerOptionsArrayList.add(i,markerOptionsArrayBuffer.get(i));
                }
            }

            for (int i = 0; i<markerOptionsArrayList.size();i++)
            {
                mapp.addMarker(markerOptionsArrayList.get(i));
            }
        }

    }



    @Override
    protected void onResume() {
        super.onResume();

        //onResume we start our timer so it can start when the app comes from the background
        startTimer();
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 5000, 10000); //
    }

    public void stoptimertask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        mapp.clear();
                        addFriendsToMap();
                        updateDiveSites();
                        markerOptionsArrayList.clear();
                        updateFriends();

                    }
                });
            }
        };
    }


    }

