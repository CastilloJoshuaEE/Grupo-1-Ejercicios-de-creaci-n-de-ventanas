package com.grupo.loginapp;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class RegistroActivity extends AppCompatActivity {

    private static final String TAG = "RegistroActivity";

    // Campos del formulario
    private EditText campoCedula;
    private EditText campoNombres;
    private EditText campoApellidos;
    private EditText campoEdad;
    private Spinner spinnerNacionalidad;
    private Spinner spinnerGenero;
    private RadioGroup radioGroupEstadoCivil;
    private TextView txtFechaNacimiento;
    private RatingBar ratingBarIngles;

    // Botones
    private Button botonRegistrar;
    private Button botonBorrar;
    private Button botonCancelar;

    // Fecha seleccionada
    private String fechaSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Vincular vistas
        campoCedula         = findViewById(R.id.campoCedula);
        campoNombres        = findViewById(R.id.campoNombres);
        campoApellidos      = findViewById(R.id.campoApellidos);
        campoEdad           = findViewById(R.id.campoEdad);
        spinnerNacionalidad = findViewById(R.id.spinnerNacionalidad);
        spinnerGenero       = findViewById(R.id.spinnerGenero);
        radioGroupEstadoCivil = findViewById(R.id.radioGroupEstadoCivil);
        txtFechaNacimiento  = findViewById(R.id.txtFechaNacimiento);
        ratingBarIngles     = findViewById(R.id.ratingBarIngles);
        botonRegistrar      = findViewById(R.id.botonRegistrar);
        botonBorrar         = findViewById(R.id.botonBorrar);
        botonCancelar       = findViewById(R.id.botonCancelar);

        configurarSpinners();
        configurarBotonFecha();
        configurarBotones();
    }

    private void configurarSpinners() {
        // Spinner Nacionalidad
        String[] nacionalidades = {
                "Seleccione...",
                "Ecuatoriana", "Colombiana", "Peruana", "Venezolana",
                "Chilena", "Argentina", "Brasileña", "Mexicana",
                "Estadounidense", "Española", "Otra"
        };
        ArrayAdapter<String> adapterNac = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, nacionalidades);
        adapterNac.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNacionalidad.setAdapter(adapterNac);

        // Spinner Género
        String[] generos = {"Seleccione...", "Masculino", "Femenino", "No binario", "Prefiero no decir"};
        ArrayAdapter<String> adapterGen = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, generos);
        adapterGen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGenero.setAdapter(adapterGen);
    }

    private void configurarBotonFecha() {
        txtFechaNacimiento.setOnClickListener(v -> {
            DatePickerFragment datePicker = new DatePickerFragment();
            datePicker.setOnDateSelectedListener((year, month, day) -> {
                fechaSeleccionada = String.format(Locale.getDefault(),
                        "%02d/%02d/%04d", day, month + 1, year);
                txtFechaNacimiento.setText(fechaSeleccionada);
            });
            datePicker.show(getSupportFragmentManager(), "datePicker");
        });
    }

    private void configurarBotones() {
        botonRegistrar.setOnClickListener(v -> registrarDatos());

        botonBorrar.setOnClickListener(v -> borrarCampos());

        botonCancelar.setOnClickListener(v -> finish());
    }

    private void registrarDatos() {
        // Recolectar datos
        String cedula      = campoCedula.getText().toString().trim();
        String nombres     = campoNombres.getText().toString().trim();
        String apellidos   = campoApellidos.getText().toString().trim();
        String edad        = campoEdad.getText().toString().trim();
        String nacionalidad = spinnerNacionalidad.getSelectedItem().toString();
        String genero      = spinnerGenero.getSelectedItem().toString();

        // Estado civil
        int radioId = radioGroupEstadoCivil.getCheckedRadioButtonId();
        String estadoCivil = "";
        if (radioId != -1) {
            RadioButton radioSeleccionado = findViewById(radioId);
            estadoCivil = radioSeleccionado.getText().toString();
        }

        String fecha       = txtFechaNacimiento.getText().toString();
        float nivelIngles  = ratingBarIngles.getRating();

        // Validación básica
        if (cedula.isEmpty() || nombres.isEmpty() || apellidos.isEmpty() || edad.isEmpty()) {
            Toast.makeText(this, "Por favor complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nacionalidad.equals("Seleccione...") || genero.equals("Seleccione...")) {
            Toast.makeText(this, "Seleccione Nacionalidad y Género", Toast.LENGTH_SHORT).show();
            return;
        }
        if (radioId == -1) {
            Toast.makeText(this, "Seleccione el Estado Civil", Toast.LENGTH_SHORT).show();
            return;
        }
        if (fecha.isEmpty() || fecha.equals("Toque para seleccionar fecha")) {
            Toast.makeText(this, "Seleccione la Fecha de Nacimiento", Toast.LENGTH_SHORT).show();
            return;
        }

        // Registrar en el LOG del sistema
        Log.i(TAG, " DATOS REGISTRADOS");
        Log.i(TAG, "Cédula:            " + cedula);
        Log.i(TAG, "Nombres:           " + nombres);
        Log.i(TAG, "Apellidos:         " + apellidos);
        Log.i(TAG, "Edad:              " + edad);
        Log.i(TAG, "Nacionalidad:      " + nacionalidad);
        Log.i(TAG, "Género:            " + genero);
        Log.i(TAG, "Estado Civil:      " + estadoCivil);
        Log.i(TAG, "Fecha Nacimiento:  " + fecha);
        Log.i(TAG, "Nivel de Inglés:   " + nivelIngles + " / 5 estrellas");
        // Mostrar Toast de confirmación
        Toast.makeText(this, " Datos ingresados correctamente", Toast.LENGTH_LONG).show();
    }

    private void borrarCampos() {
        campoCedula.setText("");
        campoNombres.setText("");
        campoApellidos.setText("");
        campoEdad.setText("");
        spinnerNacionalidad.setSelection(0);
        spinnerGenero.setSelection(0);
        radioGroupEstadoCivil.clearCheck();
        txtFechaNacimiento.setText("Toque para seleccionar fecha");
        fechaSeleccionada = "";
        ratingBarIngles.setRating(0);
    }
}
