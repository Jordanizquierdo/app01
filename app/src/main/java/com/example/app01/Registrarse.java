package com.example.app01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app01.MainActivity;
import com.example.app01.R;

public class Registrarse extends AppCompatActivity {

    EditText emailEditText, passwordEditText, password2EditText;
    Button registrarButton;
    ImageView button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrarse);

        emailEditText = findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = findViewById(R.id.editTextTextPassword);
        password2EditText = findViewById(R.id.editTextTextPassword2);
        registrarButton = findViewById(R.id.button4);

        registrarButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String password2 = password2EditText.getText().toString();

            if (email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                Toast.makeText(Registrarse.this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(password2)) {
                Toast.makeText(Registrarse.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else {
                // Llamar al método para guardar los datos
                guardarUsuario(email, password);

                // Iniciar la nueva Activity
                Intent intent = new Intent(Registrarse.this, MainActivity.class);
                startActivity(intent);
            }
        });

        button = findViewById(R.id.back_login);
        button.setOnClickListener(v -> {
            Intent a = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(a);
        });
    }

    //guardar el usuario en SharedPreferences
    private void guardarUsuario(String email, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(email, password);
        editor.apply();

        Toast.makeText(Registrarse.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
    }
}
