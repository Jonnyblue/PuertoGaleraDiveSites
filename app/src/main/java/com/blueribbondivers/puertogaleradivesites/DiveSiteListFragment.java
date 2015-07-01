package com.blueribbondivers.puertogaleradivesites;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

        mDivesites = new ArrayList<Divesite>();
        mDivesites = parse();
        ArrayAdapter<Divesite> adapter = new ArrayAdapter<Divesite>(getActivity(),android.R.layout.simple_list_item_1,mDivesites);
        setListAdapter(adapter);

    }

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

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Divesite c = (Divesite)(getListAdapter()).getItem(position);
        Log.d(TAG, c.getTitle() + " was clicked");
    }
}






