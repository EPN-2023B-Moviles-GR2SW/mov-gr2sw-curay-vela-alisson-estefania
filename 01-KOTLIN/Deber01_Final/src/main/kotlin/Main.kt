// Alisson Curay

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicReference
import java.text.SimpleDateFormat
import java.time.format.DateTimeParseException
import java.util.Date

fun main() {
    val universidades = cargarUniversidades()
    val estudiantes = cargarEstudiantes(universidades)

    loop@ while (true) {
        mostrarMenu()

        when (readLine()?.toString()) {
            "a" -> {
                crearNuevaUniversidad(universidades)
            }
            "b" -> {
                editarUniversidad(universidades)
            }
            "c" -> {
                eliminarUniversidad(universidades, estudiantes)
            }
            "d" -> {
                mostrarUniversidades(universidades)
            }
            "e" -> {
                crearNuevoEstudiante(universidades, estudiantes)
            }
            "f" -> {
                editarEstudiante(estudiantes)
            }
            "g" -> {
                eliminarEstudiante(estudiantes, universidades)
            }
            "h" -> {
                mostrarEstudiantes(estudiantes)
            }
            "i" -> {
                mostrarEstudiantesPorUniversidad(universidades)
            }
            "j" -> {
                // Guardar y salir
                guardarDatos(universidades, estudiantes)
                break@loop
            }
            else -> {
                println("Opción no válida. Inténtelo de nuevo.")
            }
        }
    }
}
fun mostrarMenu() {
    println("Menú:")
    println("a. Crear nueva universidad")
    println("b. Editar información de universidad")
    println("c. Eliminar universidad (universidades, estudiantes)")
    println("d. Mostrar universidades")
    println("e. Crear nuevo estudiante")
    println("f. Editar información de estudiante")
    println("g. Eliminar estudiante")
    println("h. Mostrar lista de estudiantes")
    println("i. Mostrar estudiantes por universidad")
    println("j. Salir")
    print("Ingrese su elección: ")
}
// En la función cargarUniversidades

fun cargarUniversidades(): MutableList<Universidad> {
    val universidades = mutableListOf<Universidad>()
    val datos = Entidad("universidades.txt", emptyList()).leerDesdeArchivo()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    for (linea in datos) {
        // Utilizar simplemente split(",") para dividir la línea
        val partes = linea.split(",")

        if (partes.size == 5) {
            val id = partes[0].toInt()
            val nombre = partes[1]
            val ubicacion = partes[2]
            val fechaFundacionStr = partes[3]
            val dimension = partes[4].toDouble()

            try {
                val fechaFundacion = LocalDate.parse(fechaFundacionStr, formatter)
                val atomicFechaFundacion = AtomicReference(fechaFundacion)

                val universidad = Universidad(id, nombre, ubicacion, atomicFechaFundacion, dimension)
                universidades.add(universidad)
            } catch (e: DateTimeParseException) {
                println("Error al parsear la fecha en la línea: $linea")
            }
        } else {
            println("Error al leer datos de universidad: $linea")
        }
    }

    return universidades
}
// En la función cargarEstudiantes
fun cargarEstudiantes(universidades: List<Universidad>): MutableList<Estudiante> {
    val estudiantes = mutableListOf<Estudiante>()
    val datos = Entidad("estudiantes.txt", emptyList()).leerDesdeArchivo()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    for (linea in datos) {
        // Utilizar simplemente split(",") para dividir la línea
        val partes = linea.split(",")

        if (partes.size == 6) {
            val nombre = partes[0]
            val edad = partes[1].toInt()
            val fechaIngresoStr = partes[2]
            val promedioCalificaciones = partes[3].toDouble()
            val esEstudianteActivo = partes[4].toBoolean()
            val idUniversidad = partes[5].toInt()

            try {
                val fechaIngreso = LocalDate.parse(fechaIngresoStr, formatter)
                val atomicFechaIngreso = AtomicReference(fechaIngreso)

                val estudiante = Estudiante(
                    nombre,
                    edad,
                    atomicFechaIngreso,
                    promedioCalificaciones,
                    esEstudianteActivo,
                    idUniversidad
                )
                estudiantes.add(estudiante)
                // Obtener la universidad correspondiente y agregar el estudiante
                universidades.find { it.id == estudiante.idUniversidad }?.estudiantes?.add(estudiante)
            } catch (e: DateTimeParseException) {
                println("Error al parsear la fecha en la línea: $linea")
            }
        } else {
            println("Error al leer datos de estudiante: $linea")
        }
    }

    return estudiantes
}


