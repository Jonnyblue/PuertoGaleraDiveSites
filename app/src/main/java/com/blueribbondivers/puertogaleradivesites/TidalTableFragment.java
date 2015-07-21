package com.blueribbondivers.puertogaleradivesites;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by jonathan on 17/07/15.
 */
public class TidalTableFragment extends Fragment {

    private TextView mDateTextView;
    private TextView mTime1TextView;
    private TextView mTime2TextView;
    private TextView mTime3TextView;
    private TextView mTime4TextView;
    private TextView mHeight1TextView;
    private TextView mHeight2TextView;
    private TextView mHeight3TextView;
    private TextView mHeight4TextView;

    private String mDate;
    private String mTime1Text;
    private String mTime2Text;
    private String mTime3Text;
    private String mTime4Text;
    private String mHeight1Text;
    private String mHeight2Text;
    private String mHeight3Text;
    private String mHeight4Text;

    public String getDate() {
        return mDate;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public String getHeight1Text() {
        return mHeight1Text;
    }

    public void setHeight1Text(String height1Text) {
        mHeight1Text = height1Text;
    }

    public String getHeight2Text() {
        return mHeight2Text;
    }

    public void setHeight2Text(String height2Text) {
        mHeight2Text = height2Text;
    }

    public String getHeight3Text() {
        return mHeight3Text;
    }

    public void setHeight3Text(String height3Text) {
        mHeight3Text = height3Text;
    }

    public String getHeight4Text() {
        return mHeight4Text;
    }

    public void setHeight4Text(String height4Text) {
        mHeight4Text = height4Text;
    }

    public String getTime1Text() {
        return mTime1Text;
    }

    public void setTime1Text(String time1Text) {
        mTime1Text = time1Text;
    }

    public String getTime2Text() {
        return mTime2Text;
    }

    public void setTime2Text(String time2Text) {
        mTime2Text = time2Text;
    }

    public String getTime3Text() {
        return mTime3Text;
    }

    public void setTime3Text(String time3Text) {
        mTime3Text = time3Text;
    }

    public String getTime4Text() {
        return mTime4Text;
    }

    public void setTime4Text(String time4Text) {
        mTime4Text = time4Text;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tide_table_layout,container,false);

        mDateTextView = (TextView)v.findViewById(R.id.table_date_text_view);
        mDateTextView.setText(mDate);
        mTime1TextView = (TextView)v.findViewById(R.id.table_time_text_view_1);
        mTime1TextView.setText(mTime1Text);
        mTime2TextView = (TextView)v.findViewById(R.id.table_time_text_view_2);
        mTime2TextView.setText(mTime2Text);
        mTime3TextView = (TextView)v.findViewById(R.id.table_time_text_view_3);
        mTime3TextView.setText(mTime3Text);
        mTime4TextView = (TextView)v.findViewById(R.id.table_time_text_view_4);
        mTime4TextView.setText(mTime4Text);
        mHeight1TextView = (TextView)v.findViewById(R.id.table_height_text_view_1);
        mHeight1TextView.setText(mHeight1Text);
        mHeight2TextView = (TextView)v.findViewById(R.id.table_height_text_view_2);
        mHeight2TextView.setText(mHeight2Text);
        mHeight3TextView = (TextView)v.findViewById(R.id.table_height_text_view_3);
        mHeight3TextView.setText(mHeight3Text);
        mHeight4TextView = (TextView)v.findViewById(R.id.table_height_text_view_4);
        mHeight4TextView.setText(mHeight4Text);
        return v;
    }
}
