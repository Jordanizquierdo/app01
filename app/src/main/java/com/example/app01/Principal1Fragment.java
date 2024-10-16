package com.example.app01;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
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

public class Principal1Fragment extends Fragment {
    private static final int RECORDATORIO_REQUEST_CODE = 1;
    public String fecha1;
    CalendarView cal;
    Button button;
    List<Rtext> recordatorioList = new ArrayList<>();
    RecordatorioAdapter adapter;
    Button button1;
    List<String> listaMascotas = new ArrayList<>();
    TextView tvPetName;

    // Mapa para almacenar los recordatorios por mascota
    Map<String, List<Rtext>> recordatoriosPorMascota = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal1, container, false);

        Toolbar tb = view.findViewById(R.id.t1);
        cal = view.findViewById(R.id.calendar);

        // Configuración de RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.lv_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar el adaptador con la lista vacía
        adapter = new RecordatorioAdapter(recordatorioList);
        recyclerView.setAdapter(adapter);

        // Incorporar el menú lateral
        NavigationView nav = view.findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.op1) {
                cargarFragmento(new AjustesFragment());
            } else if (id == R.id.op2) {
                cargarFragmento(new HistorialFragment());
            } else if (id == R.id.op3) {
                cargarFragmento(new ReservarFragment());
            }
            // Asegúrate de tener el id correcto en tu layout
            return true;
        });

        DrawerLayout dl = view.findViewById(R.id.registrarse);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(tb);

            // Crea el ActionBarDrawerToggle
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity, dl, tb, R.string.abrir_drawer, R.string.cerrar_drawer);

            // Cambia el color del ícono del Drawer
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.azul1)); // Aquí cambias el color

            // Agrega el toggle al DrawerLayout
            dl.addDrawerListener(toggle);
            toggle.syncState(); // Sincroniza el estado del toggle con el DrawerLayout

            // Configura la acción al hacer clic en el ícono de navegación
            tb.setNavigationOnClickListener(v -> {
                if (dl.isDrawerOpen(GravityCompat.START)) {
                    dl.closeDrawer(GravityCompat.START);
                } else {
                    dl.openDrawer(GravityCompat.START);
                }
            });
        }

            // Obtener la fecha actual al iniciar la actividad
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            fecha1 = dayOfMonth + "/" + month + "/" + year;

            // Botón para mostrar la fecha actual
            button = view.findViewById(R.id.b3);
            button.setOnClickListener(v -> {
                if (fecha1 != null) {
                    // En lugar de usar Intent
                    cargarFragmento(new RecordatorioFragment());

                }
            });

            // Listener del calendario para cambios de fecha
            cal.setOnDateChangeListener((calendarView, newYear, newMonth, newDayOfMonth) -> {
                fecha1 = newDayOfMonth + "/" + (newMonth + 1) + "/" + newYear;
                Toast.makeText(getActivity(), "Fecha seleccionada: " + fecha1, Toast.LENGTH_LONG).show();
            });

            // Inicializar lista de mascotas
            listaMascotas.add("Firulais");
            listaMascotas.add("Max");
            listaMascotas.add("Luna");

            // Referencia del TextView del nombre de la mascota
            tvPetName = view.findViewById(R.id.tv_pet_name);
            tvPetName.setText(listaMascotas.get(0));  // Mostrar la primera mascota por defecto

            // Listener para cambiar de mascota al hacer clic en el nombre
            tvPetName.setOnClickListener(v -> mostrarDialogoCambioMascota());

            // Cargar recordatorios para la primera mascota por defecto
            cargarRecordatoriosParaMascota(tvPetName.getText().toString());


        nav.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.button3) {
                // Redirigir a la MainActivity
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Esto asegura que no pueda regresar con el botón atrás
                startActivity(intent);
                getActivity().finish();  // Cierra la actividad actual para prevenir que vuelva
            }
            return true;
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RECORDATORIO_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            String mascotaActual = tvPetName.getText().toString();
            String name = data.getStringExtra("name");
            String cantidadStr = data.getStringExtra("cantidad");
            String intervaloStr = data.getStringExtra("intervalo");
            String desc = data.getStringExtra("desc");
            String fecha = data.getStringExtra("fecha");

            int cantidadDias = Integer.parseInt(cantidadStr);
            int intervaloHoras = Integer.parseInt(intervaloStr);
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMM dd, hh:mm a", Locale.getDefault());
            Calendar calendar = Calendar.getInstance();

            try {
                Date fechaSeleccionada = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fecha);
                calendar.setTime(fechaSeleccionada);
            } catch (Exception e) {
                e.printStackTrace();
            }

            LocalTime horaActual = LocalTime.now();
            int horas = horaActual.getHour();
            for (int i = 0; i < cantidadDias; i++) {
                for (int tiempo = horas; tiempo < 24; tiempo += intervaloHoras) {
                    String fechaConHora = dateFormat.format(calendar.getTime());
                    Rtext nuevoRecordatorio = new Rtext(fechaConHora, tiempo + ":00", name, desc);

                    List<Rtext> recordatoriosMascota = recordatoriosPorMascota.get(mascotaActual);
                    if (recordatoriosMascota == null) {
                        recordatoriosMascota = new ArrayList<>();
                        recordatoriosPorMascota.put(mascotaActual, recordatoriosMascota);
                    }
                    recordatoriosMascota.add(nuevoRecordatorio);
                }

                horas = 0;
                calendar.add(Calendar.DATE, 1);
            }

            cargarRecordatoriosParaMascota(mascotaActual);
        }
    }

    private void cargarRecordatoriosParaMascota(String nombreMascota) {
        List<Rtext> recordatoriosMascota = recordatoriosPorMascota.get(nombreMascota);

        if (recordatoriosMascota == null) {
            recordatoriosMascota = new ArrayList<>();
            recordatoriosPorMascota.put(nombreMascota, recordatoriosMascota);
        }

        recordatorioList.clear();
        recordatorioList.addAll(recordatoriosMascota);
        adapter.notifyDataSetChanged();
    }

    private void mostrarDialogoCambioMascota() {
        String[] opciones = new String[listaMascotas.size() + 1];
        for (int i = 0; i < listaMascotas.size(); i++) {
            opciones[i] = listaMascotas.get(i);
        }
        opciones[listaMascotas.size()] = "Crear nueva mascota";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Seleccionar mascota");
        builder.setItems(opciones, (dialog, which) -> {
            if (which == listaMascotas.size()) {
                mostrarDialogoCrearMascota();
            } else {
                tvPetName.setText(listaMascotas.get(which));
                cargarRecordatoriosParaMascota(listaMascotas.get(which));
            }
        });
        builder.show();
    }

    private void mostrarDialogoCrearMascota() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Crear nueva mascota");

        final EditText input = new EditText(getActivity());
        input.setHint("Nombre de la mascota");
        builder.setView(input);

        builder.setPositiveButton("Crear", (dialog, which) -> {
            String nombreNuevaMascota = input.getText().toString();
            if (!nombreNuevaMascota.isEmpty()) {
                listaMascotas.add(nombreNuevaMascota);
                recordatoriosPorMascota.put(nombreNuevaMascota, new ArrayList<>());
                tvPetName.setText(nombreNuevaMascota);
                cargarRecordatoriosParaMascota(nombreNuevaMascota);
                Toast.makeText(getActivity(), "Nueva mascota creada: " + nombreNuevaMascota, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void cargarFragmento(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // Opcional: para que el usuario pueda volver atrás
                .commit();
    }
}

//arreglar ultimas 3 en fragment y recordatorio (usar firebase tambien) y cargar recordatorios desde firebase
