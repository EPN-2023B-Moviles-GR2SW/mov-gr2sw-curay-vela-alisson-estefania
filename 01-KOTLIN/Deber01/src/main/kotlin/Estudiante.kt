// Alisson Curay

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicReference

data class Estudiante(
    var nombre: String,
    var edad: Int,
    private val fechaIngresoInternal: AtomicReference<LocalDate>,
    var promedioCalificaciones: Double,
    var esEstudianteActivo: Boolean,
    val idUniversidad: Int
) : Entidad("estudiantes.txt", listOf("nombre", "edad", "fechaIngreso",
    "promedioCalificaciones", "esEstudianteActivo", "idUniversidad")) {

    var fechaIngreso: LocalDate
        get() = fechaIngresoInternal.get()
        set(value) {
            fechaIngresoInternal.set(value)
        }

    override fun obtenerValorCampo(campo: String): String {
        return when (campo) {
            "nombre" -> nombre
            "edad" -> edad.toString()
            "fechaIngreso" -> formatearFecha(fechaIngreso)
            "promedioCalificaciones" -> promedioCalificaciones.toString()
            "esEstudianteActivo" -> esEstudianteActivo.toString()
            "idUniversidad" -> idUniversidad.toString()
            else -> throw IllegalArgumentException("Campo no válido: $campo")
        }
    }

    override fun toString(): String {
        return "${nombre},${edad},${formatearFecha(fechaIngreso)},${promedioCalificaciones},${esEstudianteActivo},${idUniversidad}"
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Estudiante) return false

        return nombre == other.nombre &&
                edad == other.edad &&
                fechaIngreso == other.fechaIngreso &&
                promedioCalificaciones == other.promedioCalificaciones &&
                esEstudianteActivo == other.esEstudianteActivo &&
                idUniversidad == other.idUniversidad
    }


    companion object {
        private val formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fun fromString(linea: String): Estudiante {
            val partes = linea.split(",")
            return Estudiante(
                partes[0],
                partes[1].toInt(),
                AtomicReference(LocalDate.parse(partes[2], formatoFecha)),
                partes[3].toDouble(),
                partes[4].toBoolean(),
                partes[5].toInt()
            )
        }
    }
}

