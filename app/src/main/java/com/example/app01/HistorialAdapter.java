package com.example.app01;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app01.Consulta;
import com.example.app01.R;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    private List<Consulta> consultas;
    private Context context;
    private OnItemClickListener listener;

    public HistorialAdapter(List<Consulta> consultas, Context context) {
        this.consultas = consultas;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Consulta consulta = consultas.get(position);
        holder.bind(consulta);
    }

    @Override
    public int getItemCount() {
        return consultas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nombreMascota, fecha;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreMascota = itemView.findViewById(R.id.nombreMascota);
            fecha = itemView.findViewById(R.id.fecha);
        }

        public void bind(final Consulta consulta) {
            nombreMascota.setText(consulta.getNombreMascota());
            fecha.setText(consulta.getFecha());

            //Listener para manejar el clic
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Crear Intent para ir a DetalleConsultaActivity
                    Intent intent = new Intent(context, DetalleConsultaActivity.class);
                    intent.putExtra("nombreMascota", consulta.getNombreMascota());
                    intent.putExtra("fecha", consulta.getFecha());
                    intent.putExtra("veterinario", "Francisco Tapia");  // Puedes cambiar a valores dinámicos si los tienes
                    intent.putExtra("diagnostico", "La mascota cuenta con malformaciones vertebrales congénitas");
                    intent.putExtra("tratamiento", "- Suplemento articular 1 cápsula/8 horas\n- Jarabe Rostrum 2ml/6 horas");

                    // Iniciar la actividad
                    context.startActivity(intent);
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Consulta consulta);
    }

    public HistorialAdapter(List<Consulta> consultas, Context context, OnItemClickListener listener) {
        this.consultas = consultas;
        this.context = context;
        this.listener = listener;
    }
}
