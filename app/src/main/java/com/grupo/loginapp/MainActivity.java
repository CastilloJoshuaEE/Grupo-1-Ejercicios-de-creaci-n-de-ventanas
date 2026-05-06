package com.grupo.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        // Configurar Toolbar como ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Login App");
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_acerca_de) {
            // Mostrar el modal Acerca de
            AcercaDeDialog dialog = AcercaDeDialog.newInstance();
            dialog.show(getSupportFragmentManager(), "AcercaDeDialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
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
