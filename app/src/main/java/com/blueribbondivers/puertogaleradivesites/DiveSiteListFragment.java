package com.blueribbondivers.puertogaleradivesites;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jonathan on 30/06/15.
 */
public class DiveSiteListFragment extends ListFragment {
    private ArrayList<Divesite> mDivesites;
    private final String TAG = "DiveSiteListFragment";
    private static String APP_TAG = "tag";
    private Context mContext;



    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        //getActivity().getActionBar().setTitle(Html.fromHtml("<font color=\"#1c3565\">" + getString(R.string.app_name) + "</font>"));

        //getActivity().setTitle(R.string.dive_site_title);

        mDivesites = DiveSites.get(getActivity()).getDivesites();
        DiveSiteAdaptor adapter = new DiveSiteAdaptor(mDivesites);
        setListAdapter(adapter);
        mContext = getActivity().getApplicationContext();

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Divesite c = ((DiveSiteAdaptor)getListAdapter()).getItem(position);
        // Start CrimePagerActivity with this crime
        Intent i = new Intent(getActivity(), DivesitePagerActivity.class);
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
            // Configure the view for this DiveSite
            Resources resources = mContext.getResources();
            Divesite c = getItem(position);
            TextView titleTextView =
                    (TextView)convertView.findViewById(R.id.divesite_titleTextView);
            titleTextView.setText(c.getName());
            TextView depthTextView =
                    (TextView)convertView.findViewById(R.id.divesite_depthTextView);
            depthTextView.setText(c.getMaxDepth());
            ProportionalImageView imageView = (ProportionalImageView) convertView.findViewById(R.id.list_imageView);
            String smallImageName = "s" + c.getPhoto();
            final int resourceID = resources.getIdentifier(smallImageName,"drawable",mContext.getPackageName());
            imageView.setImageResource(resourceID);
            setHasOptionsMenu(true);
            return convertView;
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }
}








