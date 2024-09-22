package com.example.app01;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HistorialActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private HistorialAdapter adapter;
    private List<Consulta> consultas;
    ImageView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial);

        recyclerView = findViewById(R.id.recyclerView);
        consultas = new ArrayList<>();

        //Agregar consultas
        consultas.add(new Consulta("Firulais", "5/10/2023"));
        consultas.add(new Consulta("Firulais", "2/3/2024"));
        consultas.add(new Consulta("Rony", "5/3/2024"));

        //Inicializar el adaptador con el listener de clic y contexto
        adapter = new HistorialAdapter(consultas, this, new HistorialAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Consulta consulta) {
                // Acción al hacer clic en un ítem
                Toast.makeText(HistorialActivity.this, "Consulta de: " + consulta.getNombreMascota(), Toast.LENGTH_SHORT).show();
            }
        });

        //Configurar el adaptador en el RecyclerView
        recyclerView.setAdapter(adapter);

        button = findViewById(R.id.back_login2);
        button.setOnClickListener(v -> {
            Intent a = new Intent(getApplicationContext(), Principal1.class);
            startActivity(a);
        });
    }
}
