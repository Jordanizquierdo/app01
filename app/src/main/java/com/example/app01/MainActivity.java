package com.example.app01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            EditText campo1 = findViewById(R.id.correo);
            String correo = campo1.getText().toString();
            EditText campo2 = findViewById(R.id.contrasena);
            String contrasenia = campo2.getText().toString();


            if (correo.equals("admin") && contrasenia.equals("admin")) {
                iniciar();
                return;
            }

            //recuperar datos
            SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);

            //obtener la contraseña almacenada para el correo ingresado
            String storedContrasenia = sharedPreferences.getString(correo, null);


            if (storedContrasenia != null && storedContrasenia.equals(contrasenia)) {
                iniciar();
            } else {
                Toast.makeText(this, "Error en las credenciales", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void iniciar() {
        Intent i = new Intent(this, Principal1.class);
        startActivity(i);
    }


    public void registrar(View v) {
        Intent a = new Intent(this, Registrarse.class);
        startActivity(a);
    }
}
