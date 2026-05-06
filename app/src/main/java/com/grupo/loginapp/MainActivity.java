package com.grupo.loginapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText campoUsuario;
    private TextInputEditText campoContrasena;
    private MaterialButton botonLogin;
    private MaterialButton botonCrearCuenta;
    private CheckBox checkRecordar;

    private static final String USER_CORRECTO = "admin";
    private static final String PASS_CORRECTO = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        campoUsuario = findViewById(R.id.campoNombreUsuario);
        campoContrasena = findViewById(R.id.campoContrasena);
        botonLogin = findViewById(R.id.botonIniciarSesion);
        botonCrearCuenta = findViewById(R.id.botonCrearCuenta);
        checkRecordar = findViewById(R.id.checkRecordar);

        SharedPreferences preferences =
                getSharedPreferences("datosLogin", MODE_PRIVATE);

        String usuarioGuardado =
                preferences.getString("usuario", null);

        if (usuarioGuardado != null) {

            Toast.makeText(this,
                    "Sesión iniciada automáticamente",
                    Toast.LENGTH_SHORT).show();

            Intent intent =
                    new Intent(MainActivity.this,
                            RegistroActivity.class);

            startActivity(intent);

            finish();
        }

        botonLogin.setOnClickListener(v -> validarLogin());

        botonCrearCuenta.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    private void validarLogin() {

        String user = campoUsuario.getText() != null
                ? campoUsuario.getText().toString().trim()
                : "";

        String pass = campoContrasena.getText() != null
                ? campoContrasena.getText().toString().trim()
                : "";

        if (user.isEmpty() || pass.isEmpty()) {

            Toast.makeText(this,
                    "Ingrese usuario y contraseña",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        if (user.equals(USER_CORRECTO)
                && pass.equals(PASS_CORRECTO)) {

            if (checkRecordar.isChecked()) {

                SharedPreferences preferences =
                        getSharedPreferences("datosLogin",
                                MODE_PRIVATE);

                SharedPreferences.Editor editor =
                        preferences.edit();

                editor.putString("usuario", user);
                editor.putString("clave", pass);

                editor.apply();
            }

            Toast.makeText(this,
                    "ACCESO CONCEDIDO " + user,
                    Toast.LENGTH_SHORT).show();

            Intent intent =
                    new Intent(MainActivity.this,
                            RegistroActivity.class);

            startActivity(intent);

        } else {

            Toast.makeText(this,
                    "Datos incorrectos",
                    Toast.LENGTH_SHORT).show();
        }
    }
}