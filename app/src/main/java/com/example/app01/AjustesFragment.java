package com.example.app01;

import android.content.Intent;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AjustesFragment extends Fragment implements ProfileAdapter.OnItemClickListener {

    private List<ProfileItem> profileItems;
    private ProfileAdapter adapter;
    private ImageView button1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_ajustes, container, false);

        // Configurar el RecyclerView
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear lista de perfiles
        profileItems = new ArrayList<>();
        profileItems.add(new ProfileItem("Nombre: ", "Annie"));
        profileItems.add(new ProfileItem("Apellido: ", "Larson"));
        profileItems.add(new ProfileItem("Correo: ", "annie.larson@gmail.com"));
        profileItems.add(new ProfileItem("Cambiar contraseña: ", ""));

        // Configurar el adaptador
        adapter = new ProfileAdapter(profileItems, this);
        recyclerView.setAdapter(adapter);

        // Botón para regresar a la actividad anterior
        button1 = view.findViewById(R.id.back_login5);
        button1.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), activity1.class);
            startActivity(intent);
        });

        return view;
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
            dialog.dismiss();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
