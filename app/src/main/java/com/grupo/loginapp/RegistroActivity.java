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
    private EditText campoUsuario;
    private EditText campoPassword;
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
    private Button botonVerRegistros;

    // Fecha seleccionada
    private String fechaSeleccionada = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        // Vincular vistas
        campoUsuario = findViewById(R.id.campoUsuarioRegistro);
        campoPassword = findViewById(R.id.campoPasswordRegistro);;

        campoCedula = findViewById(R.id.campoCedula);
        campoNombres = findViewById(R.id.campoNombres);
        campoApellidos = findViewById(R.id.campoApellidos);
        campoEdad = findViewById(R.id.campoEdad);

        spinnerNacionalidad = findViewById(R.id.spinnerNacionalidad);
        spinnerGenero = findViewById(R.id.spinnerGenero);

        radioGroupEstadoCivil = findViewById(R.id.radioGroupEstadoCivil);

        txtFechaNacimiento = findViewById(R.id.txtFechaNacimiento);

        ratingBarIngles = findViewById(R.id.ratingBarIngles);

        botonRegistrar = findViewById(R.id.botonRegistrar);
        botonBorrar = findViewById(R.id.botonBorrar);
        botonCancelar = findViewById(R.id.botonCancelar);
        botonVerRegistros = findViewById(R.id.botonVerRegistros);

        configurarSpinners();
        configurarBotonFecha();
        configurarBotones();
    }

    private void configurarSpinners() {

        String[] nacionalidades = {
                "Seleccione...",
                "Ecuatoriana",
                "Colombiana",
                "Peruana",
                "Venezolana",
                "Chilena",
                "Argentina",
                "Brasileña",
                "Mexicana",
                "Estadounidense",
                "Española",
                "Otra"
        };

        ArrayAdapter<String> adapterNac = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                nacionalidades
        );

        adapterNac.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerNacionalidad.setAdapter(adapterNac);

        // Género
        String[] generos = {
                "Seleccione...",
                "Masculino",
                "Femenino",
                "No binario",
                "Prefiero no decir"
        };

        ArrayAdapter<String> adapterGen = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                generos
        );

        adapterGen.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spinnerGenero.setAdapter(adapterGen);
    }

    private void configurarBotonFecha() {

        txtFechaNacimiento.setOnClickListener(v -> {

            DatePickerFragment datePicker = new DatePickerFragment();

            datePicker.setOnDateSelectedListener((year, month, day) -> {

                fechaSeleccionada = String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%04d",
                        day,
                        month + 1,
                        year
                );

                txtFechaNacimiento.setText(fechaSeleccionada);
            });

            datePicker.show(
                    getSupportFragmentManager(),
                    "datePicker"
            );
        });
    }

    private void configurarBotones() {

        botonRegistrar.setOnClickListener(v -> registrarDatos());

        botonBorrar.setOnClickListener(v -> borrarCampos());

        botonCancelar.setOnClickListener(v -> finish());

        botonVerRegistros.setOnClickListener(v -> mostrarRegistros());
    }

    private void registrarDatos() {

        String usuario = campoUsuario.getText().toString().trim();
        String password = campoPassword.getText().toString().trim();

        String cedula = campoCedula.getText().toString().trim();
        String nombres = campoNombres.getText().toString().trim();
        String apellidos = campoApellidos.getText().toString().trim();
        String edad = campoEdad.getText().toString().trim();

        String nacionalidad = spinnerNacionalidad.getSelectedItem().toString();

        String genero = spinnerGenero.getSelectedItem().toString();

        String regexLetras = "[a-zA-ZáéíóúñÑ ]+";

        // Usuario
        if (usuario.isEmpty()) {

            campoUsuario.setError("Ingrese un usuario");
            campoUsuario.requestFocus();
            return;
        }

        // Password
        if (password.isEmpty()) {

            campoPassword.setError("Ingrese una contraseña");
            campoPassword.requestFocus();
            return;
        }

        // Cedula
        if (cedula.isEmpty()) {

            campoCedula.setError("La cédula es obligatoria");
            campoCedula.requestFocus();
            return;

        } else if (!cedula.matches("\\d+")) {

            campoCedula.setError("La cédula debe contener solo números");
            campoCedula.requestFocus();
            return;

        } else if (cedula.length() != 10) {

            campoCedula.setError("La cédula debe tener 10 dígitos");
            campoCedula.requestFocus();
            return;
        }

        // Nombres
        if (nombres.isEmpty()) {

            campoNombres.setError("Ingrese sus nombres");
            campoNombres.requestFocus();
            return;

        } else if (!nombres.matches(regexLetras)) {

            campoNombres.setError("Solo letras");
            campoNombres.requestFocus();
            return;
        }

        // Apellidos
        if (apellidos.isEmpty()) {

            campoApellidos.setError("Ingrese sus apellidos");
            campoApellidos.requestFocus();
            return;

        } else if (!apellidos.matches(regexLetras)) {

            campoApellidos.setError("Solo letras");
            campoApellidos.requestFocus();
            return;
        }

        // Edad
        if (edad.isEmpty()) {

            campoEdad.setError("Ingrese edad");
            campoEdad.requestFocus();
            return;
        }

        int edadInt = Integer.parseInt(edad);

        if (edadInt < 1 || edadInt > 120) {

            campoEdad.setError("Edad inválida");
            campoEdad.requestFocus();
            return;
        }

        // Nacionalidad
        if (spinnerNacionalidad.getSelectedItemPosition() == 0) {

            Toast.makeText(
                    this,
                    "Seleccione nacionalidad",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Género
        if (spinnerGenero.getSelectedItemPosition() == 0) {

            Toast.makeText(
                    this,
                    "Seleccione género",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Fecha
        String fecha = txtFechaNacimiento.getText().toString();

        if (fecha.isEmpty()
                || fecha.equals("Toque para seleccionar fecha")) {

            Toast.makeText(
                    this,
                    "Seleccione fecha",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        // Estado Civil
        int radioId = radioGroupEstadoCivil.getCheckedRadioButtonId();

        if (radioId == -1) {

            Toast.makeText(
                    this,
                    "Seleccione estado civil",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        RadioButton radioSeleccionado = findViewById(radioId);

        String estadoCivil =
                radioSeleccionado.getText().toString();

        float nivelIngles =
                ratingBarIngles.getRating();

        guardarEnArchivo(
                usuario,
                password,
                cedula,
                nombres,
                apellidos,
                edad,
                nacionalidad,
                genero,
                estadoCivil,
                fecha,
                nivelIngles
        );

        Toast.makeText(
                this,
                "Usuario registrado correctamente",
                Toast.LENGTH_LONG
        ).show();
    }

    private void guardarEnArchivo(
            String usuario,
            String password,
            String cedula,
            String nombres,
            String apellidos,
            String edad,
            String nacionalidad,
            String genero,
            String estadoCivil,
            String fechaNacimiento,
            float nivelIngles) {

        try {

            String lineaDatos =
                    usuario + ";" +
                            password + ";" +
                            cedula + ";" +
                            nombres + ";" +
                            apellidos + ";" +
                            edad + ";" +
                            nacionalidad + ";" +
                            genero + ";" +
                            estadoCivil + ";" +
                            fechaNacimiento + ";" +
                            nivelIngles;

            File directorio = getExternalFilesDir(null);

            File archivo =
                    new File(directorio, ARCHIVO_REGISTROS);

            FileOutputStream fos =
                    new FileOutputStream(archivo, true);

            OutputStreamWriter out =
                    new OutputStreamWriter(fos);

            out.write(lineaDatos + "\n");

            out.close();

            Log.d(TAG,
                    "Datos guardados correctamente");

        } catch (Exception e) {

            Log.e(TAG,
                    "Error al guardar: " + e.getMessage());

            Toast.makeText(
                    this,
                    "Error al guardar",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void mostrarRegistros() {

        try {

            File directorio = getExternalFilesDir(null);

            File archivo =
                    new File(directorio, ARCHIVO_REGISTROS);

            if (!archivo.exists()) {

                Toast.makeText(
                        this,
                        "No hay registros",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            FileInputStream fis =
                    new FileInputStream(archivo);

            BufferedReader reader =
                    new BufferedReader(
                            new InputStreamReader(fis)
                    );

            StringBuilder contenido =
                    new StringBuilder();

            String linea;

            int contador = 1;

            while ((linea = reader.readLine()) != null) {

                String[] campos = linea.split(";");

                contenido.append("=== REGISTRO ")
                        .append(contador)
                        .append(" ===\n");

                if (campos.length >= 11) {

                    contenido.append("Usuario: ")
                            .append(campos[0]).append("\n");

                    contenido.append("Contraseña: ")
                            .append(campos[1]).append("\n");

                    contenido.append("Cédula: ")
                            .append(campos[2]).append("\n");

                    contenido.append("Nombres: ")
                            .append(campos[3]).append("\n");

                    contenido.append("Apellidos: ")
                            .append(campos[4]).append("\n");

                    contenido.append("Edad: ")
                            .append(campos[5]).append("\n");

                    contenido.append("Nacionalidad: ")
                            .append(campos[6]).append("\n");

                    contenido.append("Género: ")
                            .append(campos[7]).append("\n");

                    contenido.append("Estado Civil: ")
                            .append(campos[8]).append("\n");

                    contenido.append("Fecha Nacimiento: ")
                            .append(campos[9]).append("\n");

                    contenido.append("Nivel Inglés: ")
                            .append(campos[10]).append(" / 5\n");
                }

                contenido.append("\n");

                contador++;
            }

            reader.close();

            VerRegistrosDialog dialog =
                    VerRegistrosDialog.newInstance(
                            contenido.toString()
                    );

            dialog.show(
                    getSupportFragmentManager(),
                    "VerRegistrosDialog"
            );

        } catch (Exception e) {

            Log.e(TAG,
                    "Error al leer registros: " + e.getMessage());

            Toast.makeText(
                    this,
                    "Error al leer registros",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void borrarCampos() {

        campoUsuario.setText("");
        campoPassword.setText("");

        campoCedula.setText("");
        campoNombres.setText("");
        campoApellidos.setText("");
        campoEdad.setText("");

        spinnerNacionalidad.setSelection(0);
        spinnerGenero.setSelection(0);

        radioGroupEstadoCivil.clearCheck();

        txtFechaNacimiento.setText(
                "Toque para seleccionar fecha"
        );

        fechaSeleccionada = "";

        ratingBarIngles.setRating(0);
    }
}