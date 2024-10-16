package com.example.app01;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MascotaAdapter extends RecyclerView.Adapter<MascotaAdapter.MascotaViewHolder> {

    private List<String> listaMascotas;
    private OnMascotaClickListener listener;

    public MascotaAdapter(List<String> listaMascotas, OnMascotaClickListener listener) {
        this.listaMascotas = listaMascotas;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MascotaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        return new MascotaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MascotaViewHolder holder, int position) {
        String nombreMascota = listaMascotas.get(position);
        holder.tvMascota.setText(nombreMascota);
        holder.itemView.setOnClickListener(v -> listener.onMascotaClick(nombreMascota));
    }

    @Override
    public int getItemCount() {
        return listaMascotas.size();
    }

    public static class MascotaViewHolder extends RecyclerView.ViewHolder {
        TextView tvMascota;

        public MascotaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMascota = itemView.findViewById(android.R.id.text1);
        }
    }

    public interface OnMascotaClickListener {
        void onMascotaClick(String nombreMascota);
    }
}
