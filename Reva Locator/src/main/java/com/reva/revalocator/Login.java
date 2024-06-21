package com.reva.revalocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
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
    String userId, section, semester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btn = findViewById(R.id.log_in);
        username = findViewById(R.id.logmail);
        password = findViewById(R.id.logpswrd);
        myFire = FirebaseDatabase.getInstance();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srn = username.getEditText().getText().toString().trim();
                String logpaswd = password.getEditText().getText().toString().trim();

                // Check for default admin credentials
                if (srn.equals("AD21R369") && logpaswd.equals("RevaAdmin@369")) {
                    Toast.makeText(Login.this, "Admin Credentials Matched !!", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(Login.this, Admin_View.class);
                    startActivity(i);
                    return; // Exit the method after admin login
                }

                myDb = myFire.getReference("Users Data");
                myDb.orderByChild("srn").equalTo(srn).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // SRN exists in the database
                            boolean passwordMatched = false;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String passwordFromDb = snapshot.child("pass").getValue(String.class);
                                if (passwordFromDb != null && passwordFromDb.equals(logpaswd)) {
                                    passwordMatched = true;
                                    userId = snapshot.getKey();
                                    section = snapshot.child("section").getValue(String.class);
                                    semester = snapshot.child("sem").getValue(String.class);
                                    break;
                                }
                            }
                            if (passwordMatched) {
                                Toast.makeText(Login.this, "Credentials Matched !!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(Login.this, MainActivity.class);
                                i.putExtra("UserId", userId);
                                i.putExtra("srn", srn);
                                i.putExtra("section", section);
                                i.putExtra("semester", semester);
                                startActivity(i);
                            } else {
                                Toast.makeText(Login.this, "Password Not matched", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login.this, "Invalid SRN. SRN does not Exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(Login.this, "Error checking SRN existence. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
