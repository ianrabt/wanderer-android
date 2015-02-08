package me.connick.wanderer.networking;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by ianrtaylor on 2/7/15.
 */

public class ApiRequestTask extends AsyncTask<String, Void, Set<Location>> {

    @Override
    protected Set<Location> doInBackground(String... params) {
        Set<Location> locs = new HashSet<Location>();
        try {
            URL url = new URL(params[0]);
            InputStream input = url.openStream();
            StringBuilder builder = new StringBuilder();
            byte[] buf = new byte[1024];
            while (input.read(buf) > 0)
                builder.append(buf);
            JSONArray values = new JSONArray(builder.toString());
            for (int i = 0; i < values.length(); i++)
                locs.add(Location.parse(values.getJSONObject(i)));
        } catch(Exception e) {
            Log.e("FUN STUFF", "err", e);
        } finally {
            return locs;
        }
    }

    @Override
    protected void onPostExecute(Set<Location> locations) {
        Log.e("FUN STUFF", "hey");
        for (Location loc : locations) {
            Log.e("FUN STUFF", loc.toString());
        }
    }
}
