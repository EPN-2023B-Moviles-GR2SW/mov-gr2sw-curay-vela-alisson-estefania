package com.example.b2023gr2sw

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ESqliteHelperEntrenador (
    contexto: Context?,
)  : SQLiteOpenHelper(
    contexto,
    "moviles", // nombre BDD
    null,
    1
) {

    override fun onCreate(db: SQLiteDatabase?) {
        val scriptSQLCrearTablaEntrenador =
            """
            CREATE TABLE ENTRENADOR(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre VARCHAR(50),
            descripcion VARCHAR(50)
            )
        """.trimIndent()
        db?.execSQL(scriptSQLCrearTablaEntrenador)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun crearEntrenador(
        nombre: String,
        descripcion: String
    ): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("descripcion",descripcion)
        val resultadoGuardar = basedatosEscritura
            .insert(
                "ENTRENADOR",
                null,
                valoresAGuardar
            )
        basedatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarEntrenadorFormulario(id:Int): Boolean{
        val conexionEscritura = writableDatabase
        // where ID = ?
        val parametrosConsultaDelete = arrayOf( id.toString())
        val resultadoEliminacion = conexionEscritura
            .delete(
                "ENTRENADOR", //Nombre tabla
                "id=?",
                parametrosConsultaDelete
            )
        conexionEscritura.close()
        return if(resultadoEliminacion.toInt() == -1) false else true
    }

    fun actualizarEntrenadorFormulario(
        nombre: String,
        descripcion: String,
        id: Int,
    ): Boolean{
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("descripcion", descripcion)
        // where ID = ?
        val parametrosConsultaActualizar = arrayOf(id.toString())
        val resultadoActualizacion = conexionEscritura
            .update(
                "ENTRENADOR", // Nombre de la tabla
                valoresAActualizar,
                "id=?", //ConsultaWhere
                parametrosConsultaActualizar
            )
        conexionEscritura.close()
        return if(resultadoActualizacion.toInt() == -1) false else true

    }
    fun consultarEntrenadorPorID(id: Int): BEntrenador{
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM ENTRENADOR WHERE ID = ?
        """.trimIndent()
        val parametrosConsultaLectura = arrayOf(id.toString())
        val resultadosConsultaLectura = baseDatosLectura.rawQuery(
            scriptConsultaLectura, //consulta
            parametrosConsultaLectura //parámetros
        )

        //Lógica búsqueda
        val existeUsuario = resultadosConsultaLectura.moveToFirst()
        val usuarioEncontrado = BEntrenador(0,"","")
        val arreglo = arrayListOf<BEntrenador>()
        if (existeUsuario){
            do {
                val id = resultadosConsultaLectura.getInt(0) //Índice 0
                val nombre = resultadosConsultaLectura.getString(1)
                val descripcion = resultadosConsultaLectura.getString(2)
                if (id != null){
                    //llenar el arreglo con un nuevo BEntrenador
                    usuarioEncontrado.id = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.descripcion = descripcion
                }

            }while (resultadosConsultaLectura.moveToNext())
        }
        resultadosConsultaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado
    }
}