package me.connick.wanderer.networking;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

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

public class ApiRequestTaskSet extends AsyncTask<String, Void, Set<Location>> {

    private final LocationSetReceiver receiver;

    public ApiRequestTaskSet() {
        this(null);
    }

    public ApiRequestTaskSet(LocationSetReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    protected Set<Location> doInBackground(String... params) {
        Set<Location> locs = new HashSet<Location>();
        try {
            URL url = new URL(params[0]);
            InputStream input = url.openStream();
            String str = slurp(input, 1024);
            Log.e(Discover_Map.TAG, str);
            JSONArray values = new JSONArray(str);
            for (int i = 0; i < values.length(); i++)
                locs.add(Location.parse(values.getJSONObject(i)));
        } catch (Exception e) {
            Log.w(Discover_Map.TAG, "err", e);
        } finally {
            return locs;
        }
    }

    @Override
    protected void onPostExecute(Set<Location> locations) {
        if (receiver != null)
            receiver.receive(locations);
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

    public static interface LocationSetReceiver {
        public void receive(Set<Location> locations);
    }
}
