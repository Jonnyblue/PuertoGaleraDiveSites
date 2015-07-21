package com.blueribbondivers.puertogaleradivesites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by jonathan on 21/07/15.
 */
public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private   ArrayList<Divesite> diveSites;
    private DiveSites diveSitesClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        diveSitesClass = DiveSites.get(getApplicationContext());
        diveSites = diveSitesClass.getDivesites();
        setContentView(R.layout.map_activity);
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        for (int i=0; i< diveSites.size();i++) {
            LatLng diveSite = new LatLng(Double.parseDouble(diveSites.get(i).getLatitude()), Double.parseDouble(diveSites.get(i).getLongitude()));
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(diveSite, 14));
            map.addMarker(new MarkerOptions()
                    .title(diveSites.get(i).getName())
                    .snippet(diveSites.get(i).getMaxDepth())
                    .position(diveSite));
        }
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                int siteID = 0;
                for (int i=0;i<diveSites.size();i++) {
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


    }

