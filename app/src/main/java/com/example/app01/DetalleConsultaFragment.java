package com.example.app01;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.firestore.FirebaseFirestore;

public class DetalleConsultaFragment extends Fragment {

    private TextView nombreMascotaDetalle;
    private TextView fechaDetalle;
    private TextView veterinarioDetalle;
    private TextView diagnosticoDetalle;
    private TextView tratamientoDetalle;
    private ImageView button;
    private FirebaseFirestore db;
    private String userId, nombreMascota, documentoId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalle_consulta, container, false);

        // Obtener argumentos desde el fragmento anterior
        if (getArguments() != null) {
            userId = getArguments().getString("userId");
            nombreMascota = getArguments().getString("nombreMascota");
            documentoId = getArguments().getString("documentoId");
        }

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializar los TextViews
        nombreMascotaDetalle = view.findViewById(R.id.nombreMascotaDetalle);
        fechaDetalle = view.findViewById(R.id.fechaDetalle);
        veterinarioDetalle = view.findViewById(R.id.veterinarioDetalle);
        diagnosticoDetalle = view.findViewById(R.id.diagnosticoDetalle);
        tratamientoDetalle = view.findViewById(R.id.tratamientoDetalle);

        // Configurar el botón para regresar al fragmento anterior
        button = view.findViewById(R.id.back1);
        button.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            fragmentManager.popBackStack(); // Regresar al fragmento anterior
        });

        // Cargar los datos de la consulta
        cargarDatosConsulta();

        return view;
    }

    private void cargarDatosConsulta() {
        if (userId != null && nombreMascota != null && documentoId != null) {
            db.collection("users")
                    .document(userId)
                    .collection("mascotas")
                    .document(nombreMascota)
                    .collection("consultas")
                    .document(documentoId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String fecha = documentSnapshot.getString("fecha");
                            String veterinario = documentSnapshot.getString("veterinario");
                            String diagnostico = documentSnapshot.getString("diagnostico");
                            String tratamiento = documentSnapshot.getString("tratamiento");

                            nombreMascotaDetalle.setText(nombreMascota);
                            fechaDetalle.setText(fecha);
                            veterinarioDetalle.setText(veterinario);
                            diagnosticoDetalle.setText(diagnostico);
                            tratamientoDetalle.setText(tratamiento);
                        } else {
                            Toast.makeText(getContext(), "No se encontraron datos para esta consulta.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error al cargar los datos de la consulta.", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(getContext(), "Datos incompletos para cargar la consulta.", Toast.LENGTH_SHORT).show();
        }
    }
}
