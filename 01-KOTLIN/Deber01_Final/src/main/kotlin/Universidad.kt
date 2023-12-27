// Alisson Curay

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicReference

data class Universidad(
    val id: Int,
    var nombre: String,
    var ubicacion: String,
    val fechaFundacionInternal: AtomicReference<LocalDate>,
    var dimension: Double

) : Entidad("universidades.txt", listOf("id", "nombre", "ubicacion", "dimension", "fechaFundacion" )) {

    var fechaFundacion: LocalDate
        get() = fechaFundacionInternal.get()
        set(value) {
            fechaFundacionInternal.set(value)
        }

    val estudiantes: MutableList<Estudiante> = mutableListOf()

    override fun obtenerValorCampo(campo: String): String {
        return when (campo) {
            "id" -> id.toString()
            "nombre" -> nombre
            "ubicacion" -> ubicacion
            "dimension" -> dimension.toString()
            "fechaFundacion" -> formatearFecha(fechaFundacion)

            else -> throw IllegalArgumentException("Campo no válido: $campo")
        }
    }

    override fun mostrarInformacion() {
        println("Nombre: $nombre")
        println("Ubicación: $ubicacion")
        println("Dimensión: ${dimension.toDouble()}")
        println("Fecha de Fundación: ${formatearFecha(fechaFundacion)}")
    }
    companion object {
        private val formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fun fromString(linea: String): Universidad {
            val partes = linea.split(",")
            return Universidad(
                partes[0].toInt(),
                partes[1],
                partes[2],
                AtomicReference(LocalDate.parse(partes[3], formatoFecha)),
                partes[4].toDouble()
            )
        }
    }
}
