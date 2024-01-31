package com.example.examenib.Database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.examenib.Estudiante

class EstudianteDBHandler(context: Context) {

    private val dbHelper = DBHelper(context)

    fun agregarEstudiante(estudiante: Estudiante, idUniversidad: Long): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_NOMBRE_ESTUDIANTE, estudiante.nombre)
            put(DBHelper.COLUMN_EDAD_ESTUDIANTE, estudiante.edad)
            put(DBHelper.COLUMN_FECHA_INGRESO_ESTUDIANTE, estudiante.fechaIngreso)
            put(DBHelper.COLUMN_ESTADO_ESTUDIANTE, if (estudiante.estado) 1 else 0)
            put(DBHelper.COLUMN_PROMEDIO_ESTUDIANTE, estudiante.promedioCalificaciones)
            put(DBHelper.COLUMN_ID_UNIVERSIDAD, idUniversidad)
        }
        val id = db.insert(DBHelper.TABLE_ESTUDIANTES, null, values)
        db.close()
        return id
    }

    fun actualizarEstudiante(estudiante: Estudiante) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_NOMBRE_ESTUDIANTE, estudiante.nombre)
            put(DBHelper.COLUMN_EDAD_ESTUDIANTE, estudiante.edad)
            put(DBHelper.COLUMN_FECHA_INGRESO_ESTUDIANTE, estudiante.fechaIngreso)
            put(DBHelper.COLUMN_ESTADO_ESTUDIANTE, if (estudiante.estado) 1 else 0)
            put(DBHelper.COLUMN_PROMEDIO_ESTUDIANTE, estudiante.promedioCalificaciones)
        }
        db.update(
            DBHelper.TABLE_ESTUDIANTES,
            values,
            "${DBHelper.COLUMN_ID_ESTUDIANTE} = ?",
            arrayOf(estudiante.id.toString())
        )
        db.close()
    }

    fun eliminarEstudiante(idEstudiante: Long) {
        val db = dbHelper.writableDatabase
        db.delete(
            DBHelper.TABLE_ESTUDIANTES,
            "${DBHelper.COLUMN_ID_ESTUDIANTE} = ?",
            arrayOf(idEstudiante.toString())
        )
        db.close()
    }

    @SuppressLint("Range")
    fun obtenerEstudiantesPorUniversidad(idUniversidad: Long): ArrayList<Estudiante> {
        val estudiantes = ArrayList<Estudiante>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM ${DBHelper.TABLE_ESTUDIANTES} WHERE ${DBHelper.COLUMN_ID_UNIVERSIDAD} = ?",
            arrayOf(idUniversidad.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID_ESTUDIANTE))
                val nombre = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NOMBRE_ESTUDIANTE))
                val edad = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_EDAD_ESTUDIANTE))
                val fechaIngreso = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_FECHA_INGRESO_ESTUDIANTE))
                val estado = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ESTADO_ESTUDIANTE)) == 1
                val promedioCalificaciones =
                    cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PROMEDIO_ESTUDIANTE))

                val estudiante = Estudiante(id, nombre, edad, fechaIngreso, estado, promedioCalificaciones)
                estudiantes.add(estudiante)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return estudiantes
    }

    @SuppressLint("Range")
    fun obtenerEstudiantePorId(idEstudiante: Long): Estudiante {
        val db = dbHelper.readableDatabase
        val cursor = db.query(
            DBHelper.TABLE_ESTUDIANTES,
            null,
            "${DBHelper.COLUMN_ID_ESTUDIANTE} = ?",
            arrayOf(idEstudiante.toString()),
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val nombre = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NOMBRE_ESTUDIANTE))
        val edad = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_EDAD_ESTUDIANTE))
        val fechaIngreso = cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_FECHA_INGRESO_ESTUDIANTE))
        val estado = cursor.getInt(cursor.getColumnIndex(DBHelper.COLUMN_ESTADO_ESTUDIANTE)) == 1
        val promedioCalificaciones =
            cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_PROMEDIO_ESTUDIANTE))

        cursor.close()
        db.close()

        return Estudiante(idEstudiante, nombre, edad, fechaIngreso, estado, promedioCalificaciones)
    }
    fun close() {
        // Cerrar la conexión con la base de datos
        dbHelper.close()
    }
}