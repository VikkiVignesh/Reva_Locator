package com.reva.revalocator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Admin_View extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        if (savedInstanceState == null) {
            retrieveDataFromDatabase();
        }
    }

    private void retrieveDataFromDatabase() {
        mDatabase.child("Semesters").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Semester> semesters = new ArrayList<>();

                for (DataSnapshot semesterSnapshot : dataSnapshot.getChildren()) {
                    String semesterName = semesterSnapshot.getKey();
                    List<Section> sections = new ArrayList<>();

                    for (DataSnapshot sectionSnapshot : semesterSnapshot.getChildren()) {
                        String sectionName = sectionSnapshot.getKey();
                        List<Student> students = new ArrayList<>();

                        for (DataSnapshot studentSnapshot : sectionSnapshot.getChildren()) {
                            String srn = studentSnapshot.getKey();
                            students.add(new Student(srn));
                        }

                        sections.add(new Section(sectionName, students));
                    }

                    semesters.add(new Semester(semesterName, sections));
                }

                displaySemesters(semesters);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Admin_View", "loadPost:onCancelled", databaseError.toException());
            }
        });
    }

    private void displaySemesters(List<Semester> semesters) {
        SemesterFragment semesterFragment = new SemesterFragment(semesters);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, semesterFragment);
        transaction.commit();
    }
}
