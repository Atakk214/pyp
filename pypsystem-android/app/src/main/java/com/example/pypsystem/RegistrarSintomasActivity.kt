package com.example.pypsystem

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import java.util.Calendar

class RegistrarSintomasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registrar_sintomas)

        // MENÚ LATERAL
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(Gravity.START)
        }

        // SELECTOR DE FECHA
        val fecha = findViewById<EditText>(R.id.etFecha)

        fecha.setOnClickListener {

            val calendario = Calendar.getInstance()

            val año = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val día = calendario.get(Calendar.DAY_OF_MONTH)

            val selectorFecha = DatePickerDialog(
                this,
                { _, añoSeleccionado, mesSeleccionado, díaSeleccionado ->

                    val fechaSeleccionada =
                        String.format(
                            "%02d/%02d/%04d",
                            díaSeleccionado,
                            mesSeleccionado + 1,
                            añoSeleccionado
                        )

                    fecha.setText(fechaSeleccionada)
                },
                año,
                mes,
                día
            )

            selectorFecha.show()
        }

        // OPCIONES DEL MENÚ

        val menuSintomas = findViewById<android.view.View>(R.id.menuSintomas)
        val menuMedicamentos = findViewById<android.view.View>(R.id.menuMedicamentos)
        val menuDiario = findViewById<android.view.View>(R.id.menuDiario)
        val menuAlertas = findViewById<android.view.View>(R.id.menuAlertas)
        val menuRecomendaciones = findViewById<android.view.View>(R.id.menuRecomendaciones)
        val menuEducacion = findViewById<android.view.View>(R.id.menuEducacion)

        menuSintomas.setOnClickListener {
            drawerLayout.closeDrawer(Gravity.START)
        }

        menuMedicamentos.setOnClickListener {
            startActivity(Intent(this, MedicamentosActivity::class.java))
            drawerLayout.closeDrawer(Gravity.START)
        }

        menuDiario.setOnClickListener {
            startActivity(Intent(this, DiarioActivity::class.java))
            drawerLayout.closeDrawer(Gravity.START)
        }

        menuAlertas.setOnClickListener {
            startActivity(Intent(this, AlertasActivity::class.java))
            drawerLayout.closeDrawer(Gravity.START)
        }

        menuRecomendaciones.setOnClickListener {
            startActivity(Intent(this, RecomendacionesActivity::class.java))
            drawerLayout.closeDrawer(Gravity.START)
        }

        menuEducacion.setOnClickListener {
            startActivity(Intent(this, EducacionActivity::class.java))
            drawerLayout.closeDrawer(Gravity.START)
        }
    }
}