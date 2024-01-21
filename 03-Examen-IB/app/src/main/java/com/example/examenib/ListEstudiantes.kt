package com.example.examenib

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

class Estudiantes : AppCompatActivity() {

    lateinit var adaptador: ArrayAdapter<BDDEstudiante>

    companion object {
        var array = arrayListOf<BDDEstudiante>()
        var posicionElementoEstudiantes = 0
    }

    val callbackCrearEstudiante =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data
                    val nuevoEstudiante = BDDEstudiante(
                        data?.getStringExtra(
                            "nombreEstudiante").toString(),
                        data?.getIntExtra(
                            "edadEstudiante",0).toString().toInt(),
                        data?.getStringExtra(
                            "fechaIngresoEstudiante").toString(),
                        data?.getBooleanExtra(
                            "estadoEstudiante", true).toString().toBoolean(),
                        data?.getDoubleExtra(
                            "promedioCalificacionesEstudiante",0.0).toString().toDouble()
                    )
                    MainActivity.array[MainActivity.posicionElementoSeleccionado].listaEstudiantes.add(
                        nuevoEstudiante
                    )
                    adaptador.notifyDataSetChanged()
                }
            }
        }
    val callbackContenidoIntentExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data
                    //Lógica para modificar los datos del arreglo
                    array[posicionElementoEstudiantes].nombre =
                        data?.getStringExtra("nombreEstudiante").toString()
                    array[posicionElementoEstudiantes].edad =
                        data?.getIntExtra("edadEstudiante",0).toString().toInt()
                    array[posicionElementoEstudiantes].fechaIngreso =
                        data?.getStringExtra("fechaIngresoEstudiante").toString()
                    array[posicionElementoEstudiantes].estado =
                        data?.getBooleanExtra("estadoEstudiante", true).toString().toBoolean()
                    array[posicionElementoEstudiantes].promedioCalificaciones =
                        data?.getDoubleExtra("promedioCalificacionesEstudiante",0.0).toString().toDouble()

                    MainActivity.array[MainActivity.posicionElementoSeleccionado]
                        .listaEstudiantes[posicionElementoEstudiantes] =
                        array[posicionElementoEstudiantes]
                    adaptador.notifyDataSetChanged()
                    mostrarSnackbar("Estudiante modificado exitosamente")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gestionar_estudiantes)

        array = MainActivity.array[MainActivity.posicionElementoSeleccionado].listaEstudiantes

        val listView = findViewById<ListView>(R.id.listv_estudiantes)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            array
        )

        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()


        val estudianteSeleccionado = findViewById<TextView>(R.id.txt_universidadEstudiante)
        estudianteSeleccionado.setText(MainActivity.array[MainActivity.posicionElementoSeleccionado].nombre)

        val btnCrearEstudiante = findViewById<Button>(R.id.btn_crear_estudiante)
        btnCrearEstudiante
            .setOnClickListener {
                agregarEstudiante()
            }
        val btnRegresar = findViewById<Button>(R.id.btn_volver_estudiante)
        btnRegresar
            .setOnClickListener {
                finish()
            }
        registerForContextMenu(listView)

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_estudiantes, menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionElementoEstudiantes = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editar_estudiante -> {
                ActividadEditarEstudiante(
                    EditarEstudiante::class.java
                )
                return true
            }
            R.id.eliminar_estudiante -> {
                EliminacionDialogo()
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun agregarEstudiante() {
        ActividadCrearEstudiante(
            CrearEstudiante::class.java
        )
        adaptador.notifyDataSetChanged()
    }
    fun EliminacionDialogo() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Desea eliminar")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                MainActivity.array[MainActivity.posicionElementoSeleccionado]
                    .listaEstudiantes.removeAt(posicionElementoEstudiantes)
                adaptador.notifyDataSetChanged()
                mostrarSnackbar("Universidad eliminada exitosamente")
            }
        )
        builder.setNegativeButton(
            "Cancelar",
            null
        )
        val dialogo = builder.create()
        dialogo.show()
    }

    fun obtenerInfo(
        nombre: String,
        edad: Int,
        fechaIngreso: String,
        estado: Boolean,
        promedioCalificaciones: Double
    ) {
        //recibir info
        val intentRecuperarParametros = Intent()
        intentRecuperarParametros
            .putExtra("nombreEstudiante", nombre)
        intentRecuperarParametros
            .putExtra("edadEstudiante", edad)
        intentRecuperarParametros
            .putExtra("fechaIngresoEstudiante", fechaIngreso)
        intentRecuperarParametros
            .putExtra("estadoEstudiante", estado)
        intentRecuperarParametros
            .putExtra("promedioCalificacionesEstudiante", promedioCalificaciones)
        setResult(
            RESULT_OK,
            intentRecuperarParametros
        )
        finish()
    }

    fun ActividadCrearEstudiante(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        callbackCrearEstudiante.launch(intent)
    }

    fun ActividadEditarEstudiante(
        clase: Class<*>
    ) {
        val intentExplicito = Intent(this, clase)
        //Enviar parámetros (solamente variables primitivas)
        intentExplicito.putExtra(
            "nombre", array[posicionElementoEstudiantes].nombre)
        intentExplicito.putExtra(
            "edad", array[posicionElementoEstudiantes].edad)
        intentExplicito.putExtra(
            "fechaIngreso", array[posicionElementoEstudiantes].fechaIngreso)
        intentExplicito.putExtra(
            "estado", array[posicionElementoEstudiantes].estado)
        intentExplicito.putExtra(
            "promedioCalificaiones", array[posicionElementoEstudiantes].promedioCalificaciones)

        callbackContenidoIntentExplicito.launch(intentExplicito)
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cons_estudiantes),
            texto,
            Snackbar.LENGTH_LONG
        )
        snack.show()
    }
}