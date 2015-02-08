package me.connick.wanderer;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class Discover_Photos extends Activity {

    /** Called when the user clicks the Send button */
    public void contClickedP(View view) {
        // Do something in response to button
        Contribute.disc = R.layout.activity_discover_photos;
        setContentView(R.layout.activity_contribute);
    }

    public void listClickedP(View view){
        setContentView(R.layout.activity_discover_list);
    }

    public void mapClickedP(View view){
        setContentView(R.layout.activity_discover_map);
    }
}
