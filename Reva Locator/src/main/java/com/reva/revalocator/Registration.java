package com.reva.revalocator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;



import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.content.Intent;
import android.os.Handler;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Registration extends AppCompatActivity implements AdapterView.OnItemSelectedListener,LocationListener {

    double latitude ,longitude;
    TextInputLayout Namelyt,Srnlyt,Maillyt,Passlyt,Cfrlyt,Moblyt,Citylyt,Pinlyt,Doblyt ,yoj;
    Spinner Glst,Schlst,Semlst,sectionss;
    Button login,register;
    FirebaseDatabase fireDb;
    //FirebaseAuth myAuth;
    DatabaseReference Dbrefer;
    String formattedAddress;
    final String sex[]={"Select Gender","Male","Female"};
    final String Sems[]={"0","1","2","3","4","5","6","7","8"};
    final String Schools[]={"Select School","School of CSE","School of MECH","School of CIVIL","School of C&IT","School of EEE","School of ECE",
    "School of Aeronutical","School of Leagal Studies","School of Commerce"};
    final String[] SectionS ={"Section A","Section B","Section C","Section D","Section E","Section F","Section G","Section H","Section I","Section J"};
    private  String gender="",school="",currsem="",Section="";
    private LocationManager locationManager;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize LocationManager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Checking For Internet Connection
        if (!isInternetAvailable()) {
            // Internet is not available, prompt the user to turn on mobile data
            Toast.makeText(this, "Please turn on mobile data", Toast.LENGTH_SHORT).show();
            showEnableDataDialog();
            // You can also show a dialog or an AlertDialog to prompt the user to turn on mobile data
        }
            setContentView(R.layout.activity_registration);
            Namelyt = findViewById(R.id.name);
            Srnlyt = findViewById(R.id.srn);
            Maillyt = findViewById(R.id.mail);
            Passlyt = findViewById(R.id.pswrd);
            Cfrlyt = findViewById(R.id.cnfrpswrd);
            Moblyt = findViewById(R.id.mob);
            Glst = findViewById(R.id.gender);
            Citylyt = findViewById(R.id.city);
            Pinlyt = findViewById(R.id.pin);
            Doblyt = findViewById(R.id.dob);
            yoj = findViewById(R.id.yoj);
            Semlst = findViewById(R.id.sem);
            Schlst = findViewById(R.id.school);
            sectionss = findViewById(R.id.section);
            login = findViewById(R.id.sign_In);
            register = findViewById(R.id.regi);

            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sex);
            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Schools);
            ArrayAdapter<String> adapter4 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, SectionS);
            ArrayAdapter<String> adapter3 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Sems);

            Glst.setAdapter(adapter1);
            Schlst.setAdapter(adapter2);
            Semlst.setAdapter(adapter3);
            sectionss.setAdapter(adapter4);

            Doblyt.setEndIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(Doblyt);
                }
            });
            yoj.setEndIconOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog(yoj);
                }
            });
            Glst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0)
                        gender = sex[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            Schlst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0)
                        school = Schools[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        sectionss.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0)
                    Section = SectionS[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
            Semlst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0)
                        currsem = Sems[position];
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(Registration.this, Login.class);
                    startActivity(i);
                }
            });
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validate();
                }
            });
    }

    private void validate()
    {
        String name=Namelyt.getEditText().getText().toString().trim();
        String srn=Srnlyt.getEditText().getText().toString().trim();
        String pass=Passlyt.getEditText().getText().toString().trim();
        String cfrpass=Cfrlyt.getEditText().getText().toString().trim();
        String mail=Maillyt.getEditText().getText().toString().trim();
        String mob=Moblyt.getEditText().getText().toString().trim();
        String city=Citylyt.getEditText().getText().toString().trim();
        String pin=Pinlyt.getEditText().getText().toString().trim();
        String Dob=Doblyt.getEditText().getText().toString().trim();
        String Yoj=yoj.getEditText().getText().toString().trim();
        String SRN = srn.toUpperCase();

        if(name.isEmpty())
        {Namelyt.setError("Field Required");}
        else if (!name.matches("^[a-zA-Z]+")) {
            Namelyt.setError("Numbers are not Allowed!!");
        } else if (name.length()<3) {
            Namelyt.setError("Length should be atleast 3");
        } else {
            Namelyt.setError(null);
        }

        if(SRN.isEmpty())
        {Srnlyt.setError("Field Required");
        } else if (!SRN.matches("^[rR][0-9]{2}[a-fA-F0-9]{5}$")) {
            Srnlyt.setError("Srn is not proper");
        }
        else {

            Srnlyt.setError(null);
        }

        if(mail.isEmpty())
        {Maillyt.setError("Field Required");
        }
        else if (!mail.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            Maillyt.setError("Invalid mail...");
        }
        else
        {Maillyt.setError(null);
        }

        if(pass.isEmpty())
        {
            Passlyt.setError("Field Required");
        }
        else if (pass.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")) {
            Passlyt.setError("Password is Strong");
        } else if (pass.length()<6) {
            Passlyt.setError("Password length should be atleast 6");
        }
        else {
            Passlyt.setError(null);
        }

        if(cfrpass.isEmpty())
        {
            Cfrlyt.setError("Field Required");
        }
        else if (!cfrpass.equals(pass)) {
            Cfrlyt.setError("password does not match with original one");
        }
        else {
            Cfrlyt.setError(null);
        }


        if(mob.isEmpty())
        {
            Moblyt.setError("Field Required");
        }else if (!mob.matches("^[6-9][0-9]{9}$")) {
            Moblyt.setError("Invalid mobile number...");
        }  else {
            Moblyt.setError(null);
        }

        if(city.isEmpty())
        {
            Citylyt.setError("Field Required");
        }
        else {
            Citylyt.setError(null);
        }

        if(Dob.isEmpty())
        {
            Doblyt.setError("Field Required");
        }
        else {
            Doblyt.setError(null);
        }
        if(Yoj.isEmpty())
        {
            Doblyt.setError("Field Required");
        }
        else {
            Doblyt.setError(null);
        }


        if(pin.isEmpty())
        {
            Pinlyt.setError("Field Required");
        } else if (!pin.matches("^[0-9]{6}$")) {
            Pinlyt.setError("Invalid PinCode");
        }
        else {
            Pinlyt.setError(null);
        }

        if(gender.isEmpty())
        {
            Toast.makeText(this, "Select Proper Gender", Toast.LENGTH_SHORT).show();
        }

        if(school.isEmpty())
        {
            Toast.makeText(this, "Select proper School Name", Toast.LENGTH_SHORT).show();
        }
        if(Section.isEmpty())
        {
            Toast.makeText(this, "Select proper Section", Toast.LENGTH_SHORT).show();
        }
        if(currsem.isEmpty())
        {
            Toast.makeText(this, "Select correct Semester", Toast.LENGTH_SHORT).show();
        }
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                String fullAddress = address.getAddressLine(0);
                String City = address.getLocality();
                String state = address.getAdminArea();
                String country = address.getCountryName();
                String postalCode = address.getPostalCode();
                String knownName = address.getFeatureName();

                // Display the address
                formattedAddress = String.format("%s, %s, %s, %s, %s", fullAddress, city, state, country, postalCode);
                //Toast.makeText(this, formattedAddress, Toast.LENGTH_LONG).show();
            } else {
                //Toast.makeText(this, "No address found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error retrieving address", Toast.LENGTH_SHORT).show();
        }
        if(Namelyt.getError()==null && Srnlyt.getError()==null && Maillyt.getError()==null && Passlyt.getError()==null && Cfrlyt.getError()==null && Moblyt.getError()==null && Citylyt.getError()==null && Pinlyt.getError()==null && Doblyt.getError()==null && yoj.getError()==null && !gender.isEmpty() && !school.isEmpty() && !currsem.isEmpty())
        {    Users user =new Users(name, SRN ,pass ,cfrpass,mail,mob ,city,pin , Dob ,Yoj,currsem,gender,school,formattedAddress,Section);
            fireDb=FirebaseDatabase.getInstance(); //Creating instance of Firebase DB

            Dbrefer=fireDb.getReference("Users Data"); //Creating Database Refernces
            Dbrefer.orderByChild("mail").equalTo(mail).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Email already exists in the database
                        Toast.makeText(Registration.this, "Admin Email already registered. Please use a different email.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Email does not exist in the database, check for SRN and mobile number
                        Dbrefer.orderByChild("srn").equalTo(srn).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    // SRN already exists in the database
                                    Toast.makeText(Registration.this, "SRN already registered. Please use a different SRN.", Toast.LENGTH_SHORT).show();
                                } else {
                                    // SRN does not exist in the database, check for mobile number
                                    Dbrefer.orderByChild("mob").equalTo(mob).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists()) {
                                                // Mobile number already exists in the database
                                                Toast.makeText(Registration.this, "Mobile number already registered. Please use a different mobile number.", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // SRN and mobile number are unique, proceed with registration
                                                Datastorage(user);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            Toast.makeText(Registration.this, "Error checking mobile number existence. Please try again.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(Registration.this, "Error checking SRN existence. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Registration.this, "Error checking email existence. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });



        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private void Datastorage(Users user)
    {

        Dbrefer.child(user.srn).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(Registration.this, "Storing Admin Data.....", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Registration.this, "Registartion Successfull !!!", Toast.LENGTH_SHORT).show();
                        Intent i =new Intent(Registration.this,Login.class);
                        startActivity(i);
                    }
                },10000 );
            }
        });
    }

    public static boolean isStartDateAfterEndDate(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date StartDate = sdf.parse(startDate);
            Date EndDate = sdf.parse(endDate);
            Date currentDate = new Date();

            boolean isStartDateAfterEndDate = EndDate.after(StartDate);
            boolean isStartDateAfterCurrentDate = StartDate.after(currentDate);

            // If both conditions are correct, return true
            if (isStartDateAfterEndDate && isStartDateAfterCurrentDate) {
                return false;
            } else {
                // Otherwise, return false
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle parse exception
        }

        // Start date is not greater than end date
        return  true;
    }
    private void showDatePickerDialog(TextInputLayout dateEditText) {

        DialogFragment newFragment = new DatePickerFragment(dateEditText);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        // Handle location change
         latitude = location.getLatitude();
         longitude = location.getLongitude();


    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }


    // Method to check internet connectivity
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    private void showEnableDataDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enable Mobile Data");
        builder.setMessage("To use this app, please turn on your mobile data.");

        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Open settings to enable mobile data
                startActivity(new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
                // You can choose to finish() the activity here or handle it in another way
                finish();
            }
        });

        // Create the AlertDialog
        AlertDialog dialog = builder.create();

        // Show the dialog
        dialog.show();
    }
}