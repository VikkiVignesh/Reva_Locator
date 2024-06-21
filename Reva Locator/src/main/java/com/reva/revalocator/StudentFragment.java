package com.reva.revalocator;

import android.content.Intent;
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

public class StudentFragment extends Fragment {

    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student> studentList;

    public StudentFragment(List<Student> studentList) {
        this.studentList = studentList;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_student, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new StudentAdapter(studentList, this::openMainAdminMode);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void openMainAdminMode(Student student) {
        if (getArguments() != null) {
            String semesterName = getArguments().getString("semesterName");
            String sectionName = getArguments().getString("sectionName");

            Intent intent = new Intent(getActivity(), MainAdminMode.class);
            intent.putExtra("semesterName", semesterName);
            intent.putExtra("sectionName", sectionName);
            intent.putExtra("srn", student.getSrn());
            startActivity(intent);
        }
    }
}
