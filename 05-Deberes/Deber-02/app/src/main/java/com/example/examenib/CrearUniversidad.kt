package com.example.examenib

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.examenib.Database.UniversidadDBHandler
import com.google.android.material.snackbar.Snackbar

class CrearUniversidad : AppCompatActivity() {

    private lateinit var universidadDBHandler: UniversidadDBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_universidad)

        universidadDBHandler = UniversidadDBHandler(this)


        val btnAceptar = findViewById<Button>(R.id.btn_aceptar_crear_universidad)
        btnAceptar.setOnClickListener {

            // Obtener referencias a los campos de entrada
            val leerNombre = findViewById<EditText>(
                R.id.ingresar_nombre_universidad)
            val leerUbicacion = findViewById<EditText>(
                R.id.ingresar_ubicacion_universidad)
            val leerFechaFundacion = findViewById<EditText>(
                R.id.ingresar_fecha_fundacion_universidad)
            val leerAreaCobertura = findViewById<EditText>(
                R.id.ingresar_extension_campus_universidad)

            // Validar y obtener los datos ingresados
            val conNombre = if (leerNombre.text.isNotEmpty()) {
                leerNombre.text.toString()
            } else {
                mostrarSnackbar("Los campos deben estar llenos")
                null
            }
            val conUbicacion = if (leerUbicacion.text.isNotEmpty()) {
                leerUbicacion.text.toString()
            } else {
                mostrarSnackbar("Los campos deben estar llenos")
                null
            }
            val confechaFundacion = if (leerFechaFundacion.text.isNotEmpty()) {
                leerFechaFundacion.text.toString()
            } else {
                mostrarSnackbar("Los campos deben estar llenos")
                null
            }

            val conAreaCobertura = if (leerAreaCobertura.text.isNotEmpty()) {
                leerAreaCobertura.text.toString().toDouble()
            } else {
                mostrarSnackbar("Los campos deben estar llenos")
                null
            }

            if (conNombre != null && conUbicacion != null && confechaFundacion
                != null && conAreaCobertura != null
            ) {
                agregarUniversidadDB(conNombre, conUbicacion, confechaFundacion, conAreaCobertura)
            }
        }
        val btnCancelar = findViewById<Button>(R.id.btn_cancelar_crear_universidad)
        btnCancelar.setOnClickListener {
            finish()
        }

    }

    private fun agregarUniversidadDB(
        nombre: String,
        ubicacion: String,
        fechaFundacion: String,
        areaCobertura: Double
    ) {
        // Crear una instancia de Universidad y agregarla a la base de datos
        val nuevaUniversidad = Universidad(
            nombre = nombre,
            ubicacion = ubicacion,
            fechaFundacion = fechaFundacion,
            areaCobertura = areaCobertura
        )

        // Obtener el ID de la universidad después de agregarla a la base de datos
        val idUniversidad = universidadDBHandler.agregarUniversidad(nuevaUniversidad)

        // Devolver resultados mediante Intent
        val intentRecuperarParametros = Intent()
        intentRecuperarParametros.putExtra("idUniversidad", idUniversidad)
        intentRecuperarParametros.putExtra("nombreUniversidad", nombre)
        intentRecuperarParametros.putExtra("ubicacionUniversidad", ubicacion)
        intentRecuperarParametros.putExtra("fechaFundacionUniversidad", fechaFundacion)
        intentRecuperarParametros.putExtra("areaCoberturaUniversidad", areaCobertura)
        setResult(RESULT_OK, intentRecuperarParametros)

        finish()
    }

    fun obtenerInfo( //devuelve datos de la universidad creada
        nombre: String,
        ubicacion: String,
        fechaFundacion: String,
        areaCobertura: Double
    ) {
        // recibir respuesta
        val intentRecuperarParametros = Intent()

        //  devuelve respuesta mediante Intent
        intentRecuperarParametros.putExtra(
            "nombreUniversidad", nombre)
        intentRecuperarParametros.putExtra(
            "ubicacionUniversidad", ubicacion)
        intentRecuperarParametros.putExtra(
            "fechaFundacionUniversidad", fechaFundacion)
        intentRecuperarParametros.putExtra(
            "areaCoberturaUniversidad", areaCobertura)
        setResult(
            RESULT_OK,
            intentRecuperarParametros
        )
        finish()
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cons_crear_universidad),
            texto,
            Snackbar.LENGTH_LONG
        )
        snack.show()
    }

}