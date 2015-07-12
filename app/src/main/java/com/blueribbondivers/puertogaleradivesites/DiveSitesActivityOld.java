package com.blueribbondivers.puertogaleradivesites;



import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


/**
 * Created by jonathan on 30/06/15.
 */
public class DiveSitesActivityOld extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        //FacebookSdk.sdkInitialize(getApplicationContext());
        return new DivesiteFragment();
    }



}
