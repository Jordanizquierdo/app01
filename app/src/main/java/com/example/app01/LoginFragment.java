package com.example.app01;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginFragment extends Fragment {

    private Button loginButton;
    private Button registerButton;  // Agregar el botón de registro
    private EditText emailField, passwordField;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Inicializar Firebase correctamente con el contexto del Fragment
        FirebaseApp.initializeApp(requireContext());
        db = FirebaseFirestore.getInstance();

        // Inicializar campos y botones
        emailField = view.findViewById(R.id.correo);
        passwordField = view.findViewById(R.id.contrasena);
        loginButton = view.findViewById(R.id.button);
        registerButton = view.findViewById(R.id.btn2);  // Inicializar el botón de registro

        // Configurar listener del botón de login
        loginButton.setOnClickListener(v -> {
            String correo = emailField.getText().toString();
            String contrasenia = passwordField.getText().toString();

            // Comprobar credenciales de administrador
            if (correo.equals("admin") && contrasenia.equals("admin")) {
                iniciar();
                return;
            }

            // Verificar usuario en Firebase Firestore
            verificarUsuario(correo, contrasenia);
        });

        // Configurar listener del botón de registro
        registerButton.setOnClickListener(v -> {
            // Cambiar al fragmento de registro
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new RegistrarseFragment());
            fragmentTransaction.addToBackStack(null);  // Para permitir volver al login si es necesario
            fragmentTransaction.commit();
        });

        return view;
    }

    // Método para iniciar la nueva actividad Principal1
    private void iniciar() {
        Intent i = new Intent(getActivity(), Principal1.class);
        startActivity(i);
    }

    // Verificar si el usuario existe en Firestore
    private void verificarUsuario(String correo, String contrasenia) {
        db.collection("users")
                .whereEqualTo("email", correo)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (!querySnapshot.isEmpty()) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String storedContrasenia = document.getString("password");
                                if (storedContrasenia != null && storedContrasenia.equals(contrasenia)) {
                                    iniciar();  // Iniciar la actividad
                                } else {
                                    Toast.makeText(getActivity(), "Error en las credenciales", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.w(TAG, "Error obteniendo documentos: ", task.getException());
                        Toast.makeText(getActivity(), "Error al acceder a la base de datos", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
