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

public class SemesterFragment extends Fragment {

    private RecyclerView recyclerView;
    private SemesterAdapter adapter;
    private List<Semester> semesterList;

    public SemesterFragment(List<Semester> semesterList) {
        this.semesterList = semesterList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semester, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SemesterAdapter(semesterList, this::openSectionFragment);
        recyclerView.setAdapter(adapter);
        return view;
    }

    private void openSectionFragment(Semester semester) {
        SectionFragment sectionFragment = new SectionFragment(semester.getSections());
        Bundle bundle = new Bundle();
        bundle.putString("semesterName", semester.getName());
        sectionFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, sectionFragment)
                .addToBackStack(null)
                .commit();
    }
}
