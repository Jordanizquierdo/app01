package com.example.app01;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecordatorioAdapter extends RecyclerView.Adapter<RecordatorioAdapter.ViewHolder> {
    public List<Rtext> recordatorioList;

    public RecordatorioAdapter(List<Rtext> recordatorioList) {
        this.recordatorioList = recordatorioList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate, tvTime, tvTitle, tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date_bar);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTitle = itemView.findViewById(R.id.tv_name);
            tvDescription = itemView.findViewById(R.id.tv_description);
        }
    }

    @Override
    public RecordatorioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);  // Cambia 'card_recordatorio' si el nombre del archivo es diferente
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Rtext recordatorio = recordatorioList.get(position);
        holder.tvDate.setText(recordatorio.getDate());
        holder.tvTime.setText(recordatorio.getTime());
        holder.tvTitle.setText(recordatorio.getTitle());
        holder.tvDescription.setText(recordatorio.getDescription());
    }

    @Override
    public int getItemCount() {
        return recordatorioList.size();
    }
}
