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
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;
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


