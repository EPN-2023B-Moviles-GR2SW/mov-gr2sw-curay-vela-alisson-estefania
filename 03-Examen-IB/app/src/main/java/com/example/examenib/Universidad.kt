package com.example.examenib

class Universidad (
    var nombre: String,
    var ubicacion: String,
    var fechaFundacion: String,
    var areaCobertura: Double,
    var listaEstudiantes: ArrayList<Estudiante>
) {
    override fun toString(): String {
        return "${nombre}  ${ubicacion}  ${fechaFundacion}"
    }
}