package com.example.app01;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class RecordatorioFragment extends Fragment {
    private CalendarView cal;
    private SharedPreferences sharedPreferences;
    Button b1;
    private String userId;
    private String nombreMascota;
    private EditText campo1, campo2, campo3, campo4;
    private FirebaseFirestore db;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recordatorio, container, false);
        FirebaseApp.initializeApp(requireContext());
        db = FirebaseFirestore.getInstance();
        // Referencia a los EditText
        campo1 = view.findViewById(R.id.name);
        campo2 = view.findViewById(R.id.cantidad);
        campo3 = view.findViewById(R.id.intervalo);
        campo4 = view.findViewById(R.id.desc);
        cal = view.findViewById(R.id.cal1);


        Bundle args = getArguments();
        String fechaRecibida = null;
        if (args != null) {
            fechaRecibida = args.getString("fecha_seleccionada");
        }

        // Formato para parsear la fecha
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        // Aquí puedes usar la fecha recibida como lo necesites
        if (fechaRecibida != null) {
            try {
                // Parsear la fecha
                Date fecha = sdf1.parse(fechaRecibida);

                // Convertir la fecha a milisegundos (Unix timestamp)
                if (fecha != null) {
                    long fechaEnMilisegundos = fecha.getTime();

                    // Establecer la fecha en el CalendarView
                    cal.setDate(fechaEnMilisegundos, true, true);  // Con animación
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }


        if (getArguments() != null) {
            nombreMascota = getArguments().getString("nombreMascota");
            userId = getArguments().getString("userId");
        }




        // Botón para guardar los datos
        Button saveButton = view.findViewById(R.id.buttonGuardar);
        saveButton.setOnClickListener(v -> {
            // Obtener los valores de los EditText
            String name = campo1.getText().toString();
            String cantidad = campo2.getText().toString();
            String intervalo = campo3.getText().toString();
            String desc = campo4.getText().toString();

            // Obtener la fecha seleccionada en el CalendarView en milisegundos
            long selectedDateMillis = cal.getDate();

            // Formato para la fecha: "yyyy-MM-dd HH:mm:ss"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

            // Convertir la fecha seleccionada a este formato
            String fechaFormateada = sdf.format(new Date(selectedDateMillis));

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(cantidad) && !TextUtils.isEmpty(intervalo) && !TextUtils.isEmpty(desc)) {

                // Crear un objeto Recordatorio con la fecha formateada en "yyyy-MM-dd HH:mm:ss" y en milisegundos
                Rtext2 recordatorio = new Rtext2(name, cantidad, intervalo, desc, fechaFormateada);

                // Guardar en Firestore bajo la estructura "user/{userId}/mascotas/{nombreMascota}/recordatorios"
                db.collection("users").document(userId)  // Asegúrate de que userId no sea nulo
                        .collection("mascotas")
                        .document(nombreMascota)  // Asegúrate de que nombreMascota no sea nulo
                        .collection("recordatorios")
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
        campo1.setText("");
        campo2.setText("");
        campo3.setText("");
        campo4.setText("");
    }
    private void cargarFragmento(Fragment fragment) {
        // Cambiar al fragmento Principal1Fragment
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)  // Opcional: para que el usuario pueda volver atrás
                .commit();
    }
}
