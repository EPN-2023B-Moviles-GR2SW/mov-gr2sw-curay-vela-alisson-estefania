// Alisson Curay

import java.io.File
import java.text.ParseException
import java.util.Date
import java.util.concurrent.atomic.AtomicReference
import java.time.LocalDate
import java.time.format.DateTimeFormatter

open class Entidad(
    val nombreArchivo: String,
    val campos: List<String>
) {
    private val formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun leerDesdeArchivo(): List<String> {
        val archivo = File(nombreArchivo)
        return if (archivo.exists()) {
            archivo.readLines()
        } else {
            emptyList()
        }
    }

    fun guardarEnArchivo(datos: List<String>) {
        val archivo = File(nombreArchivo)
        archivo.writeText(datos.joinToString("\n"))
    }

    open fun mostrarInformacion() {
        println("Información no disponible.")
    }

    override fun toString(): String {
        return campos.joinToString(",")
    }

    protected open fun obtenerValorCampo(campo: String): String {
        throw NotImplementedError("Implementar en las clases derivadas")
    }

    protected fun formatearFecha(fecha: LocalDate): String {
        return fecha.format(formatoFecha)
    }

    protected fun analizarFecha(fechaStr: String): LocalDate {
        return try {
            LocalDate.parse(fechaStr, formatoFecha)
        } catch (e: ParseException) {
            LocalDate.now() // Fecha por defecto en caso de error
        }
    }
}
