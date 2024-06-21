package com.reva.revalocator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.location.Address;
import android.location.Geocoder;
import java.io.IOException;
import java.util.List;


public class MapsFragment extends Fragment implements LocationListener, OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    String srn,sem,sec;
    private GoogleMap mMap;
    private Marker myMarker;
    private Context mContext;
    private DatabaseReference mDatabase;
    private static final int PERMISSION_REQUEST_CODE = 1001;

    private LocationManager locationManager;

    int i=0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);

        srn=getActivity().getIntent().getStringExtra("srn");
        sem=getActivity().getIntent().getStringExtra("semester");
        sec=getActivity().getIntent().getStringExtra("section");


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext = getContext();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

//        if (!checkLocationPermission()) {
//            requestLocationPermissions();
//        } else {
//            // Permissions are already granted, start the service
//            startLocationService();
//        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

    }
//    public static boolean isGPSEnabled(Context context) {
//        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
//        return locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//    }
//    private boolean checkLocationPermission() {
//        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
//    }
//    private void requestLocationPermissions() {
//        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_CODE);
//    }
//    private void startLocationService() {
//        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
//        if (locationManager != null) {
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
//        }
//    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        get_Location();
    }

    private void get_Location() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
        } else {
            mMap.setMyLocationEnabled(true); // Enable showing user's location on the map
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager != null) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }

    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng myLoc = null;
        if (location != null) {

            myLoc = new LatLng(location.getLatitude(), location.getLongitude());

            // Update the marker on the map
            myLoc = new LatLng(location.getLatitude(), location.getLongitude());

            if (myMarker != null) {
                myMarker.setPosition(myLoc);
            } else {
                myMarker = mMap.addMarker(new MarkerOptions().position(myLoc).title("My Location").icon(bitdescriber(mContext, R.drawable.std_marker)));
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 20));

        }
        updateMarkerPositionInDatabase(myLoc);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }
    private void updateMarkerPositionInDatabase(LatLng newPosition) {

        String dateTime = getCurrentDateTime();
        String locationName = getLocationName(newPosition.latitude, newPosition.longitude);
        DatabaseReference markerLocationsRef = mDatabase.child("markerLocations");
        DatabaseReference markerLocationsRef2 = mDatabase.child("Semesters");

        // Check if the SRN is not null
        if (srn != null) {
            // Create a child node with the SRN as the key
            DatabaseReference srnRef = markerLocationsRef.child(srn);
           DatabaseReference srnRef2 = markerLocationsRef2.child("Semester "+sem).child(sec).child(srn).child("Current Location");
            // Update the child node with the new location and date/time
            srnRef.child("latitude").setValue(newPosition.latitude);
            srnRef.child("longitude").setValue(newPosition.longitude);
            srnRef.child("locationName").setValue(locationName);
            srnRef.child("dateTime").setValue(dateTime);

            srnRef2.child("latitude").setValue(newPosition.latitude);
            srnRef2.child("longitude").setValue(newPosition.longitude);
            srnRef2.child("locationName").setValue(locationName);
            srnRef2.child("dateTime").setValue(dateTime);

        } else {
            // Handle the case where SRN is null
            // You might want to handle this situation based on your app's requirements
        }
    }


    private String getLocationName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    sb.append(address.getAddressLine(i)).append(" ");
                }
                return sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Unknown Location";
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
    // Method to show dialog to turn on location services
    private void showLocationTurnDialog() {
        if (!isGPSEnabled(mContext)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
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

    private BitmapDescriptor bitdescriber(Context ctx, int vectorread) {
        Drawable vectordraw = ContextCompat.getDrawable(ctx, vectorread);
        vectordraw.setBounds(0, 0, vectordraw.getIntrinsicWidth(), vectordraw.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectordraw.getIntrinsicWidth(), vectordraw.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectordraw.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                get_Location();
            } else {
                Toast.makeText(mContext, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

//    private void storeUserData(String srn, String location ,String latitude ,String  longitude ,String date_time) {
//        // Get a reference to the location where the user data will be stored
//        DatabaseReference userDataRef = mDatabase.child("users").child(srn);
//
//        // Create a User object with the provided data
//        Users user = new Users(srn , location ,latitude , longitude , date_time);
//
//        // Set the value of the user data in the database
//        userDataRef.setValue(user);
//    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}

}
