package com.example.app01;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class ReservarFragment extends Fragment {

    public CalendarView calendarView;
    public RecyclerView timeSlotsRecyclerView;
    public Button confirmButton;
    public TextView confirmationText;
    public List<Horario> timeSlots;
    public ImageView button1;
    public Horario selectedTimeSlot = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View view = inflater.inflate(R.layout.fragment_reservar, container, false);

        // Inicializar vistas
        calendarView = view.findViewById(R.id.calendarView);
        timeSlotsRecyclerView = view.findViewById(R.id.timeSlotsRecyclerView);
        confirmButton = view.findViewById(R.id.confirmButton);

        // Horarios de ejemplo
        timeSlots = new ArrayList<>();
        timeSlots.add(new Horario("9:00 AM", "9:20 AM"));
        timeSlots.add(new Horario("9:20 AM", "9:40 AM"));
        timeSlots.add(new Horario("9:40 AM", "10:00 AM"));

        // Configurar el RecyclerView
        timeSlotsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        TimeSlotAdapter adapter = new TimeSlotAdapter(timeSlots, new TimeSlotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Horario horario) {
                selectedTimeSlot = horario;
                Toast.makeText(getContext(), "Seleccionado: " + horario.getFormattedTimeSlot(), Toast.LENGTH_SHORT).show();
            }
        });
        timeSlotsRecyclerView.setAdapter(adapter);

        // Botón de confirmar
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTimeSlot != null) {
                    Toast.makeText(getContext(), "Espere confirmación", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Por favor selecciona una hora", Toast.LENGTH_SHORT).show();
                }
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
}
