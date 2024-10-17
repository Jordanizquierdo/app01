package com.example.app01;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistorialFragment extends Fragment {
    private RecyclerView recyclerView;
    private HistorialAdapter adapter;
    private List<Consulta> consultas;
    private ImageView button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historial, container, false);

        // Inicializar el RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        consultas = new ArrayList<>();

        // Agregar consultas (puedes modificar estos datos o cargarlos dinámicamente)
        consultas.add(new Consulta("Firulais", "5/10/2023"));
        consultas.add(new Consulta("Firulais", "2/3/2024"));
        consultas.add(new Consulta("Rony", "5/3/2024"));

        // Inicializar el adaptador con el listener de clic y contexto
        adapter = new HistorialAdapter(consultas, getContext(), new HistorialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Consulta consulta) {
                // Acción al hacer clic en un ítem
                Toast.makeText(getContext(), "Consulta de: " + consulta.getNombreMascota(), Toast.LENGTH_SHORT).show();
            }
        });

        // Configurar el adaptador en el RecyclerView
        recyclerView.setAdapter(adapter);

        // Configurar el botón para regresar a la actividad anterior
        button = view.findViewById(R.id.back_login2);
        button.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new Principal1Fragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }
}
