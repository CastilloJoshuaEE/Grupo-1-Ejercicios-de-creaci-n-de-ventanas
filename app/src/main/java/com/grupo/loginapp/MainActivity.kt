package com.grupo.loginapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity() {


    private lateinit var contenedorNombreUsuario: TextInputLayout
    private lateinit var contenedorContrasena: TextInputLayout
    private lateinit var campoNombreUsuario: TextInputEditText
    private lateinit var campoContrasena: TextInputEditText
    private lateinit var botonIniciarSesion: MaterialButton

    // Credenciales válidas definidas en el código
    private val usuarioValido = "admin"
    private val contrasenaValida = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ===== Inicialización de vistas =====
        inicializarVistas()

        // ===== Configurar el botón de inicio de sesión =====
        botonIniciarSesion.setOnClickListener {
            validarCredenciales()
        }
    }

    /**
     * Inicializa todas las vistas del layout
     */
    private fun inicializarVistas() {
        contenedorNombreUsuario = findViewById(R.id.contenedorNombreUsuario)
        contenedorContrasena    = findViewById(R.id.contenedorContrasena)
        campoNombreUsuario      = findViewById(R.id.campoNombreUsuario)
        campoContrasena         = findViewById(R.id.campoContrasena)
        botonIniciarSesion      = findViewById(R.id.botonIniciarSesion)
    }

    /**
     * Valida las credenciales ingresadas por el usuario.
     * Muestra TOAST según el resultado de la validación.
     */
    private fun validarCredenciales() {
        // Limpiar errores previos
        contenedorNombreUsuario.error = null
        contenedorContrasena.error    = null

        // Obtener los valores ingresados
        val usuarioIngresado    = campoNombreUsuario.text.toString().trim()
        val contrasenaIngresada = campoContrasena.text.toString().trim()

        // Validar que los campos no estén vacíos
        var camposValidos = true

        if (usuarioIngresado.isEmpty()) {
            contenedorNombreUsuario.error = getString(R.string.error_campo_vacio)
            camposValidos = false
        }

        if (contrasenaIngresada.isEmpty()) {
            contenedorContrasena.error = getString(R.string.error_campo_vacio)
            camposValidos = false
        }

        if (!camposValidos) return

        // ===== Verificar credenciales =====
        val credencialesCorrectas = (usuarioIngresado == usuarioValido) &&
                                    (contrasenaIngresada == contrasenaValida)

        if (credencialesCorrectas) {
            // Credenciales correctas → Acceso Concedido
            mostrarToast(getString(R.string.mensaje_acceso_concedido))
        } else {
            // Credenciales incorrectas → Datos incorrectos
            mostrarToast(getString(R.string.mensaje_datos_incorrectos))
            limpiarCampos()
        }
    }

    /**
     * Muestra un mensaje Toast en pantalla
     * @param mensaje Texto a mostrar en el Toast
     */
    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }

    /**
     * Limpia los campos de texto cuando las credenciales son incorrectas
     */
    private fun limpiarCampos() {
        campoNombreUsuario.text?.clear()
        campoContrasena.text?.clear()
        campoNombreUsuario.requestFocus()
    }
}
