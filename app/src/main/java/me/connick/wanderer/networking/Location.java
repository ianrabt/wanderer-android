package me.connick.wanderer.networking;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by ianrtaylor on 2/7/15.
 */
public class Location {
    public final int id;
    public final String address;
    public final BigDecimal lat, lon;
    public final SortedSet<Perspective> perspectives = new TreeSet<Perspective>();
    public final Set<Rating> ratings = new HashSet<Rating>();
    public final double ratingAvg;
    public final int ratingCount;

    public Location(int id, String lat, String lon, String address, double ratingAvg, int ratingCount) {
        this.id = id;
        this.lat = new BigDecimal(lat);
        this.lon = new BigDecimal(lon);
        this.address = address;
        this.ratingAvg = ratingAvg;
        this.ratingCount = ratingCount;
    }

    public static class Perspective implements Comparable<Perspective> {
        public final int id;
        public final String photoURL, desc, author;
        public final Date date;
        public final Bitmap photo;

        public Perspective(int id, String photoURL, String desc, String author, String date) throws ParseException {
            this.id = id;
            this.photoURL = photoURL;
            this.desc = desc;
            this.date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(date);
            this.author = author;

            Bitmap downloaded = null;
            try {
                URL url = new URL(photoURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                downloaded = BitmapFactory.decodeStream(input);
            } catch (IOException e) {}
            photo = downloaded;
        }

        @Override
        public int compareTo(Perspective another) {
            return (int) (another.date.getTime() - date.getTime());
        }
    }

    public static class Rating {
        public final int id;
        public final int rating;
        public final String author;

        public Rating(int id, int rating, String author) {
            this.id = id;
            this.rating = rating;
            this.author = author;
        }
    }

    public static Location parse(JSONObject obj) throws Exception {
        Location loc = new Location(obj.getInt("id"), obj.getString("latitude"), obj.getString("longitude"), obj.getString("address"), obj.getDouble("rating_avg"), obj.getInt("rating_count"));
        JSONArray ps = obj.getJSONArray("perspective_set");
        for (int i = 0; i < ps.length(); i++) {
            JSONObject pObj = ps.getJSONObject(i);
            loc.perspectives.add(new Perspective(pObj.getInt("id"), pObj.getString("photo"), pObj.getString("description"), pObj.getString("author"), pObj.getString("date")));
        }
        JSONArray rs = obj.getJSONArray("rating_set");
        for (int i = 0; i < rs.length(); i++) {
            JSONObject rObj = rs.getJSONObject(i);
            loc.ratings.add(new Rating(rObj.getInt("id"), rObj.getInt("rating"), rObj.getString("author")));
        }
        return loc;
    }
}
