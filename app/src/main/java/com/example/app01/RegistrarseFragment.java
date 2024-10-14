package com.example.app01;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class RegistrarseFragment extends Fragment {

    EditText emailEditText, passwordEditText, password2EditText;
    Button registrarButton;
    ImageView button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrarse, container, false);

        emailEditText = view.findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = view.findViewById(R.id.editTextTextPassword);
        password2EditText = view.findViewById(R.id.editTextTextPassword2);
        registrarButton = view.findViewById(R.id.button4);

        registrarButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String password2 = password2EditText.getText().toString();

            if (email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(password2)) {
                Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else {
                // Llamar al método para guardar los datos
                guardarUsuario(email, password);

                // Iniciar la nueva Activity
                Intent intent = new Intent(getActivity(),Principal1.class);
                startActivity(intent);
            }
        });


        button = view.findViewById(R.id.back_login);
        button.setOnClickListener(v -> {
            // Cambiar al fragmento de registro
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
            fragmentTransaction.addToBackStack(null);  // Para permitir volver al login si es necesario
            fragmentTransaction.commit();
        });



        return view;
    }

    // guardar el usuario en SharedPreferences
    private void guardarUsuario(String email, String password) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(email, password);
        editor.apply();

        Toast.makeText(getContext(), "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
    }
}
