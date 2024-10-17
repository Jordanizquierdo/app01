package com.example.app01;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


import static androidx.core.content.ContentProviderCompat.requireContext;
import static java.security.AccessController.getContext;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AjustesFragment extends Fragment implements ProfileAdapter.OnItemClickListener {

    private List<ProfileItem> profileItems;
    private ProfileAdapter adapter;
    private FirebaseFirestore db;
    private String userId = "USER_ID";  // Debes obtener el ID del usuario actual
    private ImageView button1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Configurar el RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar la lista de perfiles
        profileItems = new ArrayList<>();
        adapter = new ProfileAdapter(profileItems, this);
        recyclerView.setAdapter(adapter);

        // Cargar datos desde Firestore
        cargarDatosUsuario();

        // Botón para regresar a la actividad anterior
        button1 = view.findViewById(R.id.back_login5);
        button1.setOnClickListener(v -> {
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new Principal1Fragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        return view;
    }

    private void cargarDatosUsuario() {
        // Obtener referencia al documento del usuario
        DocumentReference userRef = db.collection("users").document(userId);

        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                // Obtener los campos del usuario
                String nombre = documentSnapshot.getString("nombre");
                String apellido = documentSnapshot.getString("apellido");
                String correo = documentSnapshot.getString("correo");

                // Agregar los datos al adaptador
                profileItems.clear();
                profileItems.add(new ProfileItem("Nombre: ", nombre));
                profileItems.add(new ProfileItem("Apellido: ", apellido));
                profileItems.add(new ProfileItem("Correo: ", correo));
                profileItems.add(new ProfileItem("Cambiar contraseña: ", ""));  // Campo opcional

                // Notificar al adaptador que los datos han cambiado
                adapter.notifyDataSetChanged();
            } else {
                // Mostrar error si no se encuentra el documento
                Log.e("AjustesFragment", "El documento de usuario no existe.");
            }
        }).addOnFailureListener(e -> {
            Log.e("AjustesFragment", "Error al obtener los datos del usuario", e);
        });
    }

    @Override
    public void onItemClick(ProfileItem item, int position) {
        showEditDialog(item, position);
    }

    private void showEditDialog(ProfileItem item, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Editar " + item.getLabel());

        final EditText input = new EditText(getContext());
        input.setText(item.getValue());
        builder.setView(input);

        builder.setPositiveButton("Guardar", (dialog, which) -> {
            String newValue = input.getText().toString();
            profileItems.set(position, new ProfileItem(item.getLabel(), newValue));
            adapter.notifyItemChanged(position);
            actualizarDatosUsuario(item.getLabel(), newValue);  // Actualizar en Firestore
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void actualizarDatosUsuario(String label, String newValue) {
        DocumentReference userRef = db.collection("users").document(userId);

        // Determinar qué campo se está actualizando
        String campo = null;
        if (label.equals("Nombre: ")) {
            campo = "nombre";
        } else if (label.equals("Apellido: ")) {
            campo = "apellido";
        } else if (label.equals("Correo: ")) {
            campo = "correo";
        }

        // Subir los datos modificados a Firestore
        if (campo != null) {
            userRef.update(campo, newValue)
                    .addOnSuccessListener(aVoid -> Log.d("AjustesFragment", "Datos actualizados exitosamente"))
                    .addOnFailureListener(e -> Log.e("AjustesFragment", "Error al actualizar los datos", e));
        }
    }
}
