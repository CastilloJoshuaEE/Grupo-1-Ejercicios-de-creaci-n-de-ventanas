package com.grupo.loginapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {

    private lateinit var usuario: TextInputEditText
    private lateinit var contrasena: TextInputEditText
    private lateinit var botonLogin: MaterialButton

    // Datos hardcoded
    private val USER_CORRECTO = "admin"
    private val PASS_CORRECTO = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Vincular con XML
        usuario = findViewById(R.id.campoNombreUsuario)
        contrasena = findViewById(R.id.campoContrasena)
        botonLogin = findViewById(R.id.botonIniciarSesion)

        // Evento del botón
        botonLogin.setOnClickListener {
            validarLogin()
        }
    }

    private fun validarLogin() {
        val user = usuario.text.toString().trim()
        val pass = contrasena.text.toString().trim()

        // Validar campos vacíos
        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show()
            return
        }

        // Validar credenciales
        if (user == USER_CORRECTO && pass == PASS_CORRECTO) {
            Toast.makeText(this, "Bienvenido $user", Toast.LENGTH_SHORT).show()



        } else {
            Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_SHORT).show()
        }
    }
}
