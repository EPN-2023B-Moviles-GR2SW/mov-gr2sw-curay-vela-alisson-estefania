package com.example.examenib

class BDDEstudiante (
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
