package com.blueribbondivers.puertogaleradivesites;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jonathan on 30/06/15.
 */
public class DiveSites {
    private static DiveSites sCrimeLab;
    private Context mAppContext;
    private ArrayList<Divesite> mDivesites;
    private final String TAG = "DiveSites";
    private static String APP_TAG = "tag";
    private DiveSites(Context appContext)
    {
        mAppContext = appContext;
        mDivesites = new ArrayList();
        mDivesites = parse();

    }

    public ArrayList<Divesite> getDivesites() {
        return mDivesites;
    }

    public Divesite getDiveSite(UUID id) {
        for (Divesite c : mDivesites) {
            if (c.getId().equals(id))
                return c;
        }
        return null;
    }
    public static DiveSites get(Context c)
    {
        if (sCrimeLab == null)
        {
            sCrimeLab = new DiveSites(c.getApplicationContext());
        }
        return sCrimeLab;
    }
    private ArrayList parse(){
        Log.d(TAG, "ArrayList being parsed");
        ArrayList<Divesite> diveSiteArray = new ArrayList();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            InputStream raw = mAppContext.getResources().openRawResource(R.raw.divesites);
            xpp.setInput(raw,"utf-8");
            Divesite divesite = new Divesite();

            while (xpp.next() != XmlPullParser.END_DOCUMENT) {
                String latitude = "latitude";
                String longitude = "longitude";
                String maxDepth = "maxDepth";
                String name = "name";
                String youtubealbumid = "youtubealbumid";
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

                if (xpp.getName().equalsIgnoreCase(youtubealbumid)) {text = xpp.nextText();
                    divesite.setFacebookAlbumID(text);
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
}
