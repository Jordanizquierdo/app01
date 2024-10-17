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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class HistorialFragment extends Fragment {

    private FirebaseFirestore db;
    private String userId, nombreMascota;
    private RecyclerView recyclerView;
    private ConsultaAdapter consultaAdapter;
    private List<Consulta> listaConsultas = new ArrayList<>();

    private ImageView button1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historial, container, false);

        // Obtener los argumentos pasados desde Principal1Fragment
        if (getArguments() != null) {
            userId = getArguments().getString("documentoId");
            nombreMascota = getArguments().getString("nombreMascota");
        }

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Configurar RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        consultaAdapter = new ConsultaAdapter(listaConsultas, this::mostrarDetalleConsulta);
        recyclerView.setAdapter(consultaAdapter);

        // Cargar las consultas desde Firebase
        cargarConsultasDesdeFirebase();



        button1 = view.findViewById(R.id.back_login2);

        button1.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new Principal1Fragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
        return view;

    }

    private void cargarConsultasDesdeFirebase() {
        if (userId != null && nombreMascota != null) {
            db.collection("users")
                    .document(userId)
                    .collection("mascotas")
                    .document(nombreMascota)
                    .collection("consultas")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        listaConsultas.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Consulta consulta = document.toObject(Consulta.class);
                            consulta.setDocumentoId(document.getId()); // Guardar el ID del documento
                            listaConsultas.add(consulta);
                        }
                        consultaAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error al cargar consultas", Toast.LENGTH_SHORT).show();
                    });
        }
    }

    // Método para mostrar los detalles de una consulta
    private void mostrarDetalleConsulta(Consulta consulta) {
        DetalleConsultaFragment detalleConsultaFragment = new DetalleConsultaFragment();
        Bundle bundle = new Bundle();

        bundle.putString("userId", userId);
        bundle.putString("nombreMascota", nombreMascota);
        bundle.putString("documentoId", consulta.getDocumentoId()); // Aquí pasas el ID del documento

        detalleConsultaFragment.setArguments(bundle);

        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, detalleConsultaFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
