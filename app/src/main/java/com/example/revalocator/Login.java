package com.example.revalocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    Button btn;
    FirebaseDatabase myFire;
    DatabaseReference myDb;
    TextInputLayout username, password;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn=findViewById(R.id.log_in);
        username =findViewById(R.id.logmail);
        password = findViewById(R.id.logpswrd);
        myFire=FirebaseDatabase.getInstance();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srn = username.getEditText().getText().toString().trim();
                String logpaswd = password.getEditText().getText().toString().trim();



                myDb=myFire.getReference("Users Data");
                myDb.orderByChild("srn").equalTo(srn).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Email exists in the database
                            // Now, iterate through the dataSnapshot to find the user with the given email
                            boolean passwordMatched = false; // Flag to indicate if password matched
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                // Retrieve the user's password from the database
                                String passwordFromDb = snapshot.child("pass").getValue(String.class);
                                // Check if the retrieved password matches the one provided by the user
                                if (passwordFromDb != null && passwordFromDb.equals(logpaswd)) {
                                    // Password matches, authentication successful
                                    passwordMatched = true;
                                    userId = snapshot.getKey();
                                    break; // Exit the loop as authentication is successful
                                }
                            }
                            if (passwordMatched) {
                                Toast.makeText(Login.this, "Credentials Matched !!", Toast.LENGTH_SHORT).show();

                                Intent i=new Intent(Login.this,MainActivity.class);
                                i.putExtra("UserId",userId);
                                startActivity(i);
                            } else {
                                Toast.makeText(Login.this, "Password Not matched", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Email does not exist in the database
                            Toast.makeText(Login.this, "Invalid email.Email does not Exists", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle any errors
                        Toast.makeText(Login.this, "Error checking email existence. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });






            }
        });
    }
}