// Archivo: Entidad.kt

import java.io.File

open class Entidad(
    val nombreArchivo: String,
    val campos: List<String>
) {
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

    // Cambios en toString para mantener el formato CSV
    override fun toString(): String {
        return campos.joinToString(",")
    }

    protected open fun obtenerValorCampo(campo: String): String {
        throw NotImplementedError("Implementar en las clases derivadas")
    }
}
