package com.example.examenib

class Universidad(
    var id: Long = -1,
    var nombre: String,
    var ubicacion: String,
    var fechaFundacion: String,
    var areaCobertura: Double,
    var listaEstudiantes: ArrayList<Estudiante> = ArrayList()
) {
    override fun toString(): String {
        return "${nombre}  ${ubicacion}  ${fechaFundacion}"
    }
}