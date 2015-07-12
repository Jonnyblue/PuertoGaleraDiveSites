package com.blueribbondivers.puertogaleradivesites;


import android.support.v4.app.Fragment;

import com.facebook.appevents.AppEventsLogger;

public class DiveSiteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DiveSiteListFragment();
    }




}
