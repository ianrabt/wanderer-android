package me.connick.wanderer;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Discover_Map extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_map);
    }

    /** Called when the user clicks the Send button */
    public void contClickedM(View view) {
        // Do something in response to button
        Contribute.disc = R.layout.activity_discover_map;
        setContentView(R.layout.activity_contribute);
    }

    public void listClickedM(View view){
        setContentView(R.layout.activity_discover_list);
    }

    public void photosClickedM(View view){
        setContentView(R.layout.activity_discover_photos);
    }
}
