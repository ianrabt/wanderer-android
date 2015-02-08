package me.connick.wanderer;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;


public class Discover_Map extends FragmentActivity implements PhotosFragment.OnFragmentInteractionListener {


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
    }

    /** Called when the user clicks the Send button */
    public void contClicked(View view) {
        // Do something in response to button
        setContentView(R.layout.activity_contribute);
    }

    public void discClicked(View view){
        //Switch Activities (tabs)
        setContentView(R.layout.activity_discover_map);
    }

    public void _map(View view){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_fragment, getMapFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        highlight(R.id.mapButton);
    }

    public void _photos(View view){
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
        ((Button) findViewById(R.id.mapButton)).setBackgroundColor(0xFFE27D2B);
        ((Button) findViewById(R.id.photosButton)).setBackgroundColor(0xFFE27D2B);
        ((Button) findViewById(R.id.listButton)).setBackgroundColor(0xFFE27D2B);
        ((Button) findViewById(id)).setBackgroundColor(0xFFA35A1F);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}
