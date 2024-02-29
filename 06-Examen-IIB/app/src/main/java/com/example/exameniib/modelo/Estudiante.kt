package com.example.exameniib.modelo

class Estudiante (

    var id: String,
    var nombre: String,
    var edad: Long,
    var fechaIngreso: String,
    var estado: Boolean,
    var promedioCalificaciones: Double
) {

    constructor(
        nombre: String,
        edad: Long,
        fechaIngreso: String,
        estado: Boolean,
        promedioCalificaciones: Double
    ):this(
        "",
        nombre,
        edad,
        fechaIngreso,
        estado,
        promedioCalificaciones
    )
    override fun toString(): String {
        return "${nombre}  ${promedioCalificaciones}"
    }
}