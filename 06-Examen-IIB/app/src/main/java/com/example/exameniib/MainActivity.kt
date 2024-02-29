package com.example.exameniib

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
import com.example.exameniib.database.FStoreUniversidad
import com.example.exameniib.modelo.Universidad
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {

    private val firestoreUniversidad = FStoreUniversidad()
    lateinit var adaptador: ArrayAdapter<Universidad>

    // Callback para la actividad de modificación de universidad
    val callbackCrearUniversidad =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data
                    val nuevaUniversidad = Universidad (
                        data?.getStringExtra("nombreUniversidad").toString(),
                        data?.getStringExtra("ubicacionUniversidad").toString(),
                        data?.getStringExtra("fechaFundacionUniversidad").toString(),
                        data?.getDoubleExtra("areaCoberturaUniversidad",0.0).toString().toDouble(),
                        arrayListOf()
                    )
                    firestoreUniversidad.crearUniversidad(nuevaUniversidad)
                    arrayUniversidad.add(nuevaUniversidad)
                    adaptador.notifyDataSetChanged()
                    mostrarSnackbar("Universidad modificada exitosamente")
                }
            }
        }
    val callbackContenidoIntentEditarUniversidad = //editar universidad
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    // Se obtienen los datos enviados desde la actividad creada
                    val data = result.data
                    // Se extraen los datos necesarios para crear una nueva universidad

                    val nombre = data?.getStringExtra("nombreUniversidad").toString()
                    val ubicacion = data?.getStringExtra("ubicacionUniversidad").toString()
                    val fechaFundacion= data?.getStringExtra("fechaFundacionUniversidad").toString()
                    val areaCobertura = data?.getDoubleExtra("areaCoberturaUniversidad",0.0).toString().toDouble()

                    firestoreUniversidad.editarUniversidad(
                        Universidad(
                            nombre,
                            ubicacion,
                            fechaFundacion,
                            areaCobertura,
                            arrayListOf()
                        )
                    )
                    arrayUniversidad[posicionUniversidad].nombre = data?.
                    getStringExtra("nombreModificado").toString()
                    arrayUniversidad[posicionUniversidad].ubicacion = data?.
                    getStringExtra("ubicacionModificada").toString()
                    arrayUniversidad[posicionUniversidad].fechaFundacion = data?.
                    getStringExtra("fechaFundacionModificada").toString()
                    arrayUniversidad[posicionUniversidad].areaCobertura = data?.
                    getDoubleExtra("areaCoberturaModificada",0.0).toString().toDouble()
                    adaptador.notifyDataSetChanged()
                    mostrarSnackbar("Universidad modificada exitosamente")
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

        //  adaptador ArrayAdapter para mostrar la lista de universidades en el ListView
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arrayUniversidad
        )
        // Configuración del adaptador y la lista
        val listView = findViewById<ListView>(R.id.listv_universidad)

        // asigna el adaptador al ListView
        listView.adapter = adaptador
        val db = Firebase.firestore
        val universidadesExistentes = db.collection("Universidades")
        limpiarArreglo()
        adaptador.notifyDataSetChanged()

        universidadesExistentes.get()
            .addOnSuccessListener {result ->
                for (document in result) {
                    anadirAArregloUniversidad(document)
                    adaptador.notifyDataSetChanged()
                }

            }
            .addOnFailureListener {
                // Errores
            }
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
    fun agregarUniversidad(
        adaptador: ArrayAdapter<Universidad>
    ) {
        // Inicia la actividad de creación de universidad sin parámetros
        abrirActividadCrearUniversidad(
            CrearUniversidad::class.java
        )
        adaptador.notifyDataSetChanged()
    }

    fun anadirAArregloUniversidad(
        universidad: QueryDocumentSnapshot
    ){

        val id = universidad.id


        val nuevaUniversidad = Universidad(
            id,
            universidad.data["nombreUniversidad"] as String,
            universidad.data["ubicacionUniversidad"] as String,
            universidad.data["fechaFundacionUniversidad"] as String,
            universidad.data["areaCoberturUniversidada"] as Double,
            arrayListOf()
        )

        arrayUniversidad.add(nuevaUniversidad)
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
        posicionUniversidad = posicion
    }

    // Método que se ejecuta al seleccionar un elemento en el menú contextual
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.editar_universidad -> {
                AbrirActividadEditarUniversidad(
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
                    ListaEstudiantes::class.java
                )
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun EliminacionDialogo() { // diálogo para confirmar eliminacion
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirme la eliminación")
        builder.setPositiveButton(
            "Aceptar",
            DialogInterface.OnClickListener { dialog, which ->
                firestoreUniversidad.eliminarUniversidad()
                mostrarSnackbar("Universidad eliminada exitosamente")
                adaptador.notifyDataSetChanged()
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
    fun limpiarArreglo() {
        arrayUniversidad.clear()
    }

    fun abrirActividadCrearUniversidad(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)

        callbackCrearUniversidad.launch(intent)
    }

    fun AbrirActividadEditarUniversidad(
        clase: Class<*>
    ) {
        val intentExplicito = Intent(this, clase)
        val universidadSeleccionada = arrayUniversidad[posicionUniversidad]
        //agregar parámetros extra al intent
        intentExplicito.putExtra("nombreUniversidad", universidadSeleccionada.nombre)
        intentExplicito.putExtra("ubicacionUniversidad", universidadSeleccionada.ubicacion)
        intentExplicito.putExtra("fechaFundacionUniversidad", universidadSeleccionada.fechaFundacion)
        intentExplicito.putExtra("areaCoberturaUniversidad", universidadSeleccionada.areaCobertura)

        //  para lanzar la actividad con el intent y esperar un resultado.
        callbackContenidoIntentEditarUniversidad.launch(intentExplicito)
    }

    // iniciar actividad estudiantes
    fun ActividadEstudiantes(
        clase: Class<*>
    ) {
        val intent = Intent(this, clase)

        callbackEstudiantes.launch(intent)
    }
    companion object {
        val arrayUniversidad = arrayListOf<Universidad>()
        var posicionUniversidad = 0
    }
}
