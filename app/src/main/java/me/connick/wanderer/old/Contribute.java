package me.connick.wanderer.old;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import me.connick.wanderer.R;

/**
 * Created by connick on 2/7/15.
 */
public class Contribute extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_map);
    }

    public void discClicked(View view){
        //Switch Activities (tabs)
        setContentView(R.layout.activity_discover_map);
    }

    public void photoClicked(View view){
        //Do something when someone clicks on the Photo button
    }

    public void submitClicked(View view){
        //Do something when someone clicks on the Submit button
    }
}
