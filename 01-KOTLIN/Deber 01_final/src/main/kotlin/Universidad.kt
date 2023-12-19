import java.text.SimpleDateFormat
import java.util.Date

data class Universidad(
    val id: Int,
    var nombre: String,
    var ubicacion: String,
    var fechaFundacion: Date
) : Entidad("universidades.txt", listOf("id", "nombre", "ubicacion", "fechaFundacion")) {
    val estudiantes: MutableList<Estudiante> = mutableListOf()

    override fun obtenerValorCampo(campo: String): String {
        return when (campo) {
            "id" -> id.toString()
            "nombre" -> nombre
            "ubicacion" -> ubicacion
            "fechaFundacion" -> SimpleDateFormat("yyyy-MM-dd").format(fechaFundacion)
            else -> throw IllegalArgumentException("Campo no válido: $campo")
        }
    }

    override fun mostrarInformacion() {
        println("Nombre: $nombre")
        println("Ubicación: $ubicacion")
        println("Fecha de Fundación: ${SimpleDateFormat("yyyy-MM-dd").format(fechaFundacion)}")
    }

    companion object {
        // Nueva función para crear una universidad desde una cadena CSV
        fun fromString(linea: String): Universidad {
            val partes = linea.split(",")
            return Universidad(
                partes[0].toInt(),
                partes[1],
                partes[2],
                SimpleDateFormat("yyyy-MM-dd").parse(partes[3])
            )
        }
    }
}
