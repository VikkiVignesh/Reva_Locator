package com.reva.revalocator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LastVisit extends Fragment {

    private TextView SRNTEXT, dateTimeTextView, latitudeTextView, longitudeTextView, locationNameTextView;
    private String semesterName, sectionName, srn;

    public LastVisit() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);

        // Retrieve SRN, semester, and section from arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            semesterName = arguments.getString("semesterName");
            sectionName = arguments.getString("sectionName");
            srn = arguments.getString("srn");

            // Initialize TextViews
            SRNTEXT = view.findViewById(R.id.dateTimeTextView);
            dateTimeTextView = view.findViewById(R.id.dateTimeTextView1);
            latitudeTextView = view.findViewById(R.id.latitudeTextView);
            longitudeTextView = view.findViewById(R.id.longitudeTextView);
            locationNameTextView = view.findViewById(R.id.locationNameTextView);

            // Ensure TextViews are not null
            if (SRNTEXT != null && dateTimeTextView != null && latitudeTextView != null && longitudeTextView != null && locationNameTextView != null) {
                // Retrieve last visited location data from Firebase
                DatabaseReference lastVisitedRef = FirebaseDatabase.getInstance().getReference()
                        .child("Semesters").child(semesterName).child(sectionName).child(srn).child("Last Visited");
                lastVisitedRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Retrieve location data
                            String dateTime = dataSnapshot.child("dateTime").getValue(String.class);
                            Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                            Double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                            String locationName = dataSnapshot.child("locationName").getValue(String.class);

                            // Display location data in TextViews
                            if (dateTime != null) dateTimeTextView.setText("Date & Time: " + dateTime);
                            if (latitude != null) latitudeTextView.setText("Latitude: " + latitude);
                            if (longitude != null) longitudeTextView.setText("Longitude: " + longitude);
                            if (locationName != null) locationNameTextView.setText("Location Name: " + locationName);
                            if (srn != null) SRNTEXT.setText("SRN: " + srn);
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
            } else {
                Log.e("TImelineFragment", "One or more TextViews are null");
            }
        } else {
            Log.e("TImelineFragment", "Arguments bundle is null");
        }

        return view;
    }
}