fun crearNuevaUniversidad(universidades: MutableList<Universidad>) {
    print("Ingrese el nombre de la universidad: ")
    val nombre = readLine() ?: ""
    print("Ingrese la ubicación: ")
    val ubicacion = readLine() ?: ""
    print("Ingrese la fecha de fundación (Formato: yyyy-MM-dd): ")
    val fechaFundacionStr = readLine() ?: ""
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    try {
        val fechaFundacion = LocalDate.parse(fechaFundacionStr, formatter)

        print("Ingrese la dimensión (Formato: m2): ")
        val dimension = readLine()?.toDoubleOrNull() ?: 0.0
        val nuevaUniversidad = Universidad(universidades.size + 1, nombre, ubicacion, AtomicReference(fechaFundacion), dimension)
        universidades.add(nuevaUniversidad)
        println("Universidad creada exitosamente.")
    } catch (e: Exception) {
        println("Error: Formato de fecha incorrecto. La universidad no se ha creado.")
    }
}

fun crearNuevoEstudiante(universidades: List<Universidad>, estudiantes: MutableList<Estudiante>) {
    println("Seleccione la universidad para el nuevo estudiante:")
    universidades.forEach { println("${it.id}. ${it.nombre}") }

    val idUniversidad = readLine()?.toIntOrNull() ?: 0
    val universidadSeleccionada = universidades.find { it.id == idUniversidad }

    if (universidadSeleccionada != null) {
        print("Ingrese el nombre del estudiante: ")
        val nombre = readLine() ?: ""
        print("Ingrese la edad: ")
        val edad = readLine()?.toIntOrNull() ?: 0
        print("Ingrese la fecha de ingreso (Formato: yyyy-MM-dd): ")
        val fechaIngresoStr = readLine() ?: ""
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        try {
            val fechaIngreso = LocalDate.parse(fechaIngresoStr, formatter)

            print("Ingrese el promedio de calificaciones: ")
            val promedioCalificaciones = readLine()?.toDoubleOrNull() ?: 0.0
            print("¿Está activo? (true/false): ")
            val esEstudianteActivo = readLine()?.toBoolean() ?: false

            // Verificar si el estudiante ya pertenece a otra universidad
            if (estudiantes.any { it.nombre == nombre && it.idUniversidad != idUniversidad }) {
                println("Error: El estudiante ya pertenece a otra universidad. No se permite registrar en dos universidades diferentes.")
            } else {
                val nuevoEstudiante = Estudiante(
                    nombre,
                    edad,
                    AtomicReference(fechaIngreso),
                    promedioCalificaciones,
                    esEstudianteActivo,
                    idUniversidad
                )

                if (estudiantes.add(nuevoEstudiante)) {
                    universidadSeleccionada.estudiantes.add(nuevoEstudiante)
                    println("Estudiante registrado correctamente en ${universidadSeleccionada.nombre}.")
                } else {
                    println("Error al agregar el estudiante. Por favor, inténtelo de nuevo.")
                }
            }
        } catch (e: Exception) {
            println("Error: Formato de fecha incorrecto. El estudiante no se ha creado.")
        }
    } else {
        println("Universidad no válida.")
    }
}

// Nueva función para editar la información de un estudiante
fun editarEstudiante(estudiantes: List<Estudiante>) {
    println("Seleccione el estudiante que desea editar:")
    estudiantes.forEachIndexed { index, estudiante ->
        println("$index. ${estudiante.nombre}")
    }

    val indiceEstudiante = readLine()?.toIntOrNull()

    if (indiceEstudiante != null && indiceEstudiante in estudiantes.indices) {
        val estudianteSeleccionado = estudiantes[indiceEstudiante]
        println("Editar información del estudiante ${estudianteSeleccionado.nombre}:")

        print("Nuevo nombre: ")
        val nuevoNombre = readLine() ?: estudianteSeleccionado.nombre

        print("Nueva edad: ")
        val nuevaEdad = readLine()?.toIntOrNull() ?: estudianteSeleccionado.edad

        print("Nueva fecha de ingreso (Formato: yyyy-MM-dd): ")
        val nuevaFechaIngresoStr = readLine()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val nuevaFechaIngreso = nuevaFechaIngresoStr?.let {
            try {
                LocalDate.parse(it, formatter)
            } catch (e: Exception) {
                estudianteSeleccionado.fechaIngreso
            }
        } ?: estudianteSeleccionado.fechaIngreso

        print("Nuevo promedio de calificaciones: ")
        val nuevoPromedio = readLine()?.toDoubleOrNull() ?: estudianteSeleccionado.promedioCalificaciones

        print("¿Está activo? (true/false): ")
        val nuevoEstadoActivo = readLine()?.toBoolean() ?: estudianteSeleccionado.esEstudianteActivo

        // Actualizar la información del estudiante
        estudianteSeleccionado.apply {
            nombre = nuevoNombre
            edad = nuevaEdad
            fechaIngreso = nuevaFechaIngreso
            promedioCalificaciones = nuevoPromedio
            esEstudianteActivo = nuevoEstadoActivo
        }

        println("Información del estudiante actualizada correctamente.")
    } else {
        println("Índice de estudiante no válido.")
    }
}



// Nueva función para editar la información de una universidad
fun editarUniversidad(universidades: List<Universidad>) {
    println("Seleccione la universidad que desea editar:")
    universidades.forEachIndexed { index, universidad ->
        println("$index. ${universidad.nombre}")
    }

    val indiceUniversidad = readLine()?.toIntOrNull()

    if (indiceUniversidad != null && indiceUniversidad in universidades.indices) {
        val universidadSeleccionada = universidades[indiceUniversidad]
        println("Editar información de la universidad ${universidadSeleccionada.nombre}:")

        print("Nuevo nombre: ")
        val nuevoNombre = readLine()?.takeIf { it.isNotBlank() } ?: universidadSeleccionada.nombre

        print("Nueva ubicación: ")
        val nuevaUbicacion = readLine()?.takeIf { it.isNotBlank() } ?: universidadSeleccionada.ubicacion

        print("Nueva fecha de fundación (Formato: yyyy-MM-dd): ")
        val nuevaFechaFundacionStr = readLine()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val nuevaFechaFundacion = nuevaFechaFundacionStr?.let {
            try {
                LocalDate.parse(it, formatter)
            } catch (e: Exception) {
                universidadSeleccionada.fechaFundacion
            }
        } ?: universidadSeleccionada.fechaFundacion

        // Actualizar la información de la universidad
        universidadSeleccionada.apply {
            nombre = nuevoNombre
            ubicacion = nuevaUbicacion
            fechaFundacion = nuevaFechaFundacion
        }

        println("Información de la universidad actualizada correctamente.")
    } else {
        println("Índice de universidad no válido.")
    }
}
// Nuevas funciones para CRUD de universidades
fun eliminarUniversidad(universidades: MutableList<Universidad>, estudiantes: MutableList<Estudiante>) {
    println("Seleccione la universidad que desea eliminar:")
    mostrarUniversidades(universidades)

    val indiceUniversidad = readLine()?.toIntOrNull()

    if (indiceUniversidad != null && indiceUniversidad in universidades.indices) {
        val universidadSeleccionada = universidades[indiceUniversidad]

        // Eliminar estudiantes asociados a la universidad
        val estudiantesAsociados = estudiantes.filter { it.idUniversidad == universidadSeleccionada.id }
        estudiantes.removeAll(estudiantesAsociados)

        // Eliminar la universidad
        universidades.removeAt(indiceUniversidad)

        println("Universidad eliminada correctamente junto con ${estudiantesAsociados.size} estudiantes asociados.")

        // Guardar los cambios en el archivo
        guardarDatos(universidades, estudiantes)
    } else {
        println("Índice de universidad no válido.")
    }
}

