package com.reva.revalocator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private List<Student> studentList;
    private OnStudentClickListener onStudentClickListener;

    public StudentAdapter(List<Student> studentList, OnStudentClickListener onStudentClickListener) {
        this.studentList = studentList;
        this.onStudentClickListener = onStudentClickListener;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.srn.setText(student.getSrn());
        holder.itemView.setOnClickListener(v -> onStudentClickListener.onStudentClick(student));
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView srn;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);
            srn = itemView.findViewById(R.id.tvSrn);
        }
    }

    public interface OnStudentClickListener {
        void onStudentClick(Student student);
    }
}
