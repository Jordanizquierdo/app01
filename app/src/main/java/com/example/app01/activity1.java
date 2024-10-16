package com.example.app01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

public class activity1 extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cargar el fragmento Principal1Fragment en esta actividad
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new Principal1Fragment())
                    .commit();
        }
    }
    public void cerrar(View v){
        Intent a = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(a);
    }
}
