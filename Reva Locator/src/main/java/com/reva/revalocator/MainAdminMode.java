package com.reva.revalocator;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainAdminMode extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin_mode);

        // Retrieve the intent data
        Intent intent = getIntent();
        String semesterName = intent.getStringExtra("semesterName");
        String sectionName = intent.getStringExtra("sectionName");
        String srn = intent.getStringExtra("srn");

        // Print the log
        Log.d("MainAdminMode", "Semester: " + semesterName);
        Log.d("MainAdminMode", "Section: " + sectionName);
        Log.d("MainAdminMode", "SRN: " + srn);

        // Setup ViewPager and TabLayout
        tabLayout = findViewById(R.id.tabellayot);
        viewPager = findViewById(R.id.viewpage);
        pageViewMessangerAdapter adapter = new pageViewMessangerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, semesterName, sectionName, srn);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
