package me.connick.wanderer;

import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import me.connick.wanderer.networking.ApiRequestTask;
import me.connick.wanderer.networking.Location;
import me.connick.wanderer.networking.Urls;


public class LocationActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.OrangeActionBarTheme);

        setContentView(R.layout.activity_location);

        int id = getIntent().getIntExtra("location", 0);
        new ApiRequestTask(new ApiRequestTask.LocationReceiver() {
            @Override
            public void receive(Location locations) {

            }
        }).execute(Urls.getLocationURL(id));
    }
}
