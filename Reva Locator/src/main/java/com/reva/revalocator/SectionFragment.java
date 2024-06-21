package com.reva.revalocator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SectionFragment extends Fragment {

    private RecyclerView recyclerView;
    private SectionAdapter adapter;
    private List<Section> sectionList;

    public SectionFragment(List<Section> sectionList) {
        this.sectionList = sectionList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new SectionAdapter(sectionList, this::openStudentFragment);
        recyclerView.setAdapter(adapter);

        if (getArguments() != null) {
            String semesterName = getArguments().getString("semesterName");
            // Use semesterName as needed
        }

        return view;
    }

    private void openStudentFragment(Section section) {
        StudentFragment studentFragment = new StudentFragment(section.getStudents());
        Bundle bundle = new Bundle();
        bundle.putString("sectionName", section.getName());
        if (getArguments() != null) {
            bundle.putString("semesterName", getArguments().getString("semesterName"));
        }
        studentFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, studentFragment)
                .addToBackStack(null)
                .commit();
    }
}
