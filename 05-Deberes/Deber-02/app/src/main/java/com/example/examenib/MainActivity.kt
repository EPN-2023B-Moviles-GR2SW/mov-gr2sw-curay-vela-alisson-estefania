package com.example.examenib

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    lateinit var adaptador: ArrayAdapter<Universidad>
    lateinit var universidadDBHandler: UniversidadDBHandler


    companion object {
        const val CODIGO_CREAR_UNIVERSIDAD = 1
        const val CODIGO_EDITAR_UNIVERSIDAD = 2
        lateinit var array: ArrayList<Universidad>
        var posicionElementoSeleccionado = 0
    }

    // Callback para la actividad de modificación de universidad
    val callbackContenidoIntentExplicito =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data
                    //Se actualizan los campos de la universidad seleccionada en el arreglo
                    // array con los valores proporcionados por la actividad.
                    // Estos valores son extraídos del Intent
                    array[posicionElementoSeleccionado].nombre = data?.
                    getStringExtra("nombreModificado").toString()
                    array[posicionElementoSeleccionado].ubicacion = data?.
                    getStringExtra("ubicacionModificada").toString()
                    array[posicionElementoSeleccionado].fechaFundacion = data?.
                    getStringExtra("fechaFundacionModificada").toString()
                    array[posicionElementoSeleccionado].areaCobertura = data?.
                    getDoubleExtra("areaCoberturaModificada",0.0).
                    toString().toDouble()
                    adaptador.notifyDataSetChanged()
                    mostrarSnackbar("Universidad modificada exitosamente")
                }
            }
        }
    val callbackCrearUniversidad = //creación de universidad
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    // Se obtienen los datos enviados desde la actividad creada
                    val data = result.data
                    // Se extraen los datos necesarios para crear una nueva universidad
                    val nuevaUniversidad = Universidad(
                        0,
                        data?.
                        getStringExtra("nombreUniversidad").toString(),
                        data?.
                        getStringExtra("ubicacionUniversidad").toString(),
                        data?.
                        getStringExtra("fechaFundacionUniversidad").toString(),
                        data?.
                        getDoubleExtra("areaCoberturaUniversidad",0.0).
                        toString().toDouble(),
                        arrayListOf()
                    )
                    //creacion de la nueva universidad
                    array.add(nuevaUniversidad)
                    adaptador.notifyDataSetChanged() // // notificar al adaptador que los datos han cambiado
                }
            }
        }
    val callbackEstudiantes =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    // obtener datos de la actividad estudiantes
                    val data = result.data
                    adaptador.notifyDataSetChanged()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar el handler de la base de datos
        universidadDBHandler = UniversidadDBHandler(this)

        // Obtener la lista de universidades desde la base de datos
        array = universidadDBHandler.obtenerUniversidades()

        // Configuración del adaptador y la lista
        val listView = findViewById<ListView>(R.id.listv_universidad)

        //  adaptador ArrayAdapter para mostrar la lista de universidades en el ListView
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            array
        )

        // asigna el adaptador al ListView
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        // configuracion del boton para crear nueva universidad
        val crearUniversidad = findViewById<Button>(R.id.btn_crear_universidad)
        crearUniversidad
            .setOnClickListener {
                agregarUniversidad(adaptador)
                mostrarSnackbar("Universidad creada exitosamente")
            }
        // Configuración de la creación del menú contextual y eventos de clic largo
        registerForContextMenu(listView)
    }

    // Método para agregar una nueva universidad
    fun agregarUniversidad(adaptador: ArrayAdapter<Universidad>) {
        val intent = Intent(this, CrearUniversidad::class.java)
        startActivityForResult(intent, CODIGO_CREAR_UNIVERSIDAD)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)

        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        // Obtener información sobre el elemento seleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        // Almacenar la posición del elemento seleccionado
        posicionElementoSeleccionado = posicion
    }

    // Método que se ejecuta al seleccionar un elemento en el menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editar_universidad -> {
                ActividadConParametros(
                    EditarUniversidad::class.java
                )
                return true
            }
            R.id.eliminar_universidad -> {
                EliminacionDialogo()
                return true
            }
            R.id.ver_estudiantes -> {
                ActividadEstudiantes(
                    ListEstudiantes::class.java
                )
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun EliminacionDialogo() { // diálogo para confirmar eliminación
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirme la eliminación")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                // Obtén la universidad seleccionada
                val universidadSeleccionada = array[posicionElementoSeleccionado]

                // Llama al método eliminarUniversidad en el UniversidadDBHandler
                universidadDBHandler.eliminarUniversidad(universidadSeleccionada.id)

                // Elimina la universidad de la lista local
                array.removeAt(posicionElementoSeleccionado)

                // Notifica al adaptador que los datos han cambiado
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

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.listv_universidad),
            texto, Snackbar.LENGTH_LONG
        )
        snack.show()
    }

    fun ActividadConParametros(clase: Class<*>) {
        val intentExplicito = Intent(this, clase)
        intentExplicito.putExtra("nombreUniversidad", array[posicionElementoSeleccionado].nombre)
        intentExplicito.putExtra("ubicacionUniversidad", array[posicionElementoSeleccionado].ubicacion)
        intentExplicito.putExtra("fechaFundacionUniversidad", array[posicionElementoSeleccionado].fechaFundacion)
        intentExplicito.putExtra("areaCoberturaUniversidad", array[posicionElementoSeleccionado].areaCobertura)
        startActivityForResult(intentExplicito, CODIGO_EDITAR_UNIVERSIDAD)
    }

    //crea un intent para iniciar una actividad de la clase proporcionada, sin parámetros
    fun ActividadSinParametros(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)

        callbackCrearUniversidad.launch(intent)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CODIGO_CREAR_UNIVERSIDAD -> {
                if (resultCode == RESULT_OK) {
                    // Obtener los datos del intent devuelto por la actividad
                    val nombre = data?.getStringExtra("nombreUniversidad") ?: ""
                    val ubicacion = data?.getStringExtra("ubicacionUniversidad") ?: ""
                    val fechaFundacion = data?.getStringExtra("fechaFundacionUniversidad") ?: ""
                    val areaCobertura = data?.getDoubleExtra("areaCoberturaUniversidad", 0.0) ?: 0.0

                    // Crear nueva universidad y agregarla al array
                    val nuevaUniversidad = Universidad(0, nombre, ubicacion, fechaFundacion, areaCobertura, arrayListOf())
                    array.add(nuevaUniversidad)

                    // Notificar al adaptador que los datos han cambiado
                    adaptador.notifyDataSetChanged()
                    mostrarSnackbar("Universidad creada exitosamente")
                }
            }
            CODIGO_EDITAR_UNIVERSIDAD -> {
                if (resultCode == RESULT_OK) {
                    // Obtener los datos del intent devuelto por la actividad
                    val nombre = data?.getStringExtra("nombreModificado") ?: ""
                    val ubicacion = data?.getStringExtra("ubicacionModificada") ?: ""
                    val fechaFundacion = data?.getStringExtra("fechaFundacionModificada") ?: ""
                    val areaCobertura = data?.getDoubleExtra("areaCoberturaModificada", 0.0) ?: 0.0

                    // Actualizar los campos de la universidad seleccionada en el array
                    array[posicionElementoSeleccionado].nombre = nombre
                    array[posicionElementoSeleccionado].ubicacion = ubicacion
                    array[posicionElementoSeleccionado].fechaFundacion = fechaFundacion
                    array[posicionElementoSeleccionado].areaCobertura = areaCobertura

                    // Notificar al adaptador que los datos han cambiado
                    adaptador.notifyDataSetChanged()
                    mostrarSnackbar("Universidad modificada exitosamente")
                }
            }
        }
    }

    // iniciar actividad
    fun ActividadEstudiantes(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)

        callbackEstudiantes.launch(intent)
    }

}
