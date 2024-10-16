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
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;public class Principal1Fragment extends Fragment {
    private static final int RECORDATORIO_REQUEST_CODE = 1;
    public String fecha1;
    CalendarView cal;
    Button button;
    Button button1;
    Button btnCrearMascota;  // Botón para crear una nueva mascota
    List<Rtext> recordatorioList = new ArrayList<>();
    RecordatorioAdapter adapter;
    List<String> listaMascotas = new ArrayList<>();
    TextView tvPetName;

    // Firebase Firestore
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_principal1, container, false);

        // Inicializar Firebase Firestore
        FirebaseApp.initializeApp(requireContext());
        db = FirebaseFirestore.getInstance();

        Toolbar tb = view.findViewById(R.id.t1);
        cal = view.findViewById(R.id.calendar);

        // Configuración de RecyclerView para mostrar recordatorios
        RecyclerView recyclerView = view.findViewById(R.id.lv_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar el adaptador con la lista vacía de recordatorios
        adapter = new RecordatorioAdapter(recordatorioList);
        recyclerView.setAdapter(adapter);

        // Incorporar el menú lateral de navegación
        NavigationView nav = view.findViewById(R.id.nav);
        nav.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.op1) {
                cargarFragmento(new AjustesFragment());  // Cargar ajustes
            } else if (id == R.id.op2) {
                cargarFragmento(new HistorialFragment());  // Cargar historial
            } else if (id == R.id.op3) {
                cargarFragmento(new ReservarFragment());  // Cargar reservación
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

        // Obtener la fecha actual al iniciar la actividad
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        fecha1 = dayOfMonth + "/" + month + "/" + year;

        // Botón para mostrar la fecha actual y enviar la mascota seleccionada
        button = view.findViewById(R.id.b3);
        button.setOnClickListener(v -> {
            if (fecha1 != null) {
                RecordatorioFragment recordatorioFragment = new RecordatorioFragment();
                Bundle bundle = new Bundle();
                bundle.putString("nombreMascota", tvPetName.getText().toString());  // Pasar la mascota seleccionada
                recordatorioFragment.setArguments(bundle);
                cargarFragmento(recordatorioFragment);  // Cargar el fragmento de recordatorio
            }
        });

        // Listener del calendario para cambios de fecha
        cal.setOnDateChangeListener((calendarView, newYear, newMonth, newDayOfMonth) -> {
            fecha1 = newDayOfMonth + "/" + (newMonth + 1) + "/" + newYear;
            Toast.makeText(getActivity(), "Fecha seleccionada: " + fecha1, Toast.LENGTH_LONG).show();
        });

        // Referencia del TextView para mostrar el nombre de la mascota seleccionada
        tvPetName = view.findViewById(R.id.tv_pet_name);

        // Botón para crear una nueva mascota



        // Cargar las mascotas desde Firestore
        cargarMascotasDesdeFirestore();

        // Listener para cambiar de mascota al hacer clic en el nombre de la mascota
        tvPetName.setOnClickListener(v -> mostrarDialogoCambioMascota());

        return view;
    }

    // Método para cargar las mascotas desde Firebase Firestore
    private void cargarMascotasDesdeFirestore() {
        CollectionReference mascotasRef = db.collection("mascotas");
        mascotasRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listaMascotas.clear();
                for (DocumentSnapshot document : task.getResult()) {
                    String nombreMascota = document.getString("nombreMascota");
                    listaMascotas.add(nombreMascota);  // Añadir cada mascota a la lista
                }
                if (!listaMascotas.isEmpty()) {
                    tvPetName.setText(listaMascotas.get(0));  // Mostrar la primera mascota por defecto
                    cargarRecordatoriosDesdeFirestore(listaMascotas.get(0));  // Cargar recordatorios para la primera mascota
                }
            } else {
                Log.d("Firestore", "Error al obtener mascotas: ", task.getException());
            }
        });
    }

    // Método para cargar los recordatorios desde Firebase Firestore para una mascota específica
    private void cargarRecordatoriosDesdeFirestore(String nombreMascota) {
        CollectionReference recordatoriosRef = db.collection("recordatorios");
        recordatoriosRef.whereEqualTo("nombreMascota", nombreMascota)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        recordatorioList.clear();  // Limpiar la lista antes de cargar nuevos datos
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Rtext recordatorio = document.toObject(Rtext.class);
                            recordatorioList.add(recordatorio);  // Agregar cada recordatorio a la lista
                        }
                        adapter.notifyDataSetChanged();  // Notificar al adaptador para actualizar la vista
                    } else {
                        Log.d("Firestore", "Error al obtener documentos: ", task.getException());
                    }
                });
    }

    // Método para cambiar entre fragmentos
    private void cargarFragmento(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // Para que el usuario pueda volver atrás
                .commit();
    }

    // Método para mostrar un diálogo que permite seleccionar una mascota diferente
    private void mostrarDialogoCambioMascota() {
        // Creamos el layout del diálogo
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_mascotas, null);
        builder.setView(dialogView);

        // Referenciar el RecyclerView para la lista de mascotas
        RecyclerView rvMascotas = dialogView.findViewById(R.id.rvMascotas);
        rvMascotas.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear adaptador para el RecyclerView
        MascotaAdapter mascotaAdapter = new MascotaAdapter(listaMascotas, nombre -> {
            tvPetName.setText(nombre);  // Cambiar el nombre de la mascota seleccionada
            cargarRecordatoriosDesdeFirestore(nombre);  // Cargar recordatorios para la mascota seleccionada
        });
        rvMascotas.setAdapter(mascotaAdapter);

        // Botón para agregar una nueva mascota
        Button btnAgregarMascota = dialogView.findViewById(R.id.btnAgregarMascota);
        btnAgregarMascota.setOnClickListener(v -> mostrarDialogoCrearMascota());  // Llamamos al método para crear mascota

        // Crear y mostrar el diálogo
        builder.setTitle("Seleccionar Mascota");
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // Método para mostrar un diálogo que permite crear una nueva mascota
    private void mostrarDialogoCrearMascota() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Crear nueva mascota");

        final EditText input = new EditText(getActivity());
        input.setHint("Nombre de la mascota");
        builder.setView(input);

        builder.setPositiveButton("Crear", (dialog, which) -> {
            String nombreMascota = input.getText().toString();
            if (!nombreMascota.isEmpty()) {
                agregarMascotaAFirestore(nombreMascota);
            } else {
                Toast.makeText(getActivity(), "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void agregarMascotaAFirestore(String nombreMascota) {
        Rtext nuevaMascota = new Rtext("", "", "", "", nombreMascota);  // Solo el campo "mascota" se establece

        db.collection("mascotas").add(nuevaMascota)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Mascota añadida con éxito", Toast.LENGTH_SHORT).show();
                    listaMascotas.add(nombreMascota);  // Añadir la mascota a la lista local
                    tvPetName.setText(nombreMascota);  // Mostrar la nueva mascota en la vista
                    cargarRecordatoriosDesdeFirestore(nombreMascota);  // Cargar los recordatorios para la nueva mascota
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error al agregar mascota", Toast.LENGTH_SHORT).show();
                    Log.w("Firestore", "Error al agregar el documento", e);
                });
    }
}
