package me.connick.wanderer;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import me.connick.wanderer.networking.ApiRequestTaskSet;
import me.connick.wanderer.networking.Location;
import me.connick.wanderer.networking.LocationAddress;
import me.connick.wanderer.networking.Urls;


public class Discover_Map extends FragmentActivity implements PhotosFragment.OnFragmentInteractionListener, ContributeFragment.OnFragmentInteractionListener, ListingFragment.OnFragmentInteractionListener {

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
        return (photosFragment != null) ? photosFragment : (photosFragment = PhotosFragment.newInstance());
    }

    private ListingFragment listFragment;

    private ListingFragment getListFragment() {
        return (listFragment != null) ? listFragment : (listFragment = ListingFragment.newInstance("", ""));
    }

    private ContributeFragment contributeFragment;

    private ContributeFragment getContributeFragment() {
        return (contributeFragment != null) ? contributeFragment : (contributeFragment = ContributeFragment.newInstance());
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
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(36, -122), 7));
                googleMap.setMyLocationEnabled(true);
                googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        Log.d(TAG, "Marker pressed, window open: " + marker.isInfoWindowShown());
                        if (marker.isInfoWindowShown()) {
                            Intent intent = new Intent(Discover_Map.this, LocationActivity.class);
                            intent.putExtra("location", locationMap.get(marker).id);
                            intent.putExtra("title", locationMap.get(marker).address);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;


    //private File photoFile = null;
    public void _takePhoto(View view) {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            new AlertDialog.Builder(this).setTitle("Missing Camera!").setNeutralButton("Ok", null).show();
            return;
        }
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //File photoFile = null;
            //try {
            //   photoFile = createImageFile();
            //} catch (IOException ex) {
            //    Toast.makeText(getApplicationContext(), "Failed to save image", Toast.LENGTH_LONG).show();
            //    return;
            //}
            // Cont
            //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void _submit(View view) {
        new LocationCreateTask(37, -122, "address").execute();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_fragment, getMapFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        selectBottom(R.id.mapButton);
        selectTop(R.id.discButton);
    }

    public class LocationCreateTask extends AsyncTask<Void, Void, Void> {

        private final double latitude, longitude;
        private final String address;

        public LocationCreateTask(double latitude, double longitude, String address) {
            this.latitude = latitude;
            this.longitude = longitude;
            this.address = address;
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(Discover_Map.TAG, "Ratingbar clicked");
            String url = Urls.getLocationsURL();
            // Create a new HttpClient and Post Header
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("latitude", "" + latitude));
                nameValuePairs.add(new BasicNameValuePair("longitude", "" + longitude));
                nameValuePairs.add(new BasicNameValuePair("address", address));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                String body = new BasicResponseHandler().handleResponse(response);

                //try {
                //    JSONObject obj = new JSONObject(body);
                //    int id = obj.getInt("id");
                //    //new PerspectiveCreateTask(id).execute();
                //}
                //catch (JSONException e) {
                //    return null;
                //}

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            return null;
        }
    }
    /*
    public class PerspectiveCreateTask extends AsyncTask<Void, Void, Void> {

        private final int locId;

        public PerspectiveCreateTask(int locId) {
            this.locId = locId;
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            HttpPost httppost = new HttpPost(Urls.getPerspectivesURL());

            //HttpEntity httpEntity = MultipartEntityBuilder.create().addBinaryBody("file", photoFile)//, ContentType.create("image/jpeg"), photoFile.getName())
            //        .addTextBody("description", "")
            //        .addTextBody("date", new SimpleDateFormat().format(new Date("yyyy-MM-dd'T'HH:mm:ss'Z'")))
            //        .addTextBody("author", "")
            //        .addTextBody("location", "" + locId).build();

            //httppost.setEntity(httpEntity);


            httpclient.getConnectionManager().shutdown();



            return null;
        }
    }//*/
/*
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix *
                ".jpg",         /* suffix *
                storageDir      /* directory *
        );

        // Save a file: path for use with ACTION_VIEW intents
        //mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }//*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            float diameter = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics());
            int w = imageBitmap.getWidth();
            int h = imageBitmap.getHeight();
            float s = diameter / Math.min(w, h);
            imageBitmap = getCroppedBitmap(Bitmap.createScaledBitmap(imageBitmap, (int) (w * s), (int) (h * s), false));
            ((ImageButton) findViewById(R.id.cam)).setImageBitmap(imageBitmap);
        }
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
                bitmap.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    public void _disc(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_fragment, getMapFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        selectBottom(R.id.mapButton);
        selectTop(R.id.discButton);
    }

    public void _cont(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_fragment, getContributeFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        selectTop(R.id.contButton);
    }

    public void _map(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_fragment, getMapFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        selectBottom(R.id.mapButton);
        selectTop(R.id.discButton);
    }

    public void _photos(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_fragment, getPhotosFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        selectBottom(R.id.photosButton);
        selectTop(R.id.discButton);
    }

    public void _list(View view) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.middle_fragment, getListFragment());
        transaction.addToBackStack(null);
        transaction.commit();
        selectBottom(R.id.listButton);
        selectTop(R.id.discButton);
    }

    private void selectBottom(int id) {
        findViewById(R.id.mapButton).setBackgroundColor(button_blank);
        findViewById(R.id.photosButton).setBackgroundColor(button_blank);
        findViewById(R.id.listButton).setBackgroundColor(button_blank);
        findViewById(id).setBackgroundColor(button_selected);
    }

    private void selectTop(int id) {
        if (id == R.id.contButton) {
            findViewById(R.id.discButton).setBackgroundColor(button_blank);
            findViewById(id).setBackgroundColor(button_selected);
            findViewById(R.id.mapButton).setVisibility(View.GONE);
            findViewById(R.id.photosButton).setVisibility(View.GONE);
            findViewById(R.id.listButton).setVisibility(View.GONE);
        }
        else {
            findViewById(R.id.contButton).setBackgroundColor(button_blank);
            findViewById(id).setBackgroundColor(button_selected);
            findViewById(id).setBackgroundColor(button_selected);
            findViewById(R.id.mapButton).setVisibility(View.VISIBLE);
            findViewById(R.id.photosButton).setVisibility(View.VISIBLE);
            findViewById(R.id.listButton).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onFragmentInteraction(String id) {

    }
}
