package com.example.app01;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class RegistrarseFragment extends Fragment {

    EditText emailEditText, passwordEditText, password2EditText;
    Button registrarButton;
    ImageView button;
    FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registrarse, container, false);

        emailEditText = view.findViewById(R.id.editTextTextEmailAddress);
        passwordEditText = view.findViewById(R.id.editTextTextPassword);
        password2EditText = view.findViewById(R.id.editTextTextPassword2);
        registrarButton = view.findViewById(R.id.button4);

        FirebaseApp.initializeApp(requireContext());
        db = FirebaseFirestore.getInstance();

        registrarButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            String password2 = password2EditText.getText().toString();

            if (email.isEmpty() || password.isEmpty() || password2.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
            } else if (!password.equals(password2)) {
                Toast.makeText(getContext(), "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
            } else {
                verificarUsuario(email, password);
            }
        });

        button = view.findViewById(R.id.back_login);
        button.setOnClickListener(v -> {
            // Cambiar al fragmento de login
            FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
            fragmentTransaction.addToBackStack(null);  // Para permitir volver al login si es necesario
            fragmentTransaction.commit();
        });

        return view;
    }

    // Verificar si el usuario ya existe en Firestore
    private void verificarUsuario(String email, String password) {
        db.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            Toast.makeText(getContext(), "El usuario ya existe", Toast.LENGTH_SHORT).show();
                        } else {
                            // Guardar el nuevo usuario en Firestore
                            registrarUsuario(email, password);
                        }
                    } else {
                        Log.w(TAG, "Error verificando el usuario: ", task.getException());
                        Toast.makeText(getContext(), "Error al verificar el usuario", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Registrar usuario en Firestore
    private void registrarUsuario(String email, String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("email", email);
        user.put("password", password); // Considera cifrar la contraseña antes de guardarla

        db.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getContext(), "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
                    Log.w(TAG, "Error adding document", e);
                });
    }
}

