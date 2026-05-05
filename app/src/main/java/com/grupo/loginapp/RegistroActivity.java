package com.grupo.loginapp;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Locale;

public class RegistroActivity extends AppCompatActivity {

    private static final String TAG = "RegistroActivity";
    private static final String ARCHIVO_REGISTROS = "RegistroUsuario.txt";

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
    private Button botonVerRegistros;  // Nuevo botón

    // Fecha seleccionada
    private String fechaSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Vincular vistas
        campoCedula           = findViewById(R.id.campoCedula);
        campoNombres          = findViewById(R.id.campoNombres);
        campoApellidos        = findViewById(R.id.campoApellidos);
        campoEdad             = findViewById(R.id.campoEdad);
        spinnerNacionalidad   = findViewById(R.id.spinnerNacionalidad);
        spinnerGenero         = findViewById(R.id.spinnerGenero);
        radioGroupEstadoCivil = findViewById(R.id.radioGroupEstadoCivil);
        txtFechaNacimiento    = findViewById(R.id.txtFechaNacimiento);
        ratingBarIngles       = findViewById(R.id.ratingBarIngles);
        botonRegistrar        = findViewById(R.id.botonRegistrar);
        botonBorrar           = findViewById(R.id.botonBorrar);
        botonCancelar         = findViewById(R.id.botonCancelar);
        botonVerRegistros     = findViewById(R.id.botonVerRegistros);

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
        botonVerRegistros.setOnClickListener(v -> mostrarRegistros());
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
        } else if (!cedula.matches("\\d+")) {
            campoCedula.setError("La cédula debe contener solo números");
            campoCedula.requestFocus();
            return;
        } else if (cedula.length() != 10) {
            campoCedula.setError("La cédula debe tener exactamente 10 dígitos");
            campoCedula.requestFocus();
            return;
        }

        // Validacion Nombres
        if (nombres.isEmpty()) {
            campoNombres.setError("Ingrese sus nombres");
            campoNombres.requestFocus();
            return;
        } else if (!nombres.matches(regexLetras)) {
            campoNombres.setError("Los nombres solo deben contener letras");
            campoNombres.requestFocus();
            return;
        }

        // Validacion Apellidos
        if (apellidos.isEmpty()) {
            campoApellidos.setError("Ingrese sus apellidos");
            campoApellidos.requestFocus();
            return;
        } else if (!apellidos.matches(regexLetras)) {
            campoApellidos.setError("Los apellidos solo deben contener letras");
            campoApellidos.requestFocus();
            return;
        }

        // Validacion Edad
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

        // Validacion Spinners
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

        float nivelIngles = ratingBarIngles.getRating();

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

        // Guardar en archivo TXT con formato: Campo1;Campo2;Campo3;...
        guardarEnArchivo(cedula, nombres, apellidos, edad, nacionalidad, 
                         genero, estadoCivil, fecha, nivelIngles);

        // Mostrar Toast de confirmación
        Toast.makeText(this, "Datos ingresados correctamente", Toast.LENGTH_LONG).show();
    }

    /**
     * Guarda los datos en un archivo TXT usando separador ;
     * Cada registro se agrega en una nueva línea
     */
    private void guardarEnArchivo(String cedula, String nombres, String apellidos, 
                                  String edad, String nacionalidad, String genero,
                                  String estadoCivil, String fechaNacimiento, float nivelIngles) {
        try {
            // Construir la línea de datos con separador ;
            String lineaDatos = cedula + ";" +
                    nombres + ";" +
                    apellidos + ";" +
                    edad + ";" +
                    nacionalidad + ";" +
                    genero + ";" +
                    estadoCivil + ";" +
                    fechaNacimiento + ";" +
                    nivelIngles;

            // Obtener el directorio de archivos externos de la app
            File directorio = getExternalFilesDir(null);
            File archivo = new File(directorio, ARCHIVO_REGISTROS);

            // Abrir en modo append (agregar al final)
            FileOutputStream fos = new FileOutputStream(archivo, true);
            OutputStreamWriter out = new OutputStreamWriter(fos);
            out.write(lineaDatos + "\n");
            out.close();

            Log.d(TAG, "Datos guardados en: " + archivo.getAbsolutePath());
        } catch (Exception e) {
            Log.e(TAG, "Error al guardar: " + e.getMessage());
            Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Recupera todos los registros del archivo y los muestra en un Modal (Dialog)
     */
    private void mostrarRegistros() {
        try {
            File directorio = getExternalFilesDir(null);
            File archivo = new File(directorio, ARCHIVO_REGISTROS);

            if (!archivo.exists()) {
                Toast.makeText(this, "No hay registros almacenados", Toast.LENGTH_SHORT).show();
                return;
            }

            // Leer todos los registros del archivo
            FileInputStream fis = new FileInputStream(archivo);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            StringBuilder contenido = new StringBuilder();
            String linea;

            int contador = 1;
            while ((linea = reader.readLine()) != null) {
                // Dividir la línea por el separador ;
                String[] campos = linea.split(";");
                
                contenido.append("=== REGISTRO ").append(contador).append(" ===\n");
                
                if (campos.length >= 9) {
                    contenido.append("Cédula: ").append(campos[0]).append("\n");
                    contenido.append("Nombres: ").append(campos[1]).append("\n");
                    contenido.append("Apellidos: ").append(campos[2]).append("\n");
                    contenido.append("Edad: ").append(campos[3]).append("\n");
                    contenido.append("Nacionalidad: ").append(campos[4]).append("\n");
                    contenido.append("Género: ").append(campos[5]).append("\n");
                    contenido.append("Estado Civil: ").append(campos[6]).append("\n");
                    contenido.append("Fecha Nacimiento: ").append(campos[7]).append("\n");
                    contenido.append("Nivel Inglés: ").append(campos[8]).append(" / 5\n");
                } else {
                    contenido.append("Datos corruptos o incompletos: ").append(linea).append("\n");
                }
                contenido.append("\n");
                contador++;
            }
            reader.close();

            // Mostrar en un Modal (Dialog)
            VerRegistrosDialog dialog = VerRegistrosDialog.newInstance(contenido.toString());
            dialog.show(getSupportFragmentManager(), "VerRegistrosDialog");

        } catch (Exception e) {
            Log.e(TAG, "Error al leer registros: " + e.getMessage());
            Toast.makeText(this, "Error al leer los registros", Toast.LENGTH_SHORT).show();
        }
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