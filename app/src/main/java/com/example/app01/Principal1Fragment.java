package com.example.app01;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Principal1Fragment extends Fragment {
    private String fecha1;
    CalendarView cal;
    Button button;
    List<Rtext> recordatorioList = new ArrayList<>();
    RecordatorioAdapter adapter;
    List<String> listaMascotas = new ArrayList<>();
    TextView tvPetName;
    Map<String, List<Rtext>> recordatoriosPorMascota = new HashMap<>();
    private FirebaseFirestore db;
    private String userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal1, container, false);

        userId = requireActivity().getIntent().getStringExtra("userId");

        FirebaseApp.initializeApp(requireContext());
        db = FirebaseFirestore.getInstance();
        Toolbar tb = view.findViewById(R.id.t1);
        cal = view.findViewById(R.id.calendar);

        RecyclerView recyclerView = view.findViewById(R.id.lv_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // Usamos getContext() en lugar de 'this'

// Inicializar el adaptador con la lista vacía
        adapter = new RecordatorioAdapter(recordatorioList);
        recyclerView.setAdapter(adapter);

        // Incorporar el menú lateral de navegación
        NavigationView nav = view.findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.op1) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, new AjustesFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else if (id == R.id.op2) {
                HistorialFragment historialFragment = new HistorialFragment();
                Bundle bundle = new Bundle();
                bundle.putString("nombreMascota", tvPetName.getText().toString()); // Nombre de la mascota
                bundle.putString("documentoId", userId);  // ID del documento
                historialFragment.setArguments(bundle);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, historialFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else if (id == R.id.op3) {
                ReservarFragment reservarFragment = new ReservarFragment();
                Bundle bundle = new Bundle();
                bundle.putString("userId", userId);  // Agregar el userId al Bundle
                reservarFragment.setArguments(bundle); // Establecer el Bundle en el fragmento
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, reservarFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            return true;
        });

        DrawerLayout dl = view.findViewById(R.id.registrarse);
        AppCompatActivity activity = (AppCompatActivity) getActivity();
        if (activity != null) {
            activity.setSupportActionBar(tb);

            // Crea el ActionBarDrawerToggle para controlar el menú lateral
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    activity, dl, tb, R.string.abrir_drawer, R.string.cerrar_drawer);

            // Cambia el color del ícono del Drawer
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.azul1));

            // Agrega el toggle al DrawerLayout
            dl.addDrawerListener(toggle);
            toggle.syncState();

            // Configura la acción al hacer clic en el ícono de navegación
            tb.setNavigationOnClickListener(v -> {
                if (dl.isDrawerOpen(GravityCompat.START)) {
                    dl.closeDrawer(GravityCompat.START);
                } else {
                    dl.openDrawer(GravityCompat.START);
                }
            });
        }
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        fecha1 = dayOfMonth + "/" + month + "/" + year;
        tvPetName = view.findViewById(R.id.tv_pet_name);

        button = view.findViewById(R.id.b3);
        button.setOnClickListener(v -> {
            if (fecha1 != null) {




                RecordatorioFragment recordatorioFragment = new RecordatorioFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("nombreMascota", tvPetName.getText().toString());
                bundle1.putString("userId", userId);
                bundle1.putString("fecha_seleccionada", fecha1);  // Añadir la fecha seleccionada // ID del documento
                recordatorioFragment.setArguments(bundle1);

                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, recordatorioFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();



            }
        });

        cal.setOnDateChangeListener((calendarView, newYear, newMonth, newDayOfMonth) -> {
            fecha1 = newDayOfMonth + "/" + (newMonth + 1) + "/" + newYear;
            Toast.makeText(getActivity(), "Fecha seleccionada: " + fecha1, Toast.LENGTH_LONG).show();
        });

        cargarMascotasDesdeFirestore();

        tvPetName.setOnClickListener(v -> mostrarDialogoCambioMascota());

        return view;
    }

    private void cargarMascotasDesdeFirestore() {
        CollectionReference mascotasRef = db.collection("users").document(userId).collection("mascotas");
        mascotasRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listaMascotas.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    String nombreMascota = document.getString("nombreMascota");
                    listaMascotas.add(nombreMascota);
                }
                if (!listaMascotas.isEmpty()) {
                    tvPetName.setText(listaMascotas.get(0));
                    cargarRecordatoriosDesdeFirestore(listaMascotas.get(0));
                }
            } else {
                Log.d("Firestore", "Error al obtener mascotas: ", task.getException());
            }
        });
    }

    private void cargarRecordatoriosDesdeFirestore(String nombreMascota) {
        CollectionReference recordatoriosRef = db.collection("users").document(userId)
                .collection("mascotas").document(nombreMascota).collection("recordatorios");

        // Formato para parsear fechas sin el día de la semana
        SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        // Formato para mostrar fechas con el día de la semana
        SimpleDateFormat displayFormat = new SimpleDateFormat("EEEE, yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MMM dd, hh:mm a", Locale.getDefault());
        SimpleDateFormat hourMinuteFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());  // Formato de 12 horas con AM/PM

        // Obtener la fecha y hora actual
        Date fechaActual = Calendar.getInstance().getTime();

        recordatoriosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recordatorioList.clear();  // Limpia la lista antes de agregar los nuevos recordatorios

                for (QueryDocumentSnapshot document : task.getResult()) {
                    try {
                        // Obtener el campo "fecha" como una cadena
                        String fechaConHora = document.getString("fecha");

                        if (fechaConHora != null) {
                            Date fechaRecordatorio;

                            // Verificar si la cadena es un número (timestamp)
                            if (fechaConHora.matches("\\d+")) {
                                // Convertir el timestamp en milisegundos a una fecha
                                long timestamp = Long.parseLong(fechaConHora);
                                fechaRecordatorio = new Date(timestamp);
                            } else {
                                // Parsear la fecha sin el día de la semana
                                fechaRecordatorio = parseFormat.parse(fechaConHora);
                            }

                            String titulo = document.getString("name");
                            String descripcion = document.getString("desc");
                            String cantidad = document.getString("cantidad");  // Días
                            String intervalo = document.getString("intervalo");  // Horas

                            // Validación de cantidad e intervalo
                            int cantidadDias = (cantidad != null) ? Integer.parseInt(cantidad) : 0;
                            int intervaloHoras = (intervalo != null) ? Integer.parseInt(intervalo) : 0;

                            // Crear un objeto Calendar basado en la fecha obtenida
                            calendar.setTime(fechaRecordatorio);
                            calendar.set(Calendar.HOUR_OF_DAY, 0);  // Reiniciar horas a las 0:00
                            calendar.set(Calendar.MINUTE, 0);       // Reiniciar minutos a las 0

                            // Generar recordatorios por el número de días y el intervalo de horas
                            for (int i = 0; i < cantidadDias; i++) {
                                for (int tiempo = 0; tiempo < 24; tiempo += intervaloHoras) {
                                    // Sumar el intervalo de horas
                                    calendar.add(Calendar.HOUR_OF_DAY, intervaloHoras);

                                    // Crear un nuevo recordatorio solo si es futuro
                                    if (calendar.getTime().after(fechaActual)) {
                                        String horaFormateada = hourMinuteFormat.format(calendar.getTime());
                                        String fechaFormateada = displayFormat.format(fechaRecordatorio);
                                        String fechaConHora12 = dateFormat.format(calendar.getTime());

                                        // Crear un nuevo recordatorio según la fecha y la nueva hora
                                        Rtext recordatorio = new Rtext(fechaConHora12, horaFormateada, titulo, descripcion);
                                        recordatorioList.add(recordatorio);
                                    }
                                }

                                // Mover al siguiente día en el calendario
                                calendar.add(Calendar.DATE, 1);
                                calendar.set(Calendar.HOUR_OF_DAY, 0);  // Reiniciar horas para el nuevo día
                            }
                        } else {
                            Log.e("Principal1", "El campo 'fecha' es nulo.");
                        }
                    } catch (ParseException e) {
                        Log.e("Principal1", "Error al procesar la fecha: " + e.getMessage(), e);
                    }
                }

                // Notificar al adaptador que la lista ha cambiado
                adapter.notifyDataSetChanged();
            } else {
                Log.d("Firestore", "Error al obtener recordatorios: ", task.getException());
            }
        });
    }












    private void cargarFragmento(Fragment fragment) {
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, new RegistrarseFragment());
        fragmentTransaction.addToBackStack(null);  // Para permitir volver al login si es necesario
        fragmentTransaction.commit();
    }

    private void mostrarDialogoCambioMascota() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_mascotas, null);
        builder.setView(dialogView);

        RecyclerView rvMascotas = dialogView.findViewById(R.id.rvMascotas);
        rvMascotas.setLayoutManager(new LinearLayoutManager(getContext()));

        if (!listaMascotas.isEmpty()) {
            MascotaAdapter mascotaAdapter = new MascotaAdapter(listaMascotas, nombre -> {
                tvPetName.setText(nombre);
                cargarRecordatoriosDesdeFirestore(nombre);
            });
            rvMascotas.setAdapter(mascotaAdapter);
        }

        Button btnAgregarMascota = dialogView.findViewById(R.id.btnAgregarMascota);
        btnAgregarMascota.setOnClickListener(v -> mostrarDialogoAgregarMascota());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void mostrarDialogoAgregarMascota() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Agregar nueva mascota");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Agregar", (dialog, which) -> {
            String nombreMascota = input.getText().toString().trim();
            if (!nombreMascota.isEmpty()) {
                agregarMascotaAFirestore(nombreMascota);
            } else {
                Toast.makeText(getContext(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void agregarMascotaAFirestore(String nombreMascota) {
        Map<String, Object> mascotaData = new HashMap<>();
        mascotaData.put("nombreMascota", nombreMascota);

        CollectionReference mascotasRef = db.collection("users").document(userId).collection("mascotas");

        mascotasRef.document(nombreMascota).set(mascotaData)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Mascota añadida: " + nombreMascota, Toast.LENGTH_SHORT).show();
                    listaMascotas.add(nombreMascota);
                    tvPetName.setText(nombreMascota);
                    cargarRecordatoriosDesdeFirestore(nombreMascota);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al añadir mascota: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
