package com.blueribbondivers.puertogaleradivesites;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by jonathan on 30/06/15.
 */
public class DiveSiteListFragment extends ListFragment {
    private ArrayList<Divesite> mDivesites;
    private final String TAG = "DiveSiteListFragment";
    private static String APP_TAG = "tag";


    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.dive_site_title);

        //mDivesites = new ArrayList<Divesite>();
        //mDivesites = parse();
        mDivesites = DiveSites.get(getActivity()).getDivesites();
        DiveSiteAdaptor adapter = new DiveSiteAdaptor(mDivesites);
        setListAdapter(adapter);

    }
/*
    private ArrayList parse(){
        Log.d(TAG, "ArrayList being parsed");
        ArrayList<Divesite> diveSiteArray = new ArrayList<Divesite>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            InputStream raw = DiveSitesActivity.mContext.getResources().openRawResource(R.raw.divesites);
            xpp.setInput(raw,"utf-8");
            Divesite divesite = new Divesite();

            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                String latitude = "latitude";
                String longitude = "longitude";
                String maxDepth = "maxDepth";
                String name = "name";
                String photo = "photo";
                String siteDescription = "siteDescription";
                String travelTime = "travelTime";
                String youtube = "youtube";
                String text;

                if (xpp.getName().equalsIgnoreCase(latitude)) {text = xpp.nextText();
                    divesite = new Divesite();
                    divesite.setLatitude(text);
                }
                if (xpp.getName().equalsIgnoreCase(longitude)) {text = xpp.nextText();
                    divesite.setLongitude(text);
                }
                if (xpp.getName().equalsIgnoreCase(maxDepth)) {text = xpp.nextText();
                    divesite.setMaxDepth(text);
                }
                if (xpp.getName().equalsIgnoreCase(name)) {text = xpp.nextText();
                    divesite.setName(text);
                }
                if (xpp.getName().equalsIgnoreCase(photo)) {text = xpp.nextText();
                    divesite.setPhoto(text);
                }
                if (xpp.getName().equalsIgnoreCase(siteDescription)) {text = xpp.nextText();
                    divesite.setSiteDescription(text);
                }
                if (xpp.getName().equalsIgnoreCase(travelTime)) {text = xpp.nextText();
                    divesite.setTravelTime(text);
                }
                if (xpp.getName().equalsIgnoreCase(youtube)) {text = xpp.nextText();
                    divesite.setYoutube(text);
                    diveSiteArray.add(divesite);
                }

                xpp.next();
            }
        }catch(Exception e) {
            Log.e(APP_TAG, e.getMessage());
        }
        return  diveSiteArray;

    }
*/
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Divesite c = ((DiveSiteAdaptor)getListAdapter()).getItem(position);
        Intent i = new Intent(getActivity(), DiveSitesActivity.class);
        i.putExtra(DivesiteFragment.EXTRA_SITE_ID, c.getId());
        startActivity(i);
    }

    private class DiveSiteAdaptor extends ArrayAdapter<Divesite> {
        public DiveSiteAdaptor(ArrayList<Divesite> diveSites) {
            super(getActivity(), 0, diveSites);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // If we weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_divesite, null);
            }
            // Configure the view for this Crime
            Divesite c = getItem(position);
            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id.divesite_titleTextView);
            titleTextView.setText(c.getName());
            TextView depthTextView =
                    (TextView)convertView.findViewById(R.id.divesite_depthTextView);
            depthTextView.setText(c.getMaxDepth());
            return convertView;
        }
    }


}








