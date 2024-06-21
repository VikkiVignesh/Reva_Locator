package com.reva.revalocator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SemesterAdapter extends RecyclerView.Adapter<SemesterAdapter.SemesterViewHolder> {

    private List<Semester> semesterList;
    private OnSemesterClickListener onSemesterClickListener;

    public SemesterAdapter(List<Semester> semesterList, OnSemesterClickListener onSemesterClickListener) {
        this.semesterList = semesterList;
        this.onSemesterClickListener = onSemesterClickListener;
    }

    @NonNull
    @Override
    public SemesterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_semester, parent, false);
        return new SemesterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SemesterViewHolder holder, int position) {
        Semester semester = semesterList.get(position);
        holder.semesterName.setText(semester.getName());
        holder.itemView.setOnClickListener(v -> onSemesterClickListener.onSemesterClick(semester));
    }

    @Override
    public int getItemCount() {
        return semesterList.size();
    }

    public static class SemesterViewHolder extends RecyclerView.ViewHolder {
        TextView semesterName;

        public SemesterViewHolder(@NonNull View itemView) {
            super(itemView);
            semesterName = itemView.findViewById(R.id.tvSemester);
        }
    }

    public interface OnSemesterClickListener {
        void onSemesterClick(Semester semester);
    }
}
