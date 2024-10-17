package com.example.app01;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app01.Horario;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.TimeSlotViewHolder> {

    private List<Horario> timeSlots;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Horario horario);
    }

    public TimeSlotAdapter(List<Horario> timeSlots, OnItemClickListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_slot, parent, false);
        return new TimeSlotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeSlotViewHolder holder, int position) {
        Horario horario = timeSlots.get(position);
        holder.bind(horario, listener);
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    public static class TimeSlotViewHolder extends RecyclerView.ViewHolder {
        private TextView timeSlotTextView;

        public TimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            timeSlotTextView = itemView.findViewById(R.id.timeSlotTextView);
        }

        public void bind(Horario horario, OnItemClickListener listener) {
            timeSlotTextView.setText(horario.getFormattedTimeSlot());
            itemView.setOnClickListener(v -> listener.onItemClick(horario));
        }
    }
}
