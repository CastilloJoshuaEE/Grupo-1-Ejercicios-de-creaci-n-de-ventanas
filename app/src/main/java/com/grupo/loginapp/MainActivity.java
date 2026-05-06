package com.grupo.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText campoUsuario;
    private TextInputEditText campoContrasena;
    private MaterialButton botonLogin;
    private MaterialButton botonCrearCuenta;

    private static final String USER_CORRECTO = "admin";
    private static final String PASS_CORRECTO = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        campoUsuario   = findViewById(R.id.campoNombreUsuario);
        campoContrasena = findViewById(R.id.campoContrasena);
        botonLogin      = findViewById(R.id.botonIniciarSesion);
        botonCrearCuenta = findViewById(R.id.botonCrearCuenta);

        botonLogin.setOnClickListener(v -> validarLogin());

        botonCrearCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    private void validarLogin() {
        String user = campoUsuario.getText() != null
                ? campoUsuario.getText().toString().trim() : "";
        String pass = campoContrasena.getText() != null
                ? campoContrasena.getText().toString().trim() : "";

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        if (user.equals(USER_CORRECTO) && pass.equals(PASS_CORRECTO)) {
            Toast.makeText(this, " ACCESO CONCENDIDO" + user, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, " Datos incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}
