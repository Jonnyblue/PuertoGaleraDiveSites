package com.blueribbondivers.puertogaleradivesites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

/**
 * Created by jonathan on 15/07/15.
 */
public class WeatherWebviewActivity extends AppCompatActivity {
    private Context mContext;
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getApplicationContext();
        /** Load Wind Guru Data */
        setContentView(R.layout.tides_waves_typhoon_webview);
        WebView windGuruWebview = (WebView) findViewById(R.id.webView);
        windGuruWebview.getSettings().setJavaScriptEnabled(true);
        windGuruWebview.loadUrl("file:///android_res/raw/windguru.html");

        /** Start Parsing Tidal Data */

        /** Here is better date http://tides.mobilegeographics.com/locations/1103.html?y=2015&m=7&d=17 */
        Date currentDate = new Date();
        DateFormat yearF = new SimpleDateFormat("yyyy");
        DateFormat monthF = new SimpleDateFormat("M");
        DateFormat dayF = new SimpleDateFormat("d");
        String year = yearF.format(currentDate);
        String month = monthF.format(currentDate);
        String day = dayF.format(currentDate);

        String url = "http://tides.mobilegeographics.com/locations/1103.html?y=" + year + "&m=" + month + "&d=" + day;

      //  String siteUrl ="http://tides.mobilegeographics.com/locations/1103.html";
        (new ParseURL() ).execute(new String[]{url});

    }

    public void updateGridView(ArrayList<TidalData> tidalArray)
    {
        TidalData tidalData = new TidalData();
        TidalTableFragment tidalDataFragment = new TidalTableFragment();
        ArrayList<ArrayList <TidalData>> tidalDayArrayList = new ArrayList();
        ArrayList<TidalData> dayTidalData = new ArrayList();

        /** Make an arraylist per day for the tableData */
        for (int i=0;i<tidalArray.size();i++)
        {
            if (i==0){dayTidalData.add(tidalArray.get(i));}

            if (i>0)
            {
                Calendar tidal1 = Calendar.getInstance();
                Date date1;
                date1 = tidalArray.get(i).getDate();
                tidal1.setTime(date1);
                int day1 = tidal1.get(Calendar.DAY_OF_MONTH);
                Date date2;
                date2 = tidalArray.get(i-1).getDate();
                tidal1.setTime(date2);
                int day2 = tidal1.get(Calendar.DAY_OF_MONTH);
                if (day1 == day2) {
                    dayTidalData.add(tidalArray.get(i));}
                else {
                    tidalDayArrayList.add(dayTidalData);
                    dayTidalData = new ArrayList();
                    dayTidalData.add(tidalArray.get(i));
                }
            }
        }
        Log.d("JSwa", "TableDataArrayList sorted to days");

        /** First TableView Fragment, Probably only has a few entries*/
        TidalTableFragment firstday = new TidalTableFragment();
        DateFormat df = new SimpleDateFormat("EEE dd MMM");
        DateFormat tf = new SimpleDateFormat("kk:mm");
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        String x;
        for (int i = 0; i<tidalDayArrayList.size();i++) {
            int count = tidalDayArrayList.get(i).size();

            switch (count) {
                case 2:
                    firstday = new TidalTableFragment();


                    firstday.setDate(df.format(tidalDayArrayList.get(i).get(0).getDate()));

                    firstday.setHeight1Text(" ");
                    firstday.setHeight2Text(" ");

                    x = Float.toString(tidalDayArrayList.get(i).get(0).getTidalHeight());
                    firstday.setHeight3Text(x + "m");
                    x = Float.toString(tidalDayArrayList.get(i).get(1).getTidalHeight());
                    firstday.setHeight4Text(x + "m");

                    firstday.setTime1Text(" ");
                    firstday.setTime2Text(" ");


                    firstday.setTime3Text(tf.format(tidalDayArrayList.get(i).get(0).getDate()));
                    firstday.setTime4Text(tf.format(tidalDayArrayList.get(i).get(1).getDate()));

                    transaction = getSupportFragmentManager().beginTransaction();
                    if ( (i/2)*2 == i )
                        transaction.add(R.id.left_tidal_table, firstday, "table" + i).commit();
                    else
                        transaction.add(R.id.right_tidal_table, firstday, "table" + i).commit();

                    break;

                case 4:

                    firstday = new TidalTableFragment();

                    firstday.setDate(df.format(tidalDayArrayList.get(i).get(0).getDate()));

                    x = Float.toString(tidalDayArrayList.get(i).get(0).getTidalHeight());
                    firstday.setHeight1Text(x + "m");
                    x = Float.toString(tidalDayArrayList.get(i).get(1).getTidalHeight());
                    firstday.setHeight2Text(x + "m");
                    x = Float.toString(tidalDayArrayList.get(i).get(2).getTidalHeight());
                    firstday.setHeight3Text(x + "m");
                    x = Float.toString(tidalDayArrayList.get(i).get(3).getTidalHeight());
                    firstday.setHeight4Text(x + "m");

                    firstday.setTime1Text(tf.format(tidalDayArrayList.get(i).get(0).getDate()));
                    firstday.setTime2Text(tf.format(tidalDayArrayList.get(i).get(1).getDate()));
                    firstday.setTime3Text(tf.format(tidalDayArrayList.get(i).get(2).getDate()));
                    firstday.setTime4Text(tf.format(tidalDayArrayList.get(i).get(3).getDate()));

                    transaction = getSupportFragmentManager().beginTransaction();
                    if ( (i/2)*2 == i )
                        transaction.add(R.id.left_tidal_table, firstday, "table" + i).commit();
                    else
                        transaction.add(R.id.right_tidal_table, firstday, "table" + i).commit();
                    break;

                case 3:

                    firstday = new TidalTableFragment();

                    firstday.setDate(df.format(tidalDayArrayList.get(i).get(0).getDate()));

                    x = Float.toString(tidalDayArrayList.get(i).get(0).getTidalHeight());
                    firstday.setHeight1Text(x + "m");
                    x = Float.toString(tidalDayArrayList.get(i).get(1).getTidalHeight());
                    firstday.setHeight2Text(x + "m");
                    x = Float.toString(tidalDayArrayList.get(i).get(2).getTidalHeight());
                    firstday.setHeight3Text(x + "m");
                    firstday.setHeight4Text(" ");

                    firstday.setTime1Text(tf.format(tidalDayArrayList.get(i).get(0).getDate()));
                    firstday.setTime2Text(tf.format(tidalDayArrayList.get(i).get(1).getDate()));
                    firstday.setTime3Text(tf.format(tidalDayArrayList.get(i).get(2).getDate()));
                    firstday.setTime4Text(" ");

                    transaction = getSupportFragmentManager().beginTransaction();
                    if ( (i/2)*2 == i )
                        transaction.add(R.id.left_tidal_table, firstday, "table" + i).commit();
                    else
                        transaction.add(R.id.right_tidal_table, firstday, "table" + i).commit();
                    break;

                default:
                    Log.d("JSwa", "Default Case");

            }
        }

    }

    /** Parse Tidal Data and Return it from*/
    class ParseURL extends AsyncTask<String,Void,Void > {
        String tidalData;

        @Override
        protected Void doInBackground(String... strings) {
            StringBuffer buffer = new StringBuffer();
            try {
                Log.d("JSwa", "Connecting to [" + strings[0] + "]");
                Document doc = Jsoup.connect(strings[0]).get();
                Log.d("JSwa", "Connected to [" + strings[0] + "]");

                Element predictionsTable = doc.select("pre").first();
                tidalData = predictionsTable.text();
                Log.d("JSwa",tidalData);
            } catch (Throwable t) {
                t.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            tidalDataToObject(tidalData);
        }
    }

    private void tidalDataToObject(String tidalData){
        String[] lines = tidalData.split(System.getProperty("line.separator"));
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd EEE h:mm a", Locale.ENGLISH);
        ArrayList<TidalData> tidalDataArrayList = new ArrayList<>();
        for (int i=3;i<lines.length;i++)
        {
            TidalData tidalData1 = new TidalData();
            String line = lines[i];
            String[] dateString = line.split(" PHT ");
            try{
                Boolean sunsetOrSunrise = false;
                Date date = df.parse(dateString[0]);
                Log.d("JSwa", date.toString());

                Scanner scanner = new Scanner(dateString[1]);
                if (scanner.hasNext("Sunset")) sunsetOrSunrise = true;
                if (scanner.hasNext("Sunrise")) sunsetOrSunrise = true;
                if (scanner.hasNext("First")) sunsetOrSunrise = true;
                if (scanner.hasNext("Last")) sunsetOrSunrise = true;
                if (scanner.hasNext("New")) sunsetOrSunrise = true;
                if (scanner.hasNext("Full")) sunsetOrSunrise = true;
                if (scanner.hasNextFloat()) {
                    tidalData1.setTidalHeight(scanner.nextFloat());
                    sunsetOrSunrise = false;
                    tidalData1.setDate(date);
                }
                if (sunsetOrSunrise == false){
                    tidalDataArrayList.add(tidalData1);
                }
            }
            catch (Exception e){
                Log.d("JSwa", "Fucked up parsing date" + e);
            }

            Log.d("JSwa", "Tidal Data parsed");

        }
        updateGraphView(tidalDataArrayList);
        updateGridView(tidalDataArrayList);
    }

    class TidalData{
        private Date mDate;
        private Float tidalHeight;

        public TidalData()
        {

        }

        public Date getDate() {
            return mDate;
        }

        public void setDate(Date date) {
            mDate = date;
        }

        public Float getTidalHeight() {
            return tidalHeight;
        }

        public void setTidalHeight(Float tidalHeight) {
            this.tidalHeight = tidalHeight;
        }
    }

    private void updateGraphView(ArrayList<TidalData> tidalDataArray)
    {
        graph = (GraphView) findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries();
        for (int i = 0; i<tidalDataArray.size(); i++)
        {
            DataPoint data = new DataPoint(tidalDataArray.get(i).getDate(),tidalDataArray.get(i).getTidalHeight());

            series.appendData(data,true,50);
        }
        series.setColor(Color.BLUE);
        series.setDrawDataPoints(true);
        series.setDataPointsRadius(10);
        series.setThickness(4);

        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#303F9F"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        //paint.setPathEffect(new DashPathEffect(new float[]{8, 5}, 0));
        series.setCustomPaint(paint);


        graph.addSeries(series);
        DateFormat df = new SimpleDateFormat("EEE d");
        graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, df));
        graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        graph.getGridLabelRenderer().setNumVerticalLabels(7);
        graph.getViewport().setMinX(tidalDataArray.get(0).getDate().getTime());
        graph.getViewport().setMaxX(tidalDataArray.get(tidalDataArray.size() - 1).getDate().getTime());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinY(-.5);
        graph.getViewport().setMaxY(2.5);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setBackgroundColor(Color.parseColor("#E8EAF6"));
        graph.setTitle("Cebu Tides - Please subtract 20mins");
        graph.setTitleColor(Color.parseColor("#303F9F"));
        graph.setVisibility(View.VISIBLE);

    }
    }


