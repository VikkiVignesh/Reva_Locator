package com.reva.revalocator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SectionAdapter extends RecyclerView.Adapter<SectionAdapter.SectionViewHolder> {

    private List<Section> sectionList;
    private OnSectionClickListener onSectionClickListener;

    public SectionAdapter(List<Section> sectionList, OnSectionClickListener onSectionClickListener) {
        this.sectionList = sectionList;
        this.onSectionClickListener = onSectionClickListener;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        Section section = sectionList.get(position);
        holder.sectionName.setText(section.getName());
        holder.itemView.setOnClickListener(v -> onSectionClickListener.onSectionClick(section));
    }

    @Override
    public int getItemCount() {
        return sectionList.size();
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder {
        TextView sectionName;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            sectionName = itemView.findViewById(R.id.tvSection);
        }
    }

    public interface OnSectionClickListener {
        void onSectionClick(Section section);
    }
}
