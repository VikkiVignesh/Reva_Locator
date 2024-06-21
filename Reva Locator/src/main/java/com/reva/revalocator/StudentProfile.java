package com.reva.revalocator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentProfile extends Fragment {

    private ImageView profileImageView;
    private TextView SRNtext,usernameTextView, emailTextView, phoneTextView, genderTextView, dobTextView, cityTextView, pinTextView, schoolTextView, semTextView, yojTextView;
    private DatabaseReference mDatabase;
    public  StudentProfile()
    {

    }
    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);

////        // Initialize views
        profileImageView = view.findViewById(R.id.profile_image_view);
        usernameTextView = view.findViewById(R.id.username_text_view);
        SRNtext = view.findViewById(R.id.srn_text_view1);
        emailTextView = view.findViewById(R.id.email_text_view);
        phoneTextView = view.findViewById(R.id.phone_text_view);
        genderTextView = view.findViewById(R.id.gender_text_view);
        dobTextView = view.findViewById(R.id.dob_text_view);
        cityTextView = view.findViewById(R.id.city_text_view);
        pinTextView = view.findViewById(R.id.pin_text_view);
        schoolTextView = view.findViewById(R.id.school_text_view);
        semTextView = view.findViewById(R.id.sem_text_view);
        yojTextView = view.findViewById(R.id.yoj_text_view);

        // Retrieve SRN from arguments
        Bundle arguments = getArguments();
        if (arguments != null) {
            String srn = arguments.getString("srn");
            if (srn != null) {
                // Database reference
                mDatabase = FirebaseDatabase.getInstance().getReference("Users Data").child(srn);

                // Retrieve user information from Firebase using SRN
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // Retrieve user data from snapshot
                            String username = snapshot.child("name").getValue(String.class);
                            String email = snapshot.child("mail").getValue(String.class);
                            String phone = snapshot.child("mob").getValue(String.class);
                            String gender = snapshot.child("gender").getValue(String.class);
                            String dob = snapshot.child("dob").getValue(String.class);
                            String city = snapshot.child("city").getValue(String.class);
                            String pin = snapshot.child("pin").getValue(String.class);
                            String school = snapshot.child("school").getValue(String.class);
                            String semester = snapshot.child("sem").getValue(String.class);
                            String yearOfJoining = snapshot.child("yoj").getValue(String.class);

                            // Update UI with retrieved user data
                            if (username != null) usernameTextView.setText(username);
                            if (srn != null) SRNtext.setText(srn);
                            if (email != null) emailTextView.setText(email);
                            if (phone != null) phoneTextView.setText(phone);
                            if (gender != null) genderTextView.setText(gender);
                            if (dob != null) dobTextView.setText(dob);
                            if (city != null) cityTextView.setText(city);
                            if (pin != null) pinTextView.setText(pin);
                            if (school != null) schoolTextView.setText(school);
                            if (semester != null) semTextView.setText(semester);
                            if (yearOfJoining != null) yojTextView.setText(yearOfJoining);

                            // You may set the profile image here if available
                        } else {
                            Log.d("StudentProfileFragment", "User data not found for SRN: " + srn);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("StudentProfileFragment", "Error fetching user data: " + error.getMessage());
                    }
                });
            } else {
                Log.e("StudentProfileFragment", "SRN is null");
            }
        } else {
            Log.e("StudentProfileFragment", "Arguments are null");
        }

        return view;
    }
}
