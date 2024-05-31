package com.example.revalocator;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.revalocator.databinding.ActivityHomeMapsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class HomeMapsActivity extends FragmentActivity implements OnMapReadyCallback,LocationListener {

    private GoogleMap mMap;
    private ActivityHomeMapsBinding binding;
    Marker myMarker;
    LocationManager locationManager;

    final int PERMISSION_REQUEST_CODE = 1001;
    final int REQUEST_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Request location updates
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0L, 0f, this);

        }
    }





    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        get_Location();
       // mMap.setOnMapClickListener(this);
        //LatLng sydney = new LatLng(13.1169, 77.6346);

    }
    public static boolean isGPSEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // Method to show dialog to turn on location services
    private void showLocationTurnDialog() {
        if (!HomeMapsActivity.isGPSEnabled(this)) {


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
                    LatLng myloc=new LatLng(latitude,longitude);
                    mMap.addMarker(new MarkerOptions().position(myloc).title("My Location").icon(bitdescriber(getApplicationContext(),R.drawable.home)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myloc,20));

                    // Do something with the obtained latitude and longitude
                    Toast.makeText(HomeMapsActivity.this, "Latitude: " + latitude + ", Longitude: " + longitude, Toast.LENGTH_SHORT).show();
                } else {
                    // Unable to retrieve location
                    Toast.makeText(HomeMapsActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Location retrieval failed
                Toast.makeText(HomeMapsActivity.this, "Location retrieval failed", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private BitmapDescriptor bitdescriber(Context ctx, int vectorread)
    {
        Drawable vectordraw=ContextCompat.getDrawable(ctx,vectorread);
        vectordraw.setBounds(0,0,vectordraw.getIntrinsicWidth(),vectordraw.getIntrinsicHeight());
        Bitmap bitmap=Bitmap.createBitmap(vectordraw.getIntrinsicWidth(),vectordraw.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas=new Canvas(bitmap);
        vectordraw.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public void onLocationChanged(Location location) {
        if (location != null) {
            // Update marker position
            LatLng myLoc = new LatLng(location.getLatitude(), location.getLongitude());
            if (myMarker != null) {
                myMarker.setPosition(myLoc);
            } else {
                myMarker = mMap.addMarker(new MarkerOptions().position(myLoc).title("My Location").icon(bitdescriber(getApplicationContext(), R.drawable.home)));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 20));
        }
    }

}


