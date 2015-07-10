package com.blueribbondivers.puertogaleradivesites;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by jonathan on 05/07/15.
 */
public class DivesitePagerActivity extends FragmentActivity {

    private ViewPager mViewPager;
    private ArrayList<Divesite> mDivesites;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.viewPager);
        setContentView(mViewPager);

        mDivesites = DiveSites.get(this).getDivesites();
        FragmentManager fm = getSupportFragmentManager();
        UUID divesiteID = (UUID)getIntent()
                .getSerializableExtra(DivesiteFragment.EXTRA_SITE_ID);
        for (int i = 0; i < mDivesites.size(); i++) {
            if (mDivesites.get(i).getId().equals(divesiteID)) {
                //mViewPager.setCurrentItem(i);
                position = i;
                break;}
        }
        mViewPager.postDelayed(new Runnable() {

            @Override
            public void run() {
                mViewPager.setCurrentItem(position);
            }
        }, 200);




        mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
            @Override
            public int getCount() {
                return mDivesites.size();
            }


            @Override
            public Fragment getItem(int pos) {
                Divesite divesite = mDivesites.get(pos);
                return DivesiteFragment.newInstance(divesite.getId());
            }
        }); }
    }

