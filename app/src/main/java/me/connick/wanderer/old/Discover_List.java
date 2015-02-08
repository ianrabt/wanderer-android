package me.connick.wanderer.old;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.connick.wanderer.Discover_Map;
import me.connick.wanderer.R;


public class Discover_List extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_list);
    }

    /** Called when the user clicks the Send button */
    public void contClickedL(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Contribute.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void mapClickedL(View view){
        Intent intent = new Intent(this, Discover_Map.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void photosClickedL(View view){
        Intent intent = new Intent(this, Discover_Photos.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
