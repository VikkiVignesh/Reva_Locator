package com.reva.revalocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import android.provider.Settings;

import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
 DrawerLayout drawerLayout;
 NavigationView navigationView;
 Toolbar toolbar;


 String srn,section,semester;

 final int PERMISSION_REQUEST_CODE = 1001;
 private GoogleMap mMap;
 LocationStorage SavedLocation=new LocationStorage();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        get_Location();

        drawerLayout = findViewById(R.id.drawable);
        navigationView =findViewById(R.id.navigationn);
        toolbar  = findViewById(R.id.tool);


        //step 1 set up the toolbar
        setSupportActionBar(toolbar);// to set the toolbar

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.OpenDrawer,R.string.ClosedDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        // Load MapsFragment initially
       fragmentload(new MapsFragment(),0);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {

                int id = item.getItemId();
                if(id == R.id.contact)
                {
                    fragmentload(new Timeline(),1);
                } else if (id == R.id.home) {
                    fragmentload(new My_profile(),1);
                } else if (id == R.id.bluehtoot) {
                    fragmentload(new MapsFragment(),1);
                }
                else {
                    fragmentload(new MapsFragment(),0);
                }
                //on click of any button close the drawer

                drawerLayout.closeDrawer(GravityCompat.START);

                return true;
            }
        });

    }


    @Override
    public void onBackPressed() {
        //if drawer is opne then closed the drawer first on backpressed else close the activity

        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void fragmentload(Fragment fragment, int flag)

    {
        Bundle bundle = new Bundle();
        bundle.putString("srn",srn);
        bundle.putString("section",section);
        bundle.putString("semester",semester);
        fragment.setArguments(bundle);


        FragmentManager fragmentManager   = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if(flag == 0)
        {
            fragmentTransaction.add(R.id.container,fragment);

        }
        else
        {
            fragmentTransaction.replace(R.id.container,fragment);
        }
        fragmentTransaction.commit();

    }

    private void get_Location()
    {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        }
        showLocationTurnDialog();
        // Inside onCreate() method, after checking location settings
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    // Use the location object to get latitude and longitude
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng myloc = new LatLng(latitude, longitude);
                    if (mMap != null) { // Check if mMap is not null
                        mMap.addMarker(new MarkerOptions().position(myloc).title("My Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc, 20));
                    } else {
                        // Handle the case where mMap is null
                        Toast.makeText(MainActivity.this, "Google Map is not initialized", Toast.LENGTH_SHORT).show();
                    }
                    // Do something with the obtained latitude and longitude
                    Toast.makeText(MainActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                } else {
                    // Unable to retrieve location
                    Toast.makeText(MainActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Location retrieval failed
                Toast.makeText(MainActivity.this, "Location retrieval failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showLocationTurnDialog() {
        if (!MainActivity.isGPSEnabled(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Location services are disabled. Do you want to enable them?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Open location settings
                            Intent enableLocationIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(enableLocationIntent);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // Dismiss the dialog
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }
}