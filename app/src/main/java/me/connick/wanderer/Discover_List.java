package me.connick.wanderer;

import android.app.Activity;
import android.view.View;


public class Discover_List extends Activity {

    /** Called when the user clicks the Send button */
    public void contClickedL(View view) {
        // Do something in response to button
        Contribute.disc = R.layout.activity_discover_list;
        setContentView(R.layout.activity_contribute);
    }

    public void mapClickedL(View view){
        setContentView(R.layout.activity_discover_map);
    }

    public void photosClickedL(View view){
        setContentView(R.layout.activity_discover_photos);
    }
}
