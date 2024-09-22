package com.example.app01;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Principal1 extends AppCompatActivity {
    private static final int RECORDATORIO_REQUEST_CODE = 1;
    public String fecha1;
    CalendarView cal;
    Button button;
    List<Rtext> recordatorioList = new ArrayList<>();
    RecordatorioAdapter adapter;
    Button button1;
    List<String> listaMascotas = new ArrayList<>();
    TextView tvPetName;

    //Mapa para almacenar los recordatorios por mascota
    Map<String, List<Rtext>> recordatoriosPorMascota = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal1);
        Toolbar tb = findViewById(R.id.t1);
        setSupportActionBar(tb);
        cal = findViewById(R.id.calendar);

        //Configuración de RecyclerView
        RecyclerView recyclerView = findViewById(R.id.lv_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Inicializar el adaptador con la lista vacía
        adapter = new RecordatorioAdapter(recordatorioList);
        recyclerView.setAdapter(adapter);

        // Incorporar el menú lateral
        NavigationView nav = findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.op1) {
                Intent a = new Intent(getApplicationContext(), AjustesActivity.class);
                startActivity(a);
            } else if (id == R.id.op2) {
                Intent a = new Intent(getApplicationContext(), HistorialActivity.class);
                startActivity(a);
            } else if (id == R.id.op3) {
                Intent a = new Intent(getApplicationContext(), Reservar.class);
                startActivity(a);
            }
            return false;

        });

        DrawerLayout dl = findViewById(R.id.registrarse);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                dl,
                R.string.abrir_drawer,
                R.string.cerrar_drawer
        );
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.azul1));
        dl.addDrawerListener(toggle);
        toggle.syncState();

        tb.setNavigationOnClickListener(view -> {
            if (dl.isDrawerOpen(GravityCompat.START)) {
                dl.closeDrawer(GravityCompat.START);
            } else {
                dl.openDrawer(GravityCompat.START);
            }
        });

        //Obtener la fecha actual al iniciar la actividad
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        fecha1 = dayOfMonth + "/" + month + "/" + year;

        //Botón para mostrar la fecha actual
        button = findViewById(R.id.b3);
        button.setOnClickListener(v -> {
            if (fecha1 != null) {
                Intent a = new Intent(getApplicationContext(), Recordatorio.class);
                a.putExtra("fecha_seleccionada", fecha1);
                startActivityForResult(a, RECORDATORIO_REQUEST_CODE);  // Iniciar la actividad esperando un resultado
            }
        });

        //Listener del calendario para cambios de fecha
        cal.setOnDateChangeListener((calendarView, newYear, newMonth, newDayOfMonth) -> {
            fecha1 = newDayOfMonth + "/" + (newMonth + 1) + "/" + newYear;
            Toast.makeText(Principal1.this, "Fecha seleccionada: " + fecha1, Toast.LENGTH_LONG).show();
        });

        //Inicializar lista de mascotas
        listaMascotas.add("Firulais");
        listaMascotas.add("Max");
        listaMascotas.add("Luna");

        //Referencia del TextView del nombre de la mascota
        tvPetName = findViewById(R.id.tv_pet_name);
        tvPetName.setText(listaMascotas.get(0));  // Mostrar la primera mascota por defecto

        //Listener para cambiar de mascota al hacer clic en el nombre
        tvPetName.setOnClickListener(v -> mostrarDialogoCambioMascota());

        //Cargar recordatorios para la primera mascota por defecto
        cargarRecordatoriosParaMascota(tvPetName.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RECORDATORIO_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            //Obtener el nombre de la mascota seleccionada
            String mascotaActual = tvPetName.getText().toString();

            //Recuperar los datos enviados desde Recordatorio
            String name = data.getStringExtra("name");
            String cantidadStr = data.getStringExtra("cantidad");
            String intervaloStr = data.getStringExtra("intervalo");
            String desc = data.getStringExtra("desc");
            String fecha = data.getStringExtra("fecha");

            int cantidadDias = Integer.parseInt(cantidadStr);  // Convertir cantidad de días
            int intervaloHoras = Integer.parseInt(intervaloStr);  // Convertir intervalo en horas

            //Inicializar el formato de fecha y la hora actual
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMM dd, hh:mm a", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();

            //Usar la fecha seleccionada como base
            try {
                Date fechaSeleccionada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fecha);
                calendar.setTime(fechaSeleccionada);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LocalTime horaActual = LocalTime.now();
            int horas = horaActual.getHour();
            //Bucle para los días seleccionados
            for (int i = 0; i < cantidadDias; i++) {
                // Bucle para los intervalos de horas en cada día (por ejemplo, de la hora actual a las 23)
                for (int tiempo = horas; tiempo < 24; tiempo += intervaloHoras) {
                    //Crear la fecha y hora con el intervalo

                    //Formatear la fecha y hora como cadena
                    String fechaConHora = dateFormat.format(calendar.getTime());

                    //Crear un nuevo recordatorio con los datos calculados
                    Rtext nuevoRecordatorio = new Rtext(fechaConHora, tiempo + ": 00", name, desc);

                    // Agregar el recordatorio a la lista de la mascota
                    List<Rtext> recordatoriosMascota = recordatoriosPorMascota.get(mascotaActual);
                    if (recordatoriosMascota == null) {
                        recordatoriosMascota = new ArrayList<>();
                        recordatoriosPorMascota.put(mascotaActual, recordatoriosMascota);
                    }
                    recordatoriosMascota.add(nuevoRecordatorio);
                }

                // Restablecer las horas al inicio del día (0) para el día siguiente
                horas = 0;

                // Mover al siguiente día
                calendar.add(Calendar.DATE, 1);
            }

            //Cargar los recordatorios actualizados de la mascota actual
            cargarRecordatoriosParaMascota(mascotaActual);
        }
    }

    private void cargarRecordatoriosParaMascota(String nombreMascota) {
        List<Rtext> recordatoriosMascota = recordatoriosPorMascota.get(nombreMascota);

        // Si no hay recordatorios para esa mascota, inicializa una lista vacía
        if (recordatoriosMascota == null) {
            recordatoriosMascota = new ArrayList<>();
            recordatoriosPorMascota.put(nombreMascota, recordatoriosMascota);
        }

        // Actualizar la lista del adaptador con los recordatorios de la mascota
        recordatorioList.clear();
        recordatorioList.addAll(recordatoriosMascota);
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogoCambioMascota() {
        //Crea un array con las mascotas y añadir opción de crear una nueva
        String[] opciones = new String[listaMascotas.size() + 1];
        for (int i = 0; i < listaMascotas.size(); i++) {
            opciones[i] = listaMascotas.get(i);
        }
        opciones[listaMascotas.size()] = "Crear nueva mascota";

        //Crea un AlertDialog para seleccionar una opción
        AlertDialog.Builder builder = new AlertDialog.Builder(Principal1.this);
        builder.setTitle("Seleccionar mascota");
        builder.setItems(opciones, (dialog, which) -> {
            if (which == listaMascotas.size()) {
                mostrarDialogoCrearMascota();
            } else {
                // Cambiar a una mascota existente
                tvPetName.setText(listaMascotas.get(which));
                cargarRecordatoriosParaMascota(listaMascotas.get(which));  // Cargar los recordatorios de la mascota seleccionada
            }
        });
        builder.show();
    }

    private void mostrarDialogoCrearMascota() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Principal1.this);
        builder.setTitle("Crear nueva mascota");

        final EditText input = new EditText(Principal1.this);
        input.setHint("Nombre de la mascota");
        builder.setView(input);

        builder.setPositiveButton("Crear", (dialog, which) -> {
            String nombreNuevaMascota = input.getText().toString();
            if (!nombreNuevaMascota.isEmpty()) {
                listaMascotas.add(nombreNuevaMascota);
                recordatoriosPorMascota.put(nombreNuevaMascota, new ArrayList<>());  // Inicializar la lista de recordatorios para la nueva mascota
                tvPetName.setText(nombreNuevaMascota);  // Cambiar a la nueva mascota
                cargarRecordatoriosParaMascota(nombreNuevaMascota);  // Cargar los recordatorios de la nueva mascota
                Toast.makeText(Principal1.this, "Nueva mascota creada: " + nombreNuevaMascota, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(Principal1.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public void cerrar(View v){
        Intent a = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(a);
    }


}
