package com.example.app01;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetalleConsultaActivity extends AppCompatActivity {

    private TextView nombreMascotaDetalle;
    private TextView fechaDetalle;
    private TextView diagnosticoDetalle;
    private TextView tratamientoDetalle;
    ImageView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_consulta);

        nombreMascotaDetalle = findViewById(R.id.nombreMascotaDetalle);
        fechaDetalle = findViewById(R.id.fechaDetalle);
        TextView veterinarioDetalle = findViewById(R.id.veterinarioDetalle);
        diagnosticoDetalle = findViewById(R.id.diagnosticoDetalle);
        tratamientoDetalle = findViewById(R.id.tratamientoDetalle);

        //Obtener los datos del Intent
        Intent intent = getIntent();
        String nombreMascota = intent.getStringExtra("nombreMascota");
        String fecha = intent.getStringExtra("fecha");
        String veterinario = intent.getStringExtra("veterinario");
        String diagnostico = intent.getStringExtra("diagnostico");
        String tratamiento = intent.getStringExtra("tratamiento");

        //Establecer los datos en los TextViews
        nombreMascotaDetalle.setText(nombreMascota);
        fechaDetalle.setText(fecha);
        veterinarioDetalle.setText(veterinario);
        diagnosticoDetalle.setText(diagnostico);
        tratamientoDetalle.setText(tratamiento);

        button = findViewById(R.id.back1);
        button.setOnClickListener(v -> {
            Intent a = new Intent(getApplicationContext(), HistorialActivity.class);
            startActivity(a);
        });
    }
}
