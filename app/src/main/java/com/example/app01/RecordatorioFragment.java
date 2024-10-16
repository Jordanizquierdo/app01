package com.example.app01;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class RecordatorioFragment extends Fragment {

    private EditText etNombre, etCantidad, etIntervalo, etDescripcion;
    private Button btnGuardar;
    private DatabaseReference recordatorioDB;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recordatorio, container, false);
        FirebaseApp.initializeApp(requireContext());

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Referenciar los elementos del layout
        etNombre = view.findViewById(R.id.name);
        etCantidad = view.findViewById(R.id.cantidad);
        etIntervalo = view.findViewById(R.id.intervalo);
        etDescripcion = view.findViewById(R.id.desc);
        btnGuardar = view.findViewById(R.id.buttonGuardar);

        // Inicializar Firebase Realtime Database (opcional, si también quieres usar Realtime Database)
        recordatorioDB = FirebaseDatabase.getInstance().getReference("recordatorios");

        // Listener para el botón de guardar
        btnGuardar.setOnClickListener(v -> {
            String nombre = etNombre.getText().toString().trim();
            String cantidad = etCantidad.getText().toString().trim();
            String intervalo = etIntervalo.getText().toString().trim();
            String descripcion = etDescripcion.getText().toString().trim();

            if (!TextUtils.isEmpty(nombre) && !TextUtils.isEmpty(cantidad) && !TextUtils.isEmpty(intervalo) && !TextUtils.isEmpty(descripcion)) {
                // Crear un objeto Recordatorio
                Rtext recordatorio = new Rtext(nombre, cantidad, intervalo, descripcion);

                // Guardar en Firebase Realtime Database (opcional)
                String id = recordatorioDB.push().getKey(); // Genera un ID único para cada recordatorio
                recordatorioDB.child(id).setValue(recordatorio); // Almacenar en Realtime Database

                // Guardar en Firestore
                db.collection("recordatorios")
                        .add(recordatorio)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getActivity(), "Recordatorio guardado en Firestore", Toast.LENGTH_SHORT).show();
                            limpiarCampos();
                            // Cambiar al fragmento Principal1Fragment después de guardar
                            cargarFragmento(new Principal1Fragment());
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Error al guardar en Firestore: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(getActivity(), "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void limpiarCampos() {
        etNombre.setText("");
        etCantidad.setText("");
        etIntervalo.setText("");
        etDescripcion.setText("");
    }

    private void cargarFragmento(Fragment fragment) {
        // Cambiar al fragmento Principal1Fragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)  // Opcional: para que el usuario pueda volver atrás
                .commit();
    }
}
