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

        String regexLetras = "[a-zA-ZáéíóúñÑ ]+";

        // Validacion Cedula
        if (cedula.isEmpty()) {
            campoCedula.setError("La cédula es obligatoria");
            campoCedula.requestFocus();
            return;
        } else if (!cedula.matches("\\d+")) { // Solo dígitos
            campoCedula.setError("La cédula debe contener solo números");
            campoCedula.requestFocus();
            return;
        } else if (cedula.length() != 10) { // Validación de longitud
            campoCedula.setError("La cédula debe tener exactamente 10 dígitos");
            campoCedula.requestFocus();
            return;
        }

        // Validacion Nombres: no vacio y solo letras
        if (nombres.isEmpty()) {
            campoNombres.setError("Ingrese sus nombres");
            campoNombres.requestFocus();
            return;
        } else if (!nombres.matches(regexLetras)) {
            campoNombres.setError("Los nombres solo deben contener letras");
            campoNombres.requestFocus();
            return;
        }
        // Validacion Apellidos: no vacio y solo letras
        if (apellidos.isEmpty()) {
            campoApellidos.setError("Ingrese sus apellidos");
            campoApellidos.requestFocus();
            return;
        } else if (!apellidos.matches(regexLetras)) {
            campoApellidos.setError("Los apellidos solo deben contener letras");
            campoApellidos.requestFocus();
            return;
        }
        // Validacion Edad: no vacio
        if (edad.isEmpty()) {
            campoEdad.setError("Ingrese la edad");
            campoEdad.requestFocus();
            return;
        }
        int edadInt = Integer.parseInt(edad);
        if (edadInt < 1 || edadInt > 120) {
            campoEdad.setError("La edad debe estar entre 1 y 120 años");
            campoEdad.requestFocus();
            return;
        }

        // Validacion Spinners (Nacionalidad y Género)
        if (spinnerNacionalidad.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Debe seleccionar una nacionalidad", Toast.LENGTH_SHORT).show();
            return;
        }
        if (spinnerGenero.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Debe seleccionar un género", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validación Fecha de Nacimiento
        String fecha = txtFechaNacimiento.getText().toString();
        if (fecha.isEmpty() || fecha.equals("Toque para seleccionar fecha")) {
            Toast.makeText(this, "Debe seleccionar su fecha de nacimiento", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validacion Estado Civil
        int radioId = radioGroupEstadoCivil.getCheckedRadioButtonId();

        if (radioId == -1) {
            Toast.makeText(this, "Debe seleccionar un estado civil", Toast.LENGTH_SHORT).show();
            return;
        }
        RadioButton radioSeleccionado = findViewById(radioId);
        String estadoCivil = radioSeleccionado.getText().toString();

        //

        float nivelIngles  = ratingBarIngles.getRating();



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
