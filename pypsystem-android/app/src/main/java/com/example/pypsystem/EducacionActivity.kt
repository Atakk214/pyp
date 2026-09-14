package com.example.pypsystem

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout

class EducacionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_educacion)

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)

        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(Gravity.START)
        }

        findViewById<android.view.View>(R.id.menuSintomas).setOnClickListener {
            startActivity(Intent(this, RegistrarSintomasActivity::class.java))
        }

        findViewById<android.view.View>(R.id.menuMedicamentos).setOnClickListener {
            startActivity(Intent(this, MedicamentosActivity::class.java))
        }

        findViewById<android.view.View>(R.id.menuDiario).setOnClickListener {
            startActivity(Intent(this, DiarioActivity::class.java))
        }

        findViewById<android.view.View>(R.id.menuAlertas).setOnClickListener {
            startActivity(Intent(this, AlertasActivity::class.java))
        }

        findViewById<android.view.View>(R.id.menuRecomendaciones).setOnClickListener {
            startActivity(Intent(this, RecomendacionesActivity::class.java))
        }

        findViewById<android.view.View>(R.id.menuEducacion).setOnClickListener {
            drawerLayout.closeDrawer(Gravity.START)
        }
    }
}