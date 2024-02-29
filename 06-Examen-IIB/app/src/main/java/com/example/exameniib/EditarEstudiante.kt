package com.example.exameniib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView

class EditarEstudiante : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_estudiante)

        // muestra el nombre del estudiante seleccionado en un TextView en la actividad de edición de estudiante
        val estudianteSeleccionado =findViewById<TextView>(R.id.titulo_editar_estudiante)
        estudianteSeleccionado.setText(ListaEstudiantes.arrayEstudiantes[ListaEstudiantes.posicionElementoEstudiantes].nombre)

        // tomar datos del estudiante actual a través del intent
        val nombre = intent.getStringExtra("nombre")
        val edad = intent.getIntExtra("edad",0)
        val fechaIngreso = intent.getStringExtra("fechaIngreso")
        val estado = intent.getBooleanExtra("estado", true)
        val promedioCalificaciones = intent.getDoubleExtra("promedioCalificaciones", 0.0)

        // reemplaza los campos de edición con los datos actuales del estudiante
        val inNombre = findViewById<EditText>(
            R.id.editar_nombre_estudiante)
        inNombre.hint = nombre
        val inEdad = findViewById<EditText>(
            R.id.editar_edad_estudiante)
        inEdad.hint = edad.toString()
        val infechaIngreso = findViewById<EditText>(
            R.id.editar_fecha_ingreso_estudiante)
        infechaIngreso.hint = fechaIngreso

        val checkBoxEstado = findViewById<CheckBox>(
            R.id.editar_estado_estudiante)
        checkBoxEstado.isChecked = estado

        val inpromedioCalificaiones = findViewById<EditText>(
            R.id.editar_promedio_estudiante)
        inpromedioCalificaiones.hint = promedioCalificaciones.toString()

        val btnAceptar = findViewById<Button>(
            R.id.btn_aceptar_editar_estudiante)

        btnAceptar.setOnClickListener {

            // tomar los valores modificados de los campos de edición
            val nombreMod = if (inNombre.text.isNotEmpty()) {
                inNombre.text.toString()
            } else {
                nombre
            }
            val edadMod = if ( inEdad.text.isNotEmpty()) {
                inEdad.text.toString().toInt()
            } else {
                edad
            }
            val fechaIngresoMod = if (infechaIngreso.text.isNotEmpty()) {
                infechaIngreso.text.toString()
            } else {
                fechaIngreso
            }
            val promedioCalificacionesMod = if (inpromedioCalificaiones.text.isNotEmpty()) {
                inpromedioCalificaiones.text.toString().toDouble()
            } else {
                promedioCalificaciones
            }

            val estadooMod = checkBoxEstado.isChecked

            if (nombreMod != null && edadMod != null && fechaIngresoMod != null
                && estadooMod != null && promedioCalificacionesMod != null
            ) {
                obtenerInfo(
                    nombreMod,
                    edadMod,
                    fechaIngresoMod,
                    estadooMod,
                    promedioCalificacionesMod
                )
            }

        }
        val btnCancelar = findViewById<Button>(R.id.btn_cancelar_editar_estudiante)
        btnCancelar.setOnClickListener {
            setResult(
                RESULT_OK,
                null
            )
            finish()
        }
    }
    fun obtenerInfo( // devuelvo datos editados
        nombre: String,
        edad: Int,
        fechaIngreso: String,
        estado: Boolean,
        promedioCalificaciones: Double
    ) {
        // crea un nuevo Intent para almacenar los datos editados
        val intentRecuperarParametros = Intent()

        // agrega los datos editados al Intent
        intentRecuperarParametros.putExtra(
            "nombreEstudiante", nombre)
        intentRecuperarParametros.putExtra(
            "edadEstudiante", edad)
        intentRecuperarParametros.putExtra(
            "fechaIngresoEstudiante", fechaIngreso)
        intentRecuperarParametros.putExtra(
            "estadoEstudiante", estado)
        intentRecuperarParametros.putExtra(
            "promedioCalificacionesEstudiante", promedioCalificaciones)
        setResult(
            RESULT_OK,
            intentRecuperarParametros
        )
        // termina la actividad actual y devuelve el resultado a la actividad que la llamó
        finish()
    }
}