fun mostrarUniversidades(universidades: List<Universidad>) {
    println("Lista de Universidades:")

    if (universidades.isNotEmpty()) {
        universidades.forEachIndexed { index, universidad ->
            println("$index. ${universidad.nombre}")
        }
    } else {
        println("No hay universidades para mostrar.")
    }
}
// Nuevas funciones para CRUD de estudiantes
fun eliminarEstudiante(estudiantes: MutableList<Estudiante>, universidades: List<Universidad>) {
    println("Seleccione el estudiante que desea eliminar:")
    mostrarEstudiantes(estudiantes)
    val indiceEstudiante = readLine()?.toIntOrNull()

    if (indiceEstudiante != null && indiceEstudiante in 0 until estudiantes.size) {
        val estudianteSeleccionado = estudiantes[indiceEstudiante]

        // Eliminar el estudiante de la lista de la universidad
        for (universidad in universidades) {
            if (universidad.estudiantes.contains(estudianteSeleccionado)) {
                universidad.estudiantes.remove(estudianteSeleccionado)
                break
            }
        }

        // Eliminar el estudiante de la lista general
        estudiantes.remove(estudianteSeleccionado)

        println("Estudiante eliminado correctamente.")

        // Guardar los cambios en el archivo
        guardarDatos(universidades, estudiantes)
    } else {
        println("Índice de estudiante no válido.")
    }
}
fun mostrarEstudiantes(estudiantes: List<Estudiante>) {
    println("Lista de Estudiantes:")

    if (estudiantes.isNotEmpty()) {
        estudiantes.forEachIndexed { index, estudiante ->
            println("$index. ${estudiante.nombre}")
        }
    } else {
        println("No hay estudiantes para mostrar.")
    }
}

// Función para mostrar estudiantes por universidad
fun mostrarEstudiantesPorUniversidad(universidades: List<Universidad>) {
    println("Estudiantes agrupados por Universidad:")
    val formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    for (universidad in universidades) {
        println("-----------------------------------")
        // Verificar si la universidad aún existe
        if (universidades.contains(universidad)) {
            universidad.mostrarInformacion()
            println("Estudiantes:")
            for (estudiante in universidad.estudiantes) {
                val fechaIngresoStr = formatoFecha.format(estudiante.fechaIngreso)
                println(" - Nombre: ${estudiante.nombre}, Edad: ${estudiante.edad}, " +
                        "Fecha de Ingreso: $fechaIngresoStr, " +
                        "Promedio: ${estudiante.promedioCalificaciones}, " +
                        "Activo: ${estudiante.esEstudianteActivo}")
            }
        } else {
            println("Información no disponible.")
        }
        println("\n")
    }
}

// Función guardarDatos

fun guardarDatos(universidades: List<Universidad>, estudiantes: List<Estudiante>) {
    val formatoFecha = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    // Convertir datos de universidades y estudiantes a líneas de texto
    val datosTotalesUniversidades = universidades.map { universidad ->
        "${universidad.id},${universidad.nombre},${universidad.ubicacion}," +
                "${formatoFecha.format(universidad.fechaFundacion)},${universidad.dimension}"
    }

    val datosTotalesEstudiantes = estudiantes.map { estudiante ->
        "${estudiante.nombre},${estudiante.edad}," +
                "${formatoFecha.format(estudiante.fechaIngreso)}," +
                "${estudiante.promedioCalificaciones}," +
                "${estudiante.esEstudianteActivo},${estudiante.idUniversidad}"
    }

    // Guardar datos en archivos
    guardarEnArchivo("universidades.txt", datosTotalesUniversidades)
    guardarEnArchivo("estudiantes.txt", datosTotalesEstudiantes)
}

fun guardarEnArchivo(nombreArchivo: String, lineas: List<String>) {
    try {
        File(nombreArchivo).bufferedWriter().use { writer ->
            lineas.forEach { linea ->
                writer.write(linea)
                writer.newLine()
            }
        }
    } catch (e: IOException) {
        println("Error al escribir en el archivo $nombreArchivo: ${e.message}")
    }
}






