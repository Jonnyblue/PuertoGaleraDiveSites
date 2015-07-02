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

        mDivesites = DiveSites.get(getActivity()).getDivesites();
        DiveSiteAdaptor adapter = new DiveSiteAdaptor(mDivesites);
        setListAdapter(adapter);

    }

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








