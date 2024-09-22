package com.example.app01;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AjustesActivity extends AppCompatActivity implements ProfileAdapter.OnItemClickListener {

    private List<ProfileItem> profileItems;
    private ProfileAdapter adapter;
    ImageView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        profileItems = new ArrayList<>();
        profileItems.add(new ProfileItem("Nombre: ", "Annie"));
        profileItems.add(new ProfileItem("Apellido: ", "Larson"));
        profileItems.add(new ProfileItem("Correo: ", "annie.larson@gmail.com"));
        profileItems.add(new ProfileItem("Cambiar contraseña: ", ""));

        adapter = new ProfileAdapter(profileItems, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(ProfileItem item, int position) {
        showEditDialog(item, position);
    }

    private void showEditDialog(ProfileItem item, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar " + item.getLabel());

        final EditText input = new EditText(this);
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





        button = findViewById(R.id.back_login5);
        button.setOnClickListener(v -> {
            Intent a = new Intent(getApplicationContext(), Principal1.class);
            startActivity(a);
        });
    }
}