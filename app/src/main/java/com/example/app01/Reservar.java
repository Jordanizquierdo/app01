package com.example.app01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Reservar extends AppCompatActivity {

    public CalendarView calendarView;
    public RecyclerView timeSlotsRecyclerView;
    public Button confirmButton;
    public TextView confirmationText;
    public List<Horario> timeSlots;
    ImageView button1;
    public Horario selectedTimeSlot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservar);

        //Inicializar vistas
        calendarView = findViewById(R.id.calendarView);
        timeSlotsRecyclerView = findViewById(R.id.timeSlotsRecyclerView);
        confirmButton = findViewById(R.id.confirmButton);


        //Horarios de ejemplo
        timeSlots = new ArrayList<>();
        timeSlots.add(new Horario("9:00 AM", "9:20 AM"));
        timeSlots.add(new Horario("9:20 AM", "9:40 AM"));
        timeSlots.add(new Horario("9:40 AM", "10:00 AM"));

        //Configurar el RecyclerView
        timeSlotsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        TimeSlotAdapter adapter = new TimeSlotAdapter(timeSlots, new TimeSlotAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Horario horario) {
                selectedTimeSlot = horario;
                Toast.makeText(Reservar.this, "Seleccionado: " + horario.getFormattedTimeSlot(), Toast.LENGTH_SHORT).show();
            }
        });
        timeSlotsRecyclerView.setAdapter(adapter);

        //botón de confirmar
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedTimeSlot != null) {
                    Toast.makeText(Reservar.this, "Espere confirmación", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Reservar.this, "Por favor selecciona una hora", Toast.LENGTH_SHORT).show();
                }
            }
        });


        button1 = findViewById(R.id.back_login3);
        button1.setOnClickListener(v -> {
            Intent p = new Intent(getApplicationContext(), Principal1.class);
            startActivity(p);
        });
    }
}
