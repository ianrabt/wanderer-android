package me.connick.wanderer.networking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ianrtaylor on 2/7/15.
 */
public class Location {
    public final String address;
    public final BigDecimal lat, lon;
    public final Set<Perspective> perspectives = new HashSet<Perspective>();
    public final Set<Rating> ratings = new HashSet<Rating>();

    public Location(String lat, String lon, String address) {
        this.lat = new BigDecimal(lat);
        this.lon = new BigDecimal(lon);
        this.address = address;
    }

    public static class Perspective {
        public final String photoURL, desc, author;
        public final Date date;
        public Perspective(String photoURL, String desc, String author, String date){
            this.photoURL = photoURL;
            this.desc = desc;
            this.date = new Date(date);
            this.author = author;
        }
    }

    public static class Rating {
        public final int rating;
        public final String author;

        public Rating(int rating, String author) {
            this.rating = rating;
            this.author = author;
        }
    }

    public static Location parse(JSONObject obj) throws JSONException {
        Location loc = new Location(obj.getString("latitude"), obj.getString("longitude"), obj.getString("address"));
        JSONArray ps = obj.getJSONArray("perspective_set");
        for (int i = 0; i < ps.length(); i++) {
            JSONObject pObj = ps.getJSONObject(i);
            loc.perspectives.add(new Perspective(pObj.getString("photo"), pObj.getString("description"), pObj.getString("author"), pObj.getString("date")));
        }
        JSONArray rs = obj.getJSONArray("perspective_set");
        for (int i = 0; i < rs.length(); i++) {
            JSONObject rObj = rs.getJSONObject(i);
            loc.ratings.add(new Rating(rObj.getInt("rating"), rObj.getString("author")));
        }
        return loc;
    }
}
