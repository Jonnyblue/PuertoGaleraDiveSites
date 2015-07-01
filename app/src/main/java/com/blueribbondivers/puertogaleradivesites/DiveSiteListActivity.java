package com.blueribbondivers.puertogaleradivesites;


import android.support.v4.app.Fragment;

public class DiveSiteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DiveSiteListFragment();
    }


}
