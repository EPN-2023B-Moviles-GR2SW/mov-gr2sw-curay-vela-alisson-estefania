package com.example.examenib

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.google.android.material.snackbar.Snackbar
import android.content.Intent

class CrearEstudiante : AppCompatActivity() {

    private lateinit var estudianteDBHandler: EstudianteDBHandler
    private var idUniversidad: Long = -1  // Asegúrate de establecer el valor correcto

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_estudiante)

        estudianteDBHandler = EstudianteDBHandler(this)

        // Obtener el idUniversidad de la actividad anterior (ajusta según tu lógica)
        idUniversidad = intent.getLongExtra("idUniversidad", -1)

        val btnAceptar = findViewById<Button>(R.id.btn_aceptar_crear_estudiante)
        btnAceptar.setOnClickListener {
            // Obtener referencias a los campos de entrada
            val leerNombre = findViewById<EditText>(
                R.id.crear_nombre_estudiante)
            val leerEdad = findViewById<EditText>(
                R.id.crear_edad_estudiante)
            val leerFechaIngreso = findViewById<EditText>(
                R.id.crear_fecha_ingreso_estudiante)
            val leerPromedioCalificaciones = findViewById<EditText>(
                R.id.crear_promedio_estudiante)
            val leerEstadoEstudiante = findViewById<CheckBox>(
                R.id.crear_estado_estudiante)

            // Validar y obtener los datos ingresados
            val conNombre = if (leerNombre.text.isNotEmpty()) {
                leerNombre.text.toString()
            } else {
                mostrarSnackbar("Los campos deben estar llenos")
                null
            }
            val conEdad = if (leerEdad.text.isNotEmpty()) {
                leerEdad.text.toString().toInt()
            } else {
                mostrarSnackbar("Los campos deben estar llenos")
                null
            }
            val confechaIngreso = if (leerFechaIngreso.text.isNotEmpty()) {
                leerFechaIngreso.text.toString()
            } else {
                mostrarSnackbar("Los campos deben estar llenos")
                null
            }
            val conPromedioCalificaciones = if (leerPromedioCalificaciones.text.isNotEmpty()) {
                leerPromedioCalificaciones.text.toString().toDouble()
            } else {
                mostrarSnackbar("Los campos deben estar llenos")
                null
            }
            val conEstado = leerEstadoEstudiante.isChecked

            // Si todos los datos son válidos, llamo a la función obtenerInfo
            if (conNombre != null && conEdad != null && confechaIngreso
                != null && conEstado != null && conPromedioCalificaciones != null
            ) {
                agregarEstudianteDB(
                    conNombre,
                    conEdad,
                    confechaIngreso,
                    conEstado,
                    conPromedioCalificaciones
                )
            }

        }
        val btnCancelar = findViewById<Button>(R.id.btn_cancelar_crear_estudiante)
        btnCancelar.setOnClickListener {
            finish()
        }

    }

    private fun agregarEstudianteDB(
        nombre: String,
        edad: Int,
        fechaIngreso: String,
        estado: Boolean,
        promedioCalificaciones: Double
    ) {
        // Crear una instancia de Estudiante y agregarla a la base de datos
        val nuevoEstudiante = Estudiante(
            nombre = nombre,
            edad = edad,
            fechaIngreso = fechaIngreso,
            estado = estado,
            promedioCalificaciones = promedioCalificaciones
        )

        // Agregar el estudiante a la base de datos y obtener su ID
        val idEstudiante = estudianteDBHandler.agregarEstudiante(nuevoEstudiante, idUniversidad)

        // Obtener el estudiante recién agregado de la base de datos usando su ID
        val estudianteRecuperado = estudianteDBHandler.obtenerEstudiantePorId(idEstudiante)

        // Actualizar el estudiante con el ID de la universidad
        estudianteDBHandler.actualizarEstudiante(estudianteRecuperado)

        // Devolver resultados mediante Intent
        val intentRecuperarParametros = Intent()
        intentRecuperarParametros.putExtra("idEstudiante", idEstudiante)
        intentRecuperarParametros.putExtra("nombreEstudiante", nombre)
        intentRecuperarParametros.putExtra("edadEstudiante", edad)
        intentRecuperarParametros.putExtra("fechaIngresoEstudiante", fechaIngreso)
        intentRecuperarParametros.putExtra("estadoEstudiante", estado)
        intentRecuperarParametros.putExtra("promedioCalificacionesEstudiante", promedioCalificaciones)
        setResult(RESULT_OK, intentRecuperarParametros)

        finish()
    }


    fun obtenerInfo( // devuelve datos del estudiante creado
        nombre: String,
        edad: Int,
        fechaIngreso: String,
        estado: Boolean,
        promedioCalificaciones: Double
    ) {
        // devolver respuesta mediante Intent
        val intentRecuperarParametros = Intent()
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
        finish()
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cons_crear_estudiante),
            texto,
            Snackbar.LENGTH_LONG
        )
        snack.show()
    }

}