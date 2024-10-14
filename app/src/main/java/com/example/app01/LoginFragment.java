package com.example.app01;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private Button loginButton;
    private Button registerButton;  // Agregar el botón de registro
    private EditText emailField, passwordField;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        // Inicializar Firebase correctamente con el contexto del Fragment
        FirebaseApp.initializeApp(requireContext());
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Inicializar campos y botones
        emailField = view.findViewById(R.id.correo);
        passwordField = view.findViewById(R.id.contrasena);
        loginButton = view.findViewById(R.id.button);
        registerButton = view.findViewById(R.id.btn2);  // Inicializar el botón de registro

        // Ejemplo de agregar un usuario a Firestore
        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);

        db.collection("users")
                .add(user)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        // Configurar listener del botón de login
        loginButton.setOnClickListener(v -> {
            String correo = emailField.getText().toString();
            String contrasenia = passwordField.getText().toString();

            // Comprobar credenciales de administrador
            if (correo.equals("admin") && contrasenia.equals("admin")) {
                iniciar();
                return;
            }

            // Recuperar datos de SharedPreferences
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
            String storedContrasenia = sharedPreferences.getString(correo, null);

            // Verificar si la contraseña coincide
            if (storedContrasenia != null && storedContrasenia.equals(contrasenia)) {
                iniciar();  // Iniciar nueva actividad si las credenciales son correctas
            } else {
                Toast.makeText(getActivity(), "Error en las credenciales", Toast.LENGTH_SHORT).show();
            }
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
}
