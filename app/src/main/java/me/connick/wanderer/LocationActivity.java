package me.connick.wanderer;

import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

import me.connick.wanderer.networking.ApiRequestTask;
import me.connick.wanderer.networking.Location;
import me.connick.wanderer.networking.Urls;


public class LocationActivity extends ActionBarActivity implements RatingBar.OnRatingBarChangeListener {

    private Location loc = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.OrangeActionBarTheme);

        setContentView(R.layout.activity_location);

        setTitle(getIntent().getStringExtra("title"));
        int id = getIntent().getIntExtra("location", 0);
        new ApiRequestTask(new ApiRequestTask.LocationReceiver() {
            @Override
            public void receive(Location location) {
                loc = location;
                RatingBar ratings = (RatingBar) findViewById(R.id.ratings);
                ratings.setRating((float) location.ratingAvg);
                ratings.setOnRatingBarChangeListener(LocationActivity.this);
                for (Location.Perspective p : location.perspectives) {
                    ImageView imgView = new ImageView(getApplicationContext());
                    imgView.setImageBitmap(p.photo);
                    LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
                    layout.addView(imgView);

                    TextView text = new TextView(getApplicationContext());
                    text.setText(p.desc);
                    layout.addView(text);
                }
            }
        }).execute(Urls.getLocationURL(id));
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        new RatingChangeTask((int) rating, loc.id).execute();
    }

    public static class RatingChangeTask extends AsyncTask<Void, Void, Void> {

        private final int rating;
        private final int locid;

        public RatingChangeTask(int rating, int locid) {
            this.rating = rating;
            this.locid = locid;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(Discover_Map.TAG, "Ratingbar clicked");
            String url = Urls.getRatingsURL();
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("rating", "" + rating));
                nameValuePairs.add(new BasicNameValuePair("location", "" + locid));
                nameValuePairs.add(new BasicNameValuePair("author", Integer.toHexString(new Random().nextInt())));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return null;
        }
    }
}
