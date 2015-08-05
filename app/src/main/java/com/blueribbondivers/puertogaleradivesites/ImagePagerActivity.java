package com.blueribbondivers.puertogaleradivesites;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by jon on 8/4/15.
 */
public class ImagePagerActivity extends Activity {
    private DiveSites mDiveSites;
    private ViewPager mViewPager;
    private SlideshowImagePagerAdaptor mSlideshowImagePagerAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_imageviewer_activity);
        mSlideshowImagePagerAdaptor = new SlideshowImagePagerAdaptor(this);

        mViewPager = (ViewPager) findViewById(R.id.imageview_Pager);
        mViewPager.setAdapter(mSlideshowImagePagerAdaptor);
    }
}
