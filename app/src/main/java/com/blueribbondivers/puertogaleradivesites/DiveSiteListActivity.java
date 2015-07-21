package com.blueribbondivers.puertogaleradivesites;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.MenuItem;


public class DiveSiteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new DiveSiteListFragment();
    }






}
