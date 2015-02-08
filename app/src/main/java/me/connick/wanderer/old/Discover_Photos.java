package me.connick.wanderer.old;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import me.connick.wanderer.Discover_Map;
import me.connick.wanderer.R;
import me.connick.wanderer.old.Contribute;
import me.connick.wanderer.old.Discover_List;


public class Discover_Photos extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_photos);
    }

    /** Called when the user clicks the Send button */
    public void contClickedP(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Contribute.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void listClickedP(View view){
        Intent intent = new Intent(this, Discover_List.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    public void mapClickedP(View view){
        Intent intent = new Intent(this, Discover_Map.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }
}
