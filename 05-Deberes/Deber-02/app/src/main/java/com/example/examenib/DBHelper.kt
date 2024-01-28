package com.example.examenib

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "university_db"
        const val DATABASE_VERSION = 1

        const val TABLE_UNIVERSIDADES = "universidades"
        const val COLUMN_ID = "id"
        const val COLUMN_NOMBRE = "nombre"
        const val COLUMN_UBICACION = "ubicacion"
        const val COLUMN_FECHA_FUNDACION = "fechaFundacion"
        const val COLUMN_AREA_COBERTURA = "areaCobertura"

        const val TABLE_ESTUDIANTES = "estudiantes"
        const val COLUMN_ID_ESTUDIANTE = "idEstudiante"
        const val COLUMN_NOMBRE_ESTUDIANTE = "nombreEstudiante"
        const val COLUMN_EDAD_ESTUDIANTE = "edadEstudiante"
        const val COLUMN_FECHA_INGRESO_ESTUDIANTE = "fechaIngresoEstudiante"
        const val COLUMN_ESTADO_ESTUDIANTE = "estadoEstudiante"
        const val COLUMN_PROMEDIO_ESTUDIANTE = "promedioEstudiante"
        const val COLUMN_ID_UNIVERSIDAD = "idUniversidad"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Crear la tabla de Universidades
        db.execSQL(
            "CREATE TABLE $TABLE_UNIVERSIDADES (" +
                    "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NOMBRE TEXT," +
                    "$COLUMN_UBICACION TEXT," +
                    "$COLUMN_FECHA_FUNDACION TEXT," +
                    "$COLUMN_AREA_COBERTURA REAL)"
        )

        // Crear la tabla de Estudiantes
        db.execSQL(
            "CREATE TABLE $TABLE_ESTUDIANTES (" +
                    "$COLUMN_ID_ESTUDIANTE INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "$COLUMN_NOMBRE_ESTUDIANTE TEXT," +
                    "$COLUMN_EDAD_ESTUDIANTE INTEGER," +
                    "$COLUMN_FECHA_INGRESO_ESTUDIANTE TEXT," +
                    "$COLUMN_ESTADO_ESTUDIANTE INTEGER," +
                    "$COLUMN_PROMEDIO_ESTUDIANTE REAL," +
                    "$COLUMN_ID_UNIVERSIDAD INTEGER," +
                    "FOREIGN KEY($COLUMN_ID_UNIVERSIDAD) REFERENCES $TABLE_UNIVERSIDADES($COLUMN_ID))"
        )
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Manejar actualizaciones de la base de datos si es necesario
    }
}