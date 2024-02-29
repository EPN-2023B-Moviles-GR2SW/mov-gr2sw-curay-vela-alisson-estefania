package com.example.exameniib.modelo

class Universidad (

    var id: String,
    var nombre: String,
    var ubicacion: String,
    var fechaFundacion: String,
    var areaCobertura: Double,
    var listaEstudiantes: ArrayList<Estudiante>
) {
    constructor(
        nombre: String,
        ubicacion: String,
        fechaFundacion: String,
        areaCobertura: Double,
        listaEstudiantes: ArrayList<Estudiante>
    ) : this(
        "",
        nombre,
        ubicacion,
        fechaFundacion,
        areaCobertura,
        listaEstudiantes
    )

    override fun toString(): String {
        return "${nombre} - ${ubicacion}"
    }

}