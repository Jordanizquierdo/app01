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
        public TextView tvNombre, tvCantidad, tvIntervalo, tvDescripcion, tvMascota;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.name);
            tvCantidad = itemView.findViewById(R.id.cantidad);
            tvIntervalo = itemView.findViewById(R.id.intervalo);
            tvDescripcion = itemView.findViewById(R.id.desc);
            tvMascota = itemView.findViewById(R.id.nombreMascota);  // Añadir un TextView para mostrar la mascota si es necesario
        }
    }

    @Override
    public RecordatorioAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Rtext recordatorio = recordatorioList.get(position);
        holder.tvNombre.setText(recordatorio.getNombre());
        holder.tvCantidad.setText(recordatorio.getCantidad());
        holder.tvIntervalo.setText(recordatorio.getIntervalo());
        holder.tvDescripcion.setText(recordatorio.getDescripcion());
        holder.tvMascota.setText(recordatorio.getMascota());  // Mostrar el nombre de la mascota si lo necesitas
    }

    @Override
    public int getItemCount() {
        return recordatorioList.size();
    }
}
