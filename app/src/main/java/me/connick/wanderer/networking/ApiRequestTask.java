package me.connick.wanderer.networking;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import me.connick.wanderer.Discover_Map;

/**
 * Created by ianrtaylor on 2/7/15.
 */

public class ApiRequestTask extends AsyncTask<String, Void, Location> {

    private final LocationReceiver receiver;

    public ApiRequestTask() {
        this(null);
    }

    public ApiRequestTask(LocationReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    protected Location doInBackground(String... params) {
        Location loc = null;
        try {
            URL url = new URL(params[0]);
            InputStream input = url.openStream();
            String str = slurp(input, 1024);
            Log.e(Discover_Map.TAG, str);
            JSONObject obj = new JSONObject(str);
            loc = Location.parse(obj);
        } catch (Exception e) {
            Log.w(Discover_Map.TAG, "err", e);
        } finally {
            return loc;
        }
    }

    @Override
    protected void onPostExecute(Location loc) {
        if (receiver != null)
            receiver.receive(loc);
    }

    private static String slurp(final InputStream is, final int bufferSize) {
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        try {
            final Reader in = new InputStreamReader(is, "UTF-8");
            try {
                while (true) {
                    int rsz = in.read(buffer, 0, buffer.length);
                    if (rsz < 0)
                        break;
                    out.append(buffer, 0, rsz);
                }
            } finally {
                in.close();
            }
        } catch (IOException ex) {}
        return out.toString();
    }

    public static interface LocationReceiver {
        public void receive(Location locations);
    }
}
