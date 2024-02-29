package com.example.exameniib

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
import com.example.exameniib.database.FStoreEstuidante
import com.example.exameniib.modelo.Estudiante
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.firestore

class ListaEstudiantes : AppCompatActivity() {
    private val firestoreEstudiante = FStoreEstuidante()
    lateinit var adaptador: ArrayAdapter<Estudiante>


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
                            "edadEstudiante",0).toString().toLong(),
                        data?.getStringExtra(
                            "fechaIngresoEstudiante").toString(),
                        data?.getBooleanExtra(
                            "estadoEstudiante", true).toString().toBoolean(),
                        data?.getDoubleExtra(
                            "promedioCalificacionesEstudiante",0.0).toString().toDouble()
                    )

                    // agrega el nuevo estudiante a la lista de estudiantes de la universidad seleccionada
                    firestoreEstudiante.crearEstudiante(nuevoEstudiante)
                    MainActivity.arrayUniversidad[MainActivity.posicionUniversidad].listaEstudiantes.add(
                        nuevoEstudiante
                    )
                    arrayEstudiantes.add(nuevoEstudiante)
                    adaptador.notifyDataSetChanged() // notificar nuevos cambios
                }
            }
        }
    val callbackContenidoEditarEstudiante =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    // Acciones a realizar si la actividad se completó exitosamente
                    val data = result.data

                    val nombre = data?.getStringExtra("nombreEstudiante").toString()
                    val edad = data?.getLongExtra("edadEstudiante", 0).toString().toLong()
                    val fechaIngreso = data?.getStringExtra("fechaIngresoEstudiante").toString()
                    val estado = data?.getBooleanExtra("estadoEstudiante", false).toString().toBoolean()
                    val promedioCalificaciones = data?.getDoubleExtra("promedioCalificacionesEstudiante", 0.0).toString().toDouble()
                    firestoreEstudiante.editarEstudiante(
                        Estudiante(
                            nombre,
                            edad,
                            fechaIngreso,
                            estado,
                            promedioCalificaciones
                        )
                    )

                    //actualizar los atributos del estudiante en el
                    // array con los valores devueltos por la actividad editar
                    arrayEstudiantes[posicionElementoEstudiantes].nombre = nombre
                    arrayEstudiantes[posicionElementoEstudiantes].edad = edad
                    arrayEstudiantes[posicionElementoEstudiantes].fechaIngreso = fechaIngreso
                    arrayEstudiantes[posicionElementoEstudiantes].estado = estado
                    arrayEstudiantes[posicionElementoEstudiantes].promedioCalificaciones = promedioCalificaciones

                    // actualiza la lista de estudiantes en la universidad seleccionada
                    MainActivity.arrayUniversidad[MainActivity.posicionUniversidad].
                    listaEstudiantes[posicionElementoEstudiantes] =
                        arrayEstudiantes[posicionElementoEstudiantes]

                    // Actualización del adaptador y notificación de cambios
                    adaptador.notifyDataSetChanged()

                    // muestra mensaje de exito
                    mostrarSnackbar("Estudiante modificado exitosamente")
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_estudiantes)

        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arrayEstudiantes
        )

        val listView = findViewById<ListView>(R.id.listv_estudiantes)

        //establece el adaptador en la ListView
        listView.adapter = adaptador

        //asigna a array el arreglo de estudiantes correspondiente
        // a la posición del elemento seleccionado en la actividad principal (MainActivity).
        //arrayEstudiantes = MainActivity.arrayUniversidad[MainActivity.posicionUniversidad].listaEstudiantes

        // crear un adaptador ArrayAdapter que utiliza el diseño predefinido
        // android.R.layout.simple_list_item_1 para mostrar los elementos del array en la ListView.

        val db = Firebase.firestore
        val estudiantesExistentes = db.collection("Universidades")
            .document(MainActivity.arrayUniversidad[MainActivity.posicionUniversidad].id)
            .collection("estudiantes")
        limpiarArreglo()
        adaptador.notifyDataSetChanged()

        estudiantesExistentes
            .get()
            .addOnSuccessListener {
                // it => eso (lo que llegue)
                for (estudiantes in it){
                    estudiantes.id
                    anadirAArregloEstudiante(estudiantes)
                }
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener {
                // Errores
            }
        adaptador.notifyDataSetChanged()

        //establece el texto del TextView con el nombre de la universidad
        val estudianteSeleccionado = findViewById<TextView>(R.id.txt_universidadEstudiante)
        estudianteSeleccionado.setText(MainActivity.arrayUniversidad[MainActivity.posicionUniversidad].nombre)

        val btnCrearEstudiante = findViewById<Button>(R.id.btn_crear_estudiante)
        btnCrearEstudiante
            .setOnClickListener {
                agregarEstudiante(adaptador)
            }
        val btnRegresar = findViewById<Button>(R.id.btn_volver_estudiante)
        btnRegresar
            .setOnClickListener {
                finish()
            }
        registerForContextMenu(listView) // registra la ListView para que pueda mostrar un menú contextual

    }

    fun anadirAArregloEstudiante(
        estudiante: QueryDocumentSnapshot
    ){
        val id = estudiante.id
        val nuevoEstudiante = Estudiante(
            id,
            estudiante.data.get("nombre") as String,
            estudiante.data.get("edad") as Long,
            estudiante.data.get("fechaIngreso") as String,
            estudiante.data.get("estado") as Boolean,
            estudiante.data.get("promedioCalificaciones") as Double
        )
        arrayEstudiantes.add(nuevoEstudiante)
        MainActivity.arrayUniversidad[MainActivity.posicionUniversidad]
            .listaEstudiantes.add(nuevoEstudiante)
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

    fun agregarEstudiante(
        adaptador: ArrayAdapter<Estudiante>
    ) {
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
                firestoreEstudiante.eliminarEstudiante()
                adaptador.notifyDataSetChanged()
                mostrarSnackbar("Estudiante eliminado exitosamente")
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
            "nombre", arrayEstudiantes[posicionElementoEstudiantes].nombre)
        intentExplicito.putExtra(
            "edad", arrayEstudiantes[posicionElementoEstudiantes].edad)
        intentExplicito.putExtra(
            "fechaIngreso", arrayEstudiantes[posicionElementoEstudiantes].fechaIngreso)
        intentExplicito.putExtra(
            "estado", arrayEstudiantes[posicionElementoEstudiantes].estado)
        intentExplicito.putExtra(
            "promedioCalificaciones", arrayEstudiantes[posicionElementoEstudiantes].promedioCalificaciones)

        callbackContenidoEditarEstudiante.launch(intentExplicito)
    }

    fun mostrarSnackbar(texto: String) {
        val snack = Snackbar.make(
            findViewById(R.id.cons_estudiantes),
            texto,
            Snackbar.LENGTH_LONG
        )
        snack.show()
    }
    fun limpiarArreglo() {
        arrayEstudiantes.clear()
    }
    companion object { // para tener propiedades estáticas compartidas entre instancias de la clase
        var arrayEstudiantes = arrayListOf<Estudiante>()
        var posicionElementoEstudiantes = 0
    }

}