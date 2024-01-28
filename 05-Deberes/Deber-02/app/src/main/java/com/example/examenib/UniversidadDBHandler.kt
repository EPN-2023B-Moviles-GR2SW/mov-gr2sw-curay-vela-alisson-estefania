package com.example.examenib

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class UniversidadDBHandler(context: Context) {

    private val dbHelper = DBHelper(context)

    fun agregarUniversidad(universidad: Universidad): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_NOMBRE, universidad.nombre)
            put(DBHelper.COLUMN_UBICACION, universidad.ubicacion)
            put(DBHelper.COLUMN_FECHA_FUNDACION, universidad.fechaFundacion)
            put(DBHelper.COLUMN_AREA_COBERTURA, universidad.areaCobertura)
        }
        val id = db.insert(DBHelper.TABLE_UNIVERSIDADES, null, values)
        db.close()
        return id
    }

    fun actualizarUniversidad(universidad: Universidad) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DBHelper.COLUMN_NOMBRE, universidad.nombre)
            put(DBHelper.COLUMN_UBICACION, universidad.ubicacion)
            put(DBHelper.COLUMN_FECHA_FUNDACION, universidad.fechaFundacion)
            put(DBHelper.COLUMN_AREA_COBERTURA, universidad.areaCobertura)
        }
        db.update(
            DBHelper.TABLE_UNIVERSIDADES,
            values,
            "${DBHelper.COLUMN_ID_UNIVERSIDAD} = ?",
            arrayOf(universidad.id.toString())
        )
        db.close()
    }

    fun eliminarUniversidad(idUniversidad: Long) {
        val db = dbHelper.writableDatabase
        db.delete(
            DBHelper.TABLE_UNIVERSIDADES,
            "${DBHelper.COLUMN_ID_UNIVERSIDAD} = ?",
            arrayOf(idUniversidad.toString())
        )
        db.close()
    }

    @SuppressLint("Range")
    fun obtenerUniversidades(): ArrayList<Universidad> {
        val universidades = ArrayList<Universidad>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM ${DBHelper.TABLE_UNIVERSIDADES}",
            null
        )

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_ID_UNIVERSIDAD))
                val nombre =
                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_NOMBRE))
                val ubicacion =
                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_UBICACION))
                val fechaFundacion =
                    cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_FECHA_FUNDACION))
                val areaCobertura =
                    cursor.getDouble(cursor.getColumnIndex(DBHelper.COLUMN_AREA_COBERTURA))

                val universidad =
                    Universidad(id, nombre, ubicacion, fechaFundacion, areaCobertura, arrayListOf())
                universidades.add(universidad)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return universidades
    }

    fun close() {
        // Cerrar la conexión con la base de datos
        dbHelper.close()
    }
}
