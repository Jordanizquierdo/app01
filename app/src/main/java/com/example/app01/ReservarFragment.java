package com.example.app01;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ReservarFragment extends Fragment {

    public CalendarView calendarView;
    public RecyclerView timeSlotsRecyclerView;
    public Button confirmButton;
    public List<Horario> availableTimeSlots;
    public ImageView button1;
    public Horario selectedTimeSlot = null;
    private FirebaseFirestore db;
    private String selectedDate;

    private String userId;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_reservar, container, false);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Inicializar vistas
        calendarView = view.findViewById(R.id.calendarView);
        timeSlotsRecyclerView = view.findViewById(R.id.timeSlotsRecyclerView);
        confirmButton = view.findViewById(R.id.confirmButton);

        // Inicializar la lista de horarios disponibles
        availableTimeSlots = new ArrayList<>();

        // Configurar el RecyclerView
        timeSlotsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TimeSlotAdapter adapter = new TimeSlotAdapter(availableTimeSlots, horario -> {
            selectedTimeSlot = horario;
        });
        timeSlotsRecyclerView.setAdapter(adapter);

        // Cargar horarios del día actual al iniciar el fragmento
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        selectedDate = sdf.format(new Date());
        cargarHorasDisponiblesDesdeFirebase(selectedDate);

        // Listener para cuando se selecciona una fecha en el CalendarView
        calendarView.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year; // Formatear la fecha seleccionada
            cargarHorasDisponiblesDesdeFirebase(selectedDate);
        });
        userId = getArguments().getString("userId");


        // Botón de confirmar
        confirmButton.setOnClickListener(v -> {
            if (selectedTimeSlot != null) {
                // Lógica para obtener la información del usuario

                // Crear el documento de la reserva
                Map<String, Object> reserva = new HashMap<>();
                reserva.put("fecha", selectedDate);
                reserva.put("hora", selectedTimeSlot);
                reserva.put("userId", userId);
                reserva.put("estado", "Esperando"); // Estado de la reserva

                // Guardar en Firestore
                db.collection("reservas") // Reemplaza "reservas" con el nombre de la colección donde quieres guardar las reservas
                        .add(reserva)
                        .addOnSuccessListener(documentReference -> {
                            Toast.makeText(getContext(), "Reserva realizada con éxito", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Error al realizar la reserva", Toast.LENGTH_SHORT).show();
                        });
            }
        });

        // Botón para regresar a la actividad anterior
        button1 = view.findViewById(R.id.back_login3);
        button1.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new Principal1Fragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }

    // Método para cargar las horas disponibles desde Firebase para una fecha seleccionada
    private void cargarHorasDisponiblesDesdeFirebase(String fechaSeleccionada) {
        if (fechaSeleccionada != null) {
            db.collection("horarios")
                    .whereEqualTo("fecha", fechaSeleccionada)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        availableTimeSlots.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String inicio = document.getString("inicio");
                            String fin = document.getString("fin");
                            availableTimeSlots.add(new Horario(inicio, fin));
                        }
                        // Notificar al adaptador sobre los cambios en la lista
                        timeSlotsRecyclerView.getAdapter().notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Error al cargar horas disponibles", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
