package com.example.revalocator;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class My_profile extends Fragment {

    private ImageView profileImageView;
    private TextView usernameTextView;
    private Button uploadImageButton;
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;

    private static final int PICK_IMAGE_REQUEST = 1;

    public My_profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        profileImageView = view.findViewById(R.id.profile_image_view);
        usernameTextView = view.findViewById(R.id.username_text_view);
        uploadImageButton = view.findViewById(R.id.upload_image_button);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users Data");
        String uId=getActivity().getIntent().getStringExtra("UserId");
        if (uId != null) {
            // Get user details from Firebase Realtime Database
            mDatabase.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        StringBuilder userDetails = new StringBuilder();
                        // Retrieve the user's details from the dataSnapshot
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String srn = dataSnapshot.child("srn").getValue(String.class);
                        String password = dataSnapshot.child("pass").getValue(String.class);
                        String dob = dataSnapshot.child("dob").getValue(String.class);
                        String department = dataSnapshot.child("school").getValue(String.class);
                        String sem=dataSnapshot.child("sem").getValue(String.class);
                        String mId=dataSnapshot.child("mail").getValue(String.class);
                        String city=dataSnapshot.child("city").getValue(String.class);
                        String sex=dataSnapshot.child("gender").getValue(String.class);


                        // Append the formatted user details to the StringBuilder
                        userDetails.append("Name: ").append(name).append("\n");
                        userDetails.append("\nSRN: ").append(srn).append("\n");
                        userDetails.append("\nMail: ").append(mId).append("\n");
                        userDetails.append("\nPassword: ").append(password).append("\n");
                        userDetails.append("\nDOB: ").append(dob).append("\n");
                        userDetails.append("\nGender: ").append(sex).append("\n");
                        userDetails.append("\nSemester: ").append(sem).append("\n");
                        userDetails.append("\nDepartment: ").append(department).append("\n");
                        userDetails.append("\nCity: ").append(city).append("\n");



                        // Set the formatted user details to the TextView
                        usernameTextView.setText(userDetails.toString());
//                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
//                            userDetails.append(childSnapshot.getKey()).append(": ")
//                                    .append(childSnapshot.getValue()).append("\n");
//                        }
                        usernameTextView.setText(userDetails.toString());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Failed to load user details", Toast.LENGTH_SHORT).show();
                }
            });
        }

        uploadImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        return view;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            profileImageView.setImageURI(imageUri);
            uploadImage();
        }
    }

    private void uploadImage() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && imageUri != null) {
            StorageReference storageRef = storage.getReference().child("profile_images")
                    .child(user.getUid());

            storageRef.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(getContext(), "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
//        } else {
//            // Handle the case where the user is not signed in
//            Toast.makeText(getContext(), "User is not signed in", Toast.LENGTH_SHORT).show();
//        }
    }
}