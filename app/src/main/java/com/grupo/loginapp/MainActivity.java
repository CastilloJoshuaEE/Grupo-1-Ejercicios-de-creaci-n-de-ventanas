package com.grupo.loginapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText campoUsuario;
    private TextInputEditText campoContrasena;

    private MaterialButton botonLogin;
    private MaterialButton botonCrearCuenta;
    private MaterialButton botonAcercaDe;

    private Button btn_Cerrar;

    private CheckBox checkRecordar;

    private static final String ARCHIVO_REGISTROS = "RegistroUsuario.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ==============================
        // VINCULAR COMPONENTES
        // ==============================
        campoUsuario = findViewById(R.id.campoNombreUsuario);
        campoContrasena = findViewById(R.id.campoContrasena);

        botonLogin = findViewById(R.id.botonIniciarSesion);
        botonCrearCuenta = findViewById(R.id.botonCrearCuenta);
        botonAcercaDe = findViewById(R.id.botonAcercaDe);

        btn_Cerrar = findViewById(R.id.btn_Cerrar);

        checkRecordar = findViewById(R.id.checkRecordar);

        // ==============================
        // RECORDAR SESIÓN (SOLO AUTOLLENAR)
        // ==============================
        SharedPreferences preferences =
                getSharedPreferences("MisPreferencias", MODE_PRIVATE);

        String usuarioGuardado = preferences.getString("usuario", "");
        String passwordGuardada = preferences.getString("password", "");

        if (!usuarioGuardado.isEmpty() && !passwordGuardada.isEmpty()) {

            campoUsuario.setText(usuarioGuardado);
            campoContrasena.setText(passwordGuardada);
            checkRecordar.setChecked(true);

            Toast.makeText(
                    this,
                    "Datos cargados automáticamente",
                    Toast.LENGTH_SHORT
            ).show();
        }

        // ==============================
        // LOGIN
        // ==============================
        botonLogin.setOnClickListener(v -> validarLogin());

        // ==============================
        // CREAR CUENTA
        // ==============================
        botonCrearCuenta.setOnClickListener(v -> {

            try {
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Error al abrir Registro", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        // ==============================
        // ACERCA DE
        // ==============================
        botonAcercaDe.setOnClickListener(v -> {
            AcercaDeDialog dialog = AcercaDeDialog.newInstance();
            dialog.show(getSupportFragmentManager(), "AcercaDeDialog");
        });

        // ==============================
        // CERRAR SESIÓN
        // ==============================
        btn_Cerrar.setOnClickListener(v -> {

            SharedPreferences.Editor editor =
                    preferences.edit();

            editor.clear();
            editor.apply();

            campoUsuario.setText("");
            campoContrasena.setText("");
            checkRecordar.setChecked(false);

            Toast.makeText(
                    MainActivity.this,
                    "Datos eliminados",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    // =========================================
    // VALIDAR LOGIN
    // =========================================
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

        try {

            File directorio = getExternalFilesDir(null);
            File archivo = new File(directorio, ARCHIVO_REGISTROS);

            if (!archivo.exists()) {
                Toast.makeText(this,
                        "No existen usuarios registrados",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            FileInputStream fis = new FileInputStream(archivo);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String linea;
            boolean acceso = false;

            while ((linea = reader.readLine()) != null) {

                String[] campos = linea.split(";");

                if (campos.length >= 2) {

                    String usuario = campos[0];
                    String password = campos[1];

                    if (user.equals(usuario) && pass.equals(password)) {
                        acceso = true;
                        break;
                    }
                }
            }

            reader.close();

            // ==========================
            // LOGIN CORRECTO
            // ==========================
            if (acceso) {

                if (checkRecordar.isChecked()) {

                    SharedPreferences.Editor editor =
                            getSharedPreferences("MisPreferencias", MODE_PRIVATE).edit();

                    editor.putString("usuario", user);
                    editor.putString("password", pass);
                    editor.apply();
                }

                Toast.makeText(
                        this,
                        "Bienvenido " + user,
                        Toast.LENGTH_LONG
                ).show();

            } else {
                Toast.makeText(
                        this,
                        "Usuario o contraseña incorrectos",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (Exception e) {
            Toast.makeText(this,
                    "Error al iniciar sesión",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}