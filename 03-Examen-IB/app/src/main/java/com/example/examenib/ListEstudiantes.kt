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

class ListEstudiantes : AppCompatActivity() {

    lateinit var adaptador: ArrayAdapter<Estudiante>

        companion object { // para tener propiedades estáticas compartidas entre instancias de la clase
        var array = arrayListOf<Estudiante>()
        var posicionElementoEstudiantes = 0
    }

    val callbackCrearEstudiante =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {

                    // Obtener datos del intent
                    val data = result.data
                    // nuevo estudiante utilizando los datos obtenidos
                    val nuevoEstudiante = Estudiante(
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
                    // agrega el nuevo estudiante a la lista de estudiantes de la universidad seleccionada
                    MainActivity.array[MainActivity.posicionElementoSeleccionado].listaEstudiantes.add(
                        nuevoEstudiante
                    )
                    adaptador.notifyDataSetChanged() // notificar nuevos cambios
                }
            }
        }
    val callbackContenidoIntentExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    // Acciones a realizar si la actividad se completó exitosamente
                    val data = result.data
                    //actualizar los atributos del estudiante en el
                    // array con los valores devueltos por la actividad editar
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

                    // actualiza la lista de estudiantes en la universidad seleccionada
                    MainActivity.array[MainActivity.posicionElementoSeleccionado]
                        .listaEstudiantes[posicionElementoEstudiantes] =
                        array[posicionElementoEstudiantes]

                    // Actualización del adaptador y notificación de cambios
                    adaptador.notifyDataSetChanged()

                    // muestra mensaje de exito
                    mostrarSnackbar("Estudiante modificado exitosamente")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estudiantes)

        //asigna a array el arreglo de estudiantes correspondiente
        // a la posición del elemento seleccionado en la actividad principal (MainActivity).
        array = MainActivity.array[MainActivity.posicionElementoSeleccionado].listaEstudiantes

        // crear un adaptador ArrayAdapter que utiliza el diseño predefinido
        // android.R.layout.simple_list_item_1 para mostrar los elementos del array en la ListView.

        val listView = findViewById<ListView>(R.id.listv_estudiantes)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            array
        )
        //establece el adaptador en la ListView
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged() // notificar cambios

        //establece el texto del TextView con el nombre de la universidad
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
        registerForContextMenu(listView) // registra la ListView para que pueda mostrar un menú contextual

    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu_estudiantes, menu)
        //  información sobre el elemento seleccionado en el menú contextual
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        // Almacenar la posición del elemento seleccionado en la propiedad estática
        // posicionElementoEstudiantes de la clase actual
        posicionElementoEstudiantes = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editar_estudiante -> { // llama a la funcion
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

    fun agregarEstudiante() { // inicia la actividad para crear un nuevo estudiante
        ActividadCrearEstudiante(
            CrearEstudiante::class.java
        )
        adaptador.notifyDataSetChanged()
    }
    fun EliminacionDialogo() { // diálogo para confirmación de eliminación
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

    fun ActividadCrearEstudiante(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)
        callbackCrearEstudiante.launch(intent)
    }

    fun ActividadEditarEstudiante(
        clase: Class<*>
    ) {
        // Crear un Intent explícito para iniciar la actividad de edición
        val intentExplicito = Intent(this, clase)
        // Enviar parámetros a la actividad de edición
        intentExplicito.putExtra(
            "nombre", array[posicionElementoEstudiantes].nombre)
        intentExplicito.putExtra(
            "edad", array[posicionElementoEstudiantes].edad)
        intentExplicito.putExtra(
            "fechaIngreso", array[posicionElementoEstudiantes].fechaIngreso)
        intentExplicito.putExtra(
            "estado", array[posicionElementoEstudiantes].estado)
        intentExplicito.putExtra(
            "promedioCalificaciones", array[posicionElementoEstudiantes].promedioCalificaciones)

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