package com.reva.revalocator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentLocationFragment extends Fragment {

    private TextView SRNTEXT,dateTimeTextView, latitudeTextView, longitudeTextView, locationNameTextView;
    private String semesterName, sectionName, srn;
  public CurrentLocationFragment()
  {

  }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_location, container, false);

        // Retrieve SRN, semester, and section from arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            semesterName = arguments.getString("semesterName");
            sectionName = arguments.getString("sectionName");
            srn = arguments.getString("srn");

            // Check if any of the parameters is null
            if (semesterName != null && sectionName != null && srn != null) {
                // Initialize TextViews
                SRNTEXT = view.findViewById(R.id.dateTimeTextView);
                dateTimeTextView = view.findViewById(R.id.dateTimeTextView1);
                latitudeTextView = view.findViewById(R.id.latitudeTextView);
                longitudeTextView = view.findViewById(R.id.longitudeTextView);
                locationNameTextView = view.findViewById(R.id.locationNameTextView);

                // Retrieve current location data from Firebase
                DatabaseReference currentLocationRef = FirebaseDatabase.getInstance().getReference()
                        .child("Semesters").child(semesterName).child(sectionName).child(srn).child("Current Location");
                currentLocationRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Retrieve location data
                            String dateTime = dataSnapshot.child("dateTime").getValue(String.class);
                            double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                            double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                            String locationName = dataSnapshot.child("locationName").getValue(String.class);

                            // Display location data in TextViews
                            dateTimeTextView.setText("Date & Time: " + dateTime);
                            latitudeTextView.setText("Latitude: " + latitude);
                            longitudeTextView.setText("Longitude: " + longitude);
                            locationNameTextView.setText("Location Name: " + locationName);
                            if(srn!=null) SRNTEXT.setText("SRN : "+srn);
                        } else {
                            // Handle the case where data does not exist
                            dateTimeTextView.setText("No data available");
                            latitudeTextView.setText("");
                            longitudeTextView.setText("");
                            locationNameTextView.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                        dateTimeTextView.setText("Error retrieving data");
                        latitudeTextView.setText("");
                        longitudeTextView.setText("");
                        locationNameTextView.setText("");
                    }
                });
            }
        }

        return view;
    }
}
