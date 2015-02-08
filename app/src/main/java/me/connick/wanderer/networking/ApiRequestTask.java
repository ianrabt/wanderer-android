package me.connick.wanderer.networking;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
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
            String str = slurp(input, 1024);
            Log.e("FUN STUFF", str);
            JSONArray values = new JSONArray(str);
            for (int i = 0; i < values.length(); i++)
                locs.add(Location.parse(values.getJSONObject(i)));
        } catch (Exception e) {
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

    public static String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try {
            final Reader in = new InputStreamReader(is, "UTF-8");
            try {
                for (; ; ) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            } finally {
                in.close();
            }
        } catch (UnsupportedEncodingException ex) {
        } catch (IOException ex) {
        }
        return out.toString();
    }
}
