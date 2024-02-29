package com.example.exameniib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class EditarUniversidad : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_universidad)

        // datos de la universidad a editar desde el intent
        val nombre = intent.getStringExtra(
            "nombreUniversidad")
        val ubicacion = intent.getStringExtra(
            "ubicacionUniversidad")
        val fechaFundacion = intent.getStringExtra(
            "fechaFundacionUniversidad")
        val areaCobertura = intent.getDoubleExtra(
            "areaCoberturaUniversidad", 0.0)

        // reemplaza los campos de edición con los datos actuales de la universidad
        val leerNombre = findViewById<EditText>(
            R.id.editar_nombre_universidad)
        leerNombre.hint = nombre
        val leerUbicacion = findViewById<EditText>(
            R.id.editar_ubicacion_universidad)
        leerUbicacion.hint = ubicacion
        val leerfechaFundacion = findViewById<EditText>(
            R.id.editar_fecha_fundacion_universidad)
        leerfechaFundacion.hint = fechaFundacion
        val leerAreaCobertura = findViewById<EditText>(
            R.id.editar_extension_campus_universidad)
        leerAreaCobertura.hint = areaCobertura.toString()


        val botonAceptar = findViewById<Button>(R.id.btn_aceptar_editar_universidad)
        botonAceptar.setOnClickListener {

            // tomar los valores modificados de los campos de edición
            val nombreModificado = if (leerNombre.text.isNotEmpty()) {
                leerNombre.text.toString()
            } else {
                nombre
            }
            val ubicacionModificada = if (leerUbicacion.text.isNotEmpty()) {
                leerUbicacion.text.toString()
            } else {
                ubicacion
            }
            val fechaFundacionModificada = if (leerfechaFundacion.text.isNotEmpty()) {
                leerfechaFundacion.text.toString()
            } else {
                fechaFundacion
            }
            val areaCoberturadificada = if (leerAreaCobertura.text.isNotEmpty()) {
                leerAreaCobertura.text.toString().toDouble()
            } else {
                areaCobertura
            }

            if (nombreModificado != null && ubicacionModificada != null
                && fechaFundacionModificada != null && areaCoberturadificada != null
            ) {
                obtenerInfo(
                    nombreModificado,
                    ubicacionModificada,
                    fechaFundacionModificada,
                    areaCoberturadificada
                )
            }
        }
        val botonCancelar = findViewById<Button>(R.id.btn_cancelar_editar_universidad)
        botonCancelar.setOnClickListener {
            setResult(
                RESULT_OK,
                null
            )
            finish()
        }

    }
    fun obtenerInfo( // devuelvo datos editados
        nombre: String,
        ubicacion: String,
        fechaFundacion: String,
        areaCobertura: Double
    ) {

        // crea un nuevo Intent para almacenar los datos editados
        val intentDevolverParametros = Intent()

        // agrega los datos editados al Intent
        intentDevolverParametros.putExtra(
            "nombreModificado", nombre)
        intentDevolverParametros.putExtra(
            "ubicacionModificada", ubicacion)
        intentDevolverParametros.putExtra(
            "fechaFundacionModificada", fechaFundacion)
        intentDevolverParametros.putExtra(
            "areaCoberturaModificada",  areaCobertura)
        setResult(
            RESULT_OK,
            intentDevolverParametros
        )
        finish()
    }
}