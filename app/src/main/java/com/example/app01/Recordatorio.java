package com.example.app01;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Recordatorio extends AppCompatActivity {
    CalendarView cal;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_recordatorio);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Referencia a los EditText
        EditText campo1 = findViewById(R.id.name);
        EditText campo2 = findViewById(R.id.cantidad);
        EditText campo3 = findViewById(R.id.intervalo);
        EditText campo4 = findViewById(R.id.desc);
        cal = findViewById(R.id.cal1);

        //Obtenemos la fecha desde Principal1
        String fechaRecibida = getIntent().getStringExtra("fecha_seleccionada");

        //Formato para parsear la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            assert fechaRecibida != null;
            Date date = sdf.parse(fechaRecibida);
            if (date != null) {
                // Configurar la fecha en el CalendarView
                cal.setDate(date.getTime(), true, true);
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        //Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("RecordatorioPrefs", MODE_PRIVATE);

        //Botón para guardar los datos
        Button saveButton = findViewById(R.id.buttonGuardar); // Asegúrate de tener este botón en tu layout
        saveButton.setOnClickListener(v -> {
            //Obtener los valores de los EditText
            String name = campo1.getText().toString();
            String cantidad = campo2.getText().toString();
            String intervalo = campo3.getText().toString();
            String desc = campo4.getText().toString();

            //Obtener la fecha seleccionada en el CalendarView
            long selectedDate = cal.getDate(); // Fecha en milisegundos

            // Guardar los datos en SharedPreferences
            Intent e = new Intent();
            e.putExtra("name", name);
            e.putExtra("cantidad", cantidad);
            e.putExtra("intervalo", intervalo);
            e.putExtra("desc", desc);
            e.putExtra("fecha", selectedDate);

            //Enviar los datos y cerrar la actividad actual
            setResult(RESULT_OK, e);
            finish();
        });
    }
}
