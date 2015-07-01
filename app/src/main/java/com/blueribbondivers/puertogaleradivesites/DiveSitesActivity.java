package com.blueribbondivers.puertogaleradivesites;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by jonathan on 30/06/15.
 */
public class DiveSitesActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new DivesiteFragment();
    }


}
