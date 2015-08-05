package com.blueribbondivers.puertogaleradivesites;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by jonathan on 26/07/15.
 */
public class LandingActivity extends Activity {

    private AccessTokenTracker accessTokenTracker;
    private UserDetails newUser;
    private TextView puertoGalera;
    private Context mContext;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private String accessToken;
    private Button skipButton;
    private ImageView imageView2;


    public static final String LOGGEYPOOES = "LOGGEYPOOES";
    private static String url_create_user= "http://www.design-logic.net/blueribbonapp/create_user.php";
    private static String url_get_friend_list= "http://www.design-logic.net/blueribbonapp/create_friendlist.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        UserDetails.initUser();
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        newUser = UserDetails.getUserDetails();
        setContentView(R.layout.landing_page);
        puertoGalera = (TextView)findViewById(R.id.puerto_galera);
        mContext = getApplicationContext();

        /** Set Titles to Neuropolitical using hte Typefaces class for some memory reason*/

        if(puertoGalera.getTypeface() !=null && !puertoGalera.getTypeface().equals(Typefaces.get(mContext,"fonts/neuropolitical.ttf")));
        puertoGalera.setTypeface(Typefaces.get(mContext,"fonts/neuropolitical.ttf"));

        puertoGalera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this,DiveSiteListActivity.class);
                startActivity(intent);
            }
        });

        skipButton = (Button)findViewById(R.id.skip_facebook_login_button);


        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this,DiveSiteListActivity.class);
                startActivity(intent);
            }
        });

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "email", "user_friends");

        if (isFacebookLoggedIn()){
            skipButton.setVisibility(View.INVISIBLE);
            parseFacebookUserData();

        }

        imageView2 = (ImageView)findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this,DiveSiteListActivity.class);
                startActivity(intent);

            }
        });





        // Other app specific specialization
        callbackManager = CallbackManager.Factory.create();
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                parseFacebookUserData();
                accessToken = AccessToken.getCurrentAccessToken().getToken();
                skipButton.setVisibility(View.INVISIBLE);
                Intent intent = new Intent(LandingActivity.this,DiveSiteListActivity.class);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), "Facebook is connected " + accessToken, Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Facebook connection Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(getApplicationContext(), "Facebook did not Connect " + exception, Toast.LENGTH_LONG).show();
            }
        });

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                       AccessToken currentAccessToken) {
                if (currentAccessToken == null) {
                    skipButton.setVisibility(View.VISIBLE);
                }
            }
        };
        accessTokenTracker.startTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public Boolean isFacebookLoggedIn() {
        if (AccessToken.getCurrentAccessToken() != null)
            return  true;
        else return false;
    }

    private void parseFacebookUserData() {
        Bundle userParameters = new Bundle();
        userParameters.putString("fields", "id,name,email,first_name,gender,last_name,location");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                userParameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject userJsonObject = response.getJSONObject();
                        try {
                            if (userJsonObject.getString("id") != null) {
                                newUser.setName(userJsonObject.getString("name"));
                                newUser.setFacebookID(userJsonObject.getString("id"));
                                newUser.setFirst_name(userJsonObject.getString("first_name"));
                                newUser.setLast_name(userJsonObject.getString("last_name"));
                                newUser.setEmail(userJsonObject.getString("email"));
                                newUser.setGender(userJsonObject.getString("gender"));





                            }
                        } catch (Exception e) {
                            Log.d(LOGGEYPOOES, "Something fucked up with Facebook User Parsing");

                        }
                        new PostUserAsync().execute();

                        Log.d(LOGGEYPOOES, "Breakpoint");
                    }
                }
        ).executeAsync();


    }

    private void parseFacebookFriendData() {
        Bundle userParameters = new Bundle();
        userParameters.putString("fields", "friends");
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me",
                userParameters,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        JSONObject userJsonObject = response.getJSONObject();
                        JSONObject friendsObject = new JSONObject();
                        JSONArray dataArray = new JSONArray();

                        try {
                            friendsObject = userJsonObject.getJSONObject("friends");
                            dataArray = friendsObject.getJSONArray("data");
                            for (int i=0;i<dataArray.length();i++)
                            {
                                JSONObject friendID = new JSONObject();
                                friendID = dataArray.getJSONObject(i);
                                String friendIdText = friendID.getString("id");
                                String friendNameText = friendID.getString("name");
                                newUser.addFriend(friendIdText);
                                newUser.addFriendName(friendNameText);
                            }

                            Log.d(LOGGEYPOOES,dataArray.toString());
                        } catch (Exception e) {
                            Log.d(LOGGEYPOOES, "Something fucked up with Facebook Friend Parsing" + e);

                        }

                        Log.d(LOGGEYPOOES, "Breakpoint");
                        if (newUser.getFriendListSize() > 0)
                        {
                            //new PostFriendAsync().execute();
                        }
                    }
                }
        ).executeAsync();


    }


    class PostUserAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url_create_user);



            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
                nameValuePairs.add(new BasicNameValuePair("name", newUser.getName()));
                nameValuePairs.add(new BasicNameValuePair("facebook_id", newUser.getFacebookID()));
                nameValuePairs.add(new BasicNameValuePair("email", newUser.getEmail()));
                nameValuePairs.add(new BasicNameValuePair("gender", newUser.getGender()));
                nameValuePairs.add(new BasicNameValuePair("first_name", newUser.getFirst_name()));
                nameValuePairs.add(new BasicNameValuePair("last_name", newUser.getLast_name()));

                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                Log.d(LOGGEYPOOES,"Posted..");

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                Log.d(LOGGEYPOOES, e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.d(LOGGEYPOOES, e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            parseFacebookFriendData();
        }
    }

    /** Don' think this is needed for now, adds friends to another table on MYSQL - We can just do it without */

    class PostFriendAsync extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url_get_friend_list);

            try {
                // Add your data
                for (int i=0;i<newUser.getFriendListSize();i++) {
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("facebookid", newUser.getFacebookID()));
                    nameValuePairs.add(new BasicNameValuePair("friendid", newUser.getFriendlist().get(i)));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    HttpResponse response = httpclient.execute(httppost);
                    Log.d(LOGGEYPOOES, "Posted..");
                }

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
                Log.d(LOGGEYPOOES, e.toString());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                Log.d(LOGGEYPOOES, e.toString());
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
        }
    }
}
