package com.example.app01;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder> {

    private List<ProfileItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProfileItem item, int position);
    }

    public ProfileAdapter(List<ProfileItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder {
        public TextView label;
        public TextView value;

        public ProfileViewHolder(View itemView) {
            super(itemView);
            label = itemView.findViewById(R.id.label);
            value = itemView.findViewById(R.id.value);
        }
    }

    @Override
    public ProfileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile, parent, false);
        return new ProfileViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ProfileViewHolder holder, int position) {
        ProfileItem item = items.get(position);
        holder.label.setText(item.getLabel());
        holder.value.setText(item.getValue());

        holder.itemView.setOnClickListener(v -> listener.onItemClick(item, position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
