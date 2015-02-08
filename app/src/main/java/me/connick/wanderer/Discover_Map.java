package me.connick.wanderer;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import me.connick.wanderer.networking.ApiRequestTaskSet;
import me.connick.wanderer.networking.Location;
import me.connick.wanderer.networking.Urls;


public class Discover_Map extends FragmentActivity implements PhotosFragment.OnFragmentInteractionListener {

    public static final String TAG = "Wanderer";

    private static final int button_blank = 0xFFE27D2B;
    private static final int button_selected = 0xFFA35A1F;

    private final Map<Marker, Location> locationMap = new HashMap<Marker, Location>();

    private MapFragment mapFragment;

    private MapFragment getMapFragment() {
        return (mapFragment != null) ? mapFragment : (mapFragment = MapFragment.newInstance());
    }

    private PhotosFragment photosFragment;

    private PhotosFragment getPhotosFragment() {
        return (photosFragment != null) ? photosFragment : (photosFragment = PhotosFragment.newInstance("", ""));
    }

    private ListingFragment listFragment;

    private ListingFragment getListFragment() {
        return (listFragment != null) ? listFragment : (listFragment = ListingFragment.newInstance("", ""));
    }

    private int state;

    @Override
    public void setContentView(int layoutResID) {
        state = layoutResID;
        super.setContentView(layoutResID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_map);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.middle_fragment, getMapFragment());
        fragmentTransaction.commit();

        new ApiRequestTaskSet(new ApiRequestTaskSet.LocationSetReceiver() {
            @Override
            public void receive(Set<Location> locations) {
                for (Location loc : locations) {
                    StringBuilder builder = new StringBuilder(5);
                    for (int i = 0; i < 5; i++) {
                        if (loc.ratingAvg > i)
                            builder.append('\u2605');
                        else
                            builder.append('\u2606');
                    }

                    MarkerOptions marker = new MarkerOptions()
                            .position(new LatLng(loc.lat.doubleValue(), loc.lon.doubleValue()))
                            .title(loc.address)
                            .snippet(builder.toString());
                    Marker instance = getMapFragment().getMap().addMarker(marker);
                    locationMap.put(instance, loc);
                }
            }
        }).execute(Urls.getLocationsURL());

        getMapFragment().getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Log.d(TAG, "Marker pressed, window open: " + marker.isInfoWindowShown());
                        if (marker.isInfoWindowShown()) {
                            Intent intent = new Intent(Discover_Map.this, LocationActivity.class);
                            intent.putExtra("location", locationMap.get(marker).id);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    /**
     * Called when the user clicks the Send button
     */
    public void contClicked(View view) {
        // Do something in response to button
        setContentView(R.layout.activity_contribute);
    }

    public void discClicked(View view) {
        //Switch Activities (tabs)
        setContentView(R.layout.activity_discover_map);
    }

    public void _map(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_fragment, getMapFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        highlight(R.id.mapButton);
    }

    public void _photos(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_fragment, getPhotosFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        highlight(R.id.photosButton);
    }

    public void _list(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_fragment, getListFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        highlight(R.id.listButton);
    }

    private void highlight(int id) {
        ((Button) findViewById(R.id.mapButton)).setBackgroundColor(button_blank);
        ((Button) findViewById(R.id.photosButton)).setBackgroundColor(button_blank);
        ((Button) findViewById(R.id.listButton)).setBackgroundColor(button_blank);
        ((Button) findViewById(id)).setBackgroundColor(button_selected);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
