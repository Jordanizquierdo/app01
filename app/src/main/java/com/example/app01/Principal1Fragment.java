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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

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
        if (userId != null) {
            Toast.makeText(getActivity(), "Usuario ID: " + userId, Toast.LENGTH_SHORT).show();
        }

        FirebaseApp.initializeApp(requireContext());
        db = FirebaseFirestore.getInstance();

        cal = view.findViewById(R.id.calendar);

        RecyclerView recyclerView = view.findViewById(R.id.lv_reminders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));  // Usamos getContext() en lugar de 'this'

// Inicializar el adaptador con la lista vacía
        adapter = new RecordatorioAdapter(recordatorioList);
        recyclerView.setAdapter(adapter);

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
                Bundle bundle = new Bundle();
                bundle.putString("nombreMascota", tvPetName.getText().toString());
                bundle.putString("userId", userId);
                recordatorioFragment.setArguments(bundle);
                cargarFragmento(recordatorioFragment);
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

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        LocalTime horaActual = LocalTime.now();
        int horas = horaActual.getHour();
        Date ahora = new Date();

        recordatoriosRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                recordatorioList.clear();

                for (QueryDocumentSnapshot document : task.getResult()) {
                    String fechaConHora = document.getString("fecha");
                    try {
                        Date fechaRecordatorio = dateFormat.parse(fechaConHora);
                        if (fechaRecordatorio != null && fechaRecordatorio.after(ahora)) {
                            String hora = document.getString("hora");
                            String titulo = document.getString("titulo");
                            String descripcion = document.getString("descripcion");

                            Rtext a = new Rtext(fechaConHora, hora, titulo, descripcion);
                            recordatorioList.add(a);

                            // Verificación: loguear cada recordatorio agregado
                            Log.d("Recordatorio", "Añadido: " + titulo + ", " + descripcion + ", " + fechaConHora);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            } else {
                Log.d("Firestore", "Error al obtener recordatorios: ", task.getException());
            }
        });
    }

    private void cargarFragmento(Fragment fragment) {
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
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
