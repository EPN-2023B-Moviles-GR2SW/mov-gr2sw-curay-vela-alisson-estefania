package com.example.examenib

class Estudiante (
    var id: Long = -1, // Añadido para identificación única en la base de datos
    var nombre: String,
    var edad: Int,
    var fechaIngreso: String,
    var estado: Boolean,
    var promedioCalificaciones: Double
    ) {
    override fun toString(): String {
        return "${nombre}  ${promedioCalificaciones}"
    }
}
