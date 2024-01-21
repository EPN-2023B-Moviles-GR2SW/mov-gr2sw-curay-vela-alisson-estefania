package com.example.examenib

class BDDUniversidad (
    var nombre: String,
    var ubicacion: String,
    var fechaFundacion: String,
    var areaCobertura: Double,
    var listaEstudiantes: ArrayList<BDDEstudiante>
) {
    override fun toString(): String {
        return "${nombre}  ${ubicacion}  ${fechaFundacion}"
    }
}