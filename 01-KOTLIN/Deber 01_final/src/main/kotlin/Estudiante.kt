//Clase estudiante
import java.text.SimpleDateFormat
import java.util.Date

data class Estudiante(
    var nombre: String,
    var edad: Int,
    var fechaIngreso: Date,
    var promedioCalificaciones: Double,
    var esEstudianteActivo: Boolean,
    val idUniversidad: Int
) : Entidad("estudiantes.txt", listOf("nombre", "edad", "fechaIngreso",
    "promedioCalificaciones", "esEstudianteActivo", "idUniversidad")) {

    override fun obtenerValorCampo(campo: String): String {
        return when (campo) {
            "nombre" -> nombre
            "edad" -> edad.toString()
            "fechaIngreso" -> SimpleDateFormat("yyyy-MM-dd").format(fechaIngreso)
            "promedioCalificaciones" -> promedioCalificaciones.toString()
            "esEstudianteActivo" -> esEstudianteActivo.toString()
            "idUniversidad" -> idUniversidad.toString()
            else -> throw IllegalArgumentException("Campo no válido: $campo")
        }
    }

    companion object {
        // Nueva función para crear un estudiante desde una cadena CSV
        fun fromString(linea: String): Estudiante {
            val partes = linea.split(",")
            return Estudiante(
                partes[0],
                partes[1].toInt(),
                SimpleDateFormat("yyyy-MM-dd").parse(partes[2]),
                partes[3].toDouble(),
                partes[4].toBoolean(),
                partes[5].toInt()
            )
        }
    }
}
