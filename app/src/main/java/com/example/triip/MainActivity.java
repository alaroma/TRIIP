package com.example.triip;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseMap implements  OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    private EditText mSearchText;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    private LatLng center;
    private LatLng current;
    private int i = 0;

    protected int getLayoutId(){ return R.layout.activity_main; }
    @Override
    protected void startDemo() {
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(61.7898036, 34.359688), 10));

        final LatLng plus = new LatLng(61.794702, 34.244036);
        final LatLng minus1 = new LatLng(61.789653, 34.352357);
        final LatLng minus2 = new LatLng(61.788984, 34.365446);

        final Location marker1 = new Location("locationB");
        marker1.setLatitude(minus1.latitude);
        marker1.setLongitude(minus1.longitude);

        final Location marker2 = new Location("locationB");
        marker2.setLatitude(minus2.latitude);
        marker2.setLongitude(minus2.longitude);

        final Location marker3 = new Location("locationB");
        marker3.setLatitude(plus.latitude);
        marker3.setLongitude(plus.longitude);

        getMap().addMarker(new MarkerOptions().position(plus));
        getMap().addMarker(new MarkerOptions().position(minus1));
        getMap().addMarker(new MarkerOptions().position(minus2));

        mSearchText = (EditText) findViewById(R.id.input_search);
        enableMyLocation();

        mSearchText = (EditText) findViewById(R.id.input_search);
        enableMyLocation();
        init();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intObj = new Intent(MainActivity.this, CreateLabel.class);
                startActivity(intObj);

            }

        });

        FloatingActionButton filt = (FloatingActionButton) findViewById(R.id.filt);
        filt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMap().setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        switch (i) {
                            case 0:
                                getMap().clear();
                                getMap().addMarker(new MarkerOptions().position(latLng)
                                        .alpha(3f));
                                getMap().animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                getMap().addCircle(new CircleOptions()
                                        .center(latLng)
                                        .radius(1000.0)
                                        .strokeWidth(2f)
                                        .strokeColor(Color.RED)
                                        .fillColor(Color.argb(70,159,50,59)));
                                center = latLng;

                                Location currentLocation = new Location("locationA");
                                currentLocation.setLatitude(center.latitude);
                                currentLocation.setLongitude(center.longitude);

                                double distance1 = currentLocation.distanceTo(marker1);
                                double distance2 = currentLocation.distanceTo(marker2);
                                double distance3 = currentLocation.distanceTo(marker3);
                                if(distance1 < 1000.0){
                                    getMap().addMarker(new MarkerOptions().position(minus1).title(""+ distance1));
                                }
                                if(distance2 < 1000.0){
                                    getMap().addMarker(new MarkerOptions().position(minus2).title(""+ distance2));
                                }
                                if(distance3 < 1000.0){
                                    getMap().addMarker(new MarkerOptions().position(plus).title(""+ distance3));
                                }

                                i++;
                                break;
                            case 1:
                                getMap().clear();
                                getMap().addMarker(new MarkerOptions().position(latLng).alpha(3f));
                                getMap().animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                getMap().addCircle(new CircleOptions()
                                        .center(latLng)
                                        .radius(1000.0)
                                        .strokeWidth(2f)
                                        .strokeColor(Color.RED)
                                        .fillColor(Color.argb(70,159,50,59)));
                                current = latLng;

                                Location currentLocation1 = new Location("locationA");
                                currentLocation1.setLatitude(current.latitude);
                                currentLocation1.setLongitude(current.longitude);

                                double distance01 = currentLocation1.distanceTo(marker1);
                                double distance02 = currentLocation1.distanceTo(marker2);
                                double distance03 = currentLocation1.distanceTo(marker3);

                                if(distance01 < 1000.0){
                                    getMap().addMarker(new MarkerOptions().position(minus1).title(""+ distance01));
                                }
                                if(distance02 < 1000.0){
                                    getMap().addMarker(new MarkerOptions().position(minus2).title(""+ distance02));
                                }
                                if(distance03 < 1000.0){
                                    getMap().addMarker(new MarkerOptions().position(plus).title(""+ distance03));
                                }
                                break;
                        }
                    }
                });


            }
        });
        createLabelonMap();

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView tt = (TextView) header.findViewById(R.id.vhod);

        tt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intObj = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intObj);
            }
        });
        ImageView mag = (ImageView) findViewById(R.id.ic_magnfy);
        mag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);

            }

        });
    }

    public void createLabelonMap() {
        final String URL = "http://triip.yasya.top:80/api/v1/marks";
        final JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(URL,

                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        try {

                            for(int i=0; i<response.length();i++) {

                                JSONObject label = (JSONObject) response.get(i);
                                Double lat = Double.valueOf(label.getString("lat"));
                                Double lng = Double.valueOf(label.getString("lng"));
                                LatLng maplab = new LatLng(lat, lng);
                                getMap().addMarker(new MarkerOptions().position(maplab).title("hi" + ""));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),
                                    "Error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest, "MAP");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void init(){
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){

                    geoLocate();
                }

                return false;
            }
        });


        hideSoftKeyboard();
    }
    private void geoLocate(){
        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MainActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }
    private void moveCamera(LatLng latLng, float zoom, String title){
        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        //if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            // getMap().addMarker(options);
       // }

        hideSoftKeyboard();
    }
    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @SuppressLint("MissingPermission")
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getMap().setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.nav_profile) {
            Intent intObj = new Intent(this, LoginActivity.class);
            startActivity(intObj);
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(this, Main2Activity.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {
            Intent intent2 = new Intent(this, SettingsActivity.class);
            startActivity(intent2);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}