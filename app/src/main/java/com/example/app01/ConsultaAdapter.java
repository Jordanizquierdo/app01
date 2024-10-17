package com.example.app01;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
public class ConsultaAdapter extends RecyclerView.Adapter<ConsultaAdapter.ConsultaViewHolder> {

    private List<Consulta> listaConsultas;
    private OnConsultaClickListener listener;

    public interface OnConsultaClickListener {
        void onConsultaClick(Consulta consulta);
    }

    public ConsultaAdapter(List<Consulta> listaConsultas, OnConsultaClickListener listener) {
        this.listaConsultas = listaConsultas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConsultaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.historial_item, parent, false);
        return new ConsultaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsultaViewHolder holder, int position) {
        Consulta consulta = listaConsultas.get(position);
        holder.bind(consulta);
    }

    @Override
    public int getItemCount() {
        return listaConsultas.size();
    }

    class ConsultaViewHolder extends RecyclerView.ViewHolder {

        private TextView fechaConsulta;
        private TextView veterinarioConsulta;

        public ConsultaViewHolder(@NonNull View itemView) {
            super(itemView);
            fechaConsulta = itemView.findViewById(R.id.fechaConsulta);
            veterinarioConsulta = itemView.findViewById(R.id.veterinarioConsulta);
        }

        public void bind(Consulta consulta) {
            fechaConsulta.setText(consulta.getFecha());
            veterinarioConsulta.setText(consulta.getVeterinario());

            // Al hacer clic en un item, se pasará el objeto Consulta completo con su ID
            itemView.setOnClickListener(v -> listener.onConsultaClick(consulta));
        }
    }
}
