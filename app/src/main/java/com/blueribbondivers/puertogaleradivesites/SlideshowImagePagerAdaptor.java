package com.blueribbondivers.puertogaleradivesites;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by jon on 8/4/15.
 */
public class SlideshowImagePagerAdaptor extends PagerAdapter {
    private ArrayList<FacebookImageFromGraph> mFacebookImages;
    Context mContext;
    LayoutInflater mLayoutInflater;
    private String tagTapped;
    int firstImage = 0;
    public ArrayList<FacebookImageFromGraph> getmFacebookImages() {
        return mFacebookImages;
    }

    public void setmFacebookImages(ArrayList<FacebookImageFromGraph> mFacebookImages) {
        this.mFacebookImages = mFacebookImages;
    }

    public SlideshowImagePagerAdaptor(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return DiveSites.get(mContext).getmFacebookImages().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        tagTapped= DiveSites.get(mContext).getFragmentImageTapped();
        int tapped = Integer.parseInt(tagTapped);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);

        mFacebookImages = DiveSites.get(mContext).getmFacebookImages();

        if (firstImage == 0) imageView.setImageBitmap(mFacebookImages.get(tapped).getPhoto());
        else imageView.setImageBitmap(mFacebookImages.get(position).getPhoto());
        firstImage++;
        
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}
