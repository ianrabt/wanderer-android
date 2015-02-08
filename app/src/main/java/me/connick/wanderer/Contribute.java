package me.connick.wanderer;

import android.app.Activity;
import android.view.View;

/**
 * Created by connick on 2/7/15.
 */
public class Contribute extends Activity{

    public static int disc;

    public void discClicked(View view){
        //Switch Activities (tabs)
        setContentView(disc);
    }

    public void photoClicked(View view){
        //Do something when someone clicks on the Photo button
    }

    public void submitClicked(View view){
        //Do something when someone clicks on the Submit button
    }
}
