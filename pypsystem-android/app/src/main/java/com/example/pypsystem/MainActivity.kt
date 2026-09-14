package com.example.pypsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val usuario = findViewById<EditText>(R.id.etUsuario)
        val contrasena = findViewById<EditText>(R.id.etContrasena)
        val botonIniciar = findViewById<Button>(R.id.btnIniciarSesion)

        botonIniciar.setOnClickListener {

            val nombreUsuario = usuario.text.toString().trim()
            val clave = contrasena.text.toString().trim()

            if (nombreUsuario == "paciente" && clave == "1234") {

                val intent = Intent(this, RegistrarSintomasActivity::class.java)
                startActivity(intent)

            } else if (nombreUsuario == "salud" && clave == "1234") {

                val intent = Intent(this, PanelSaludActivity::class.java)
                startActivity(intent)

            } else {

                Toast.makeText(
                    this,
                    "Usuario o contraseña incorrectos",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}