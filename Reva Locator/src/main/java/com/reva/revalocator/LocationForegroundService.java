package com.reva.revalocator;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class LocationForegroundService extends Service implements LocationListener {

    private static final int PERMISSION_REQUEST_CODE = 1001;
    private LocationManager locationManager;
    private GoogleMap mMap;
    private Marker myMarker;
    private Context mContext;
    private DatabaseReference mDatabase;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(1, new Notification()); // Start foreground service

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // Permissions are not granted, request them from the user
                // Permission request will be handled in the fragment
            } else {
                // Permissions are granted, request location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            locationManager.removeUpdates(this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng myLoc = null;
        if (location != null) {
            myLoc = new LatLng(location.getLatitude(), location.getLongitude());
            if (myMarker != null) {
                myMarker.setPosition(myLoc);
            } else {
                myMarker = mMap.addMarker(new MarkerOptions().position(myLoc).title("My Location").icon(bitdescriber(mContext, R.drawable.home)));
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 20));
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        updateMarkerPositionInDatabase(myLoc);
    }



    private void updateMarkerPositionInDatabase(LatLng newPosition) {
        String srn = "R21EF314";
        String dateTime = getCurrentDateTime();
        String locationName = getLocationName(newPosition.latitude, newPosition.longitude);
        DatabaseReference markerLocationsRef = mDatabase.child("markerLocations");

        // Check if the SRN is not null
        if (srn != null) {
            // Create a child node with the SRN as the key
            DatabaseReference srnRef = markerLocationsRef.child(srn);

            // Update the child node with the new location and date/time
            srnRef.child("latitude").setValue(newPosition.latitude);
            srnRef.child("longitude").setValue(newPosition.longitude);
            srnRef.child("locationName").setValue(locationName);
            srnRef.child("dateTime").setValue(dateTime);
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
    private BitmapDescriptor bitdescriber(Context ctx, int vectorread) {
        Drawable vectordraw = ContextCompat.getDrawable(ctx, vectorread);
        vectordraw.setBounds(0, 0, vectordraw.getIntrinsicWidth(), vectordraw.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectordraw.getIntrinsicWidth(), vectordraw.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectordraw.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    // Implement other LocationListener methods as needed
}
