// Archivo: Main.kt
import java.io.File
fun main() {
    // Cargar universidades y estudiantes desde archivos
    val universidades = cargarUniversidades()
    val estudiantes = cargarEstudiantes(universidades)
    // Menú principal
    while (true) {
        println("Menú:")
        println("a. Crear nueva universidad")
        println("b. Editar información de universidad")
        println("c. Eliminar universidad (universidades, estudiantes)")
        println("d. Mostrar universidades")
        println("e. Crear nuevo estudiante")
        println("f. Editar información de estudiante")
        println("g. Eliminar estudiante")
        println("h. mostrar estudiante")
        println("i. Mostrar estudiantes por universidad")
        println("j. Salir")
        print("Ingrese su elección: ")

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
                // Guardar universidades y estudiantes en archivos antes de salir
                guardarDatos(universidades.map { it.toString() }, "universidades.txt")
                guardarDatos(estudiantes.map { it.toString() }, "estudiantes.txt")
                return
            }
            else -> {
                println("Opción no válida. Intente de nuevo.")
            }
        }
    }
}
// En la función cargarUniversidades
fun cargarUniversidades(): MutableList<Universidad> {
    val universidades = mutableListOf<Universidad>()
    val datos = Entidad("universidades.txt", emptyList()).leerDesdeArchivo()
    for (linea in datos) {
        val regex = Regex("Universidad\\(id=(\\d+), nombre=(.*), ubicacion=(.*), fechaFundacion=(.*)\\)")
        val matchResult = regex.find(linea)
        if (matchResult != null) {
            val (id, nombre, ubicacion, fechaFundacion) = matchResult.destructured
            val universidad = Universidad(id.toInt(), nombre, ubicacion, fechaFundacion)
            universidades.add(universidad)
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
    for (linea in datos) {
        val regex = Regex("Estudiante\\(nombre=(.*), edad=(\\d+), fechaIngreso=(.*), promedioCalificaciones=(.*), esEstudianteActivo=(.*), idUniversidad=(\\d+)\\)")
        val matchResult = regex.find(linea)
        if (matchResult != null) {
            val (nombre, edad, fechaIngreso, promedioCalificaciones, esEstudianteActivo, idUniversidad) = matchResult.destructured
            val estudiante = Estudiante(
                nombre,
                edad.toInt(),
                fechaIngreso,
                promedioCalificaciones.toDouble(),
                esEstudianteActivo.toBoolean(),
                idUniversidad.toInt()
            )
            estudiantes.add(estudiante)
            universidades.find { it.id == estudiante.idUniversidad }?.estudiantes?.add(estudiante)
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
    print("Ingrese la fecha de fundación: ")
    val fechaFundacion = readLine() ?: ""
    val nuevaUniversidad = Universidad(universidades.size + 1, nombre, ubicacion, fechaFundacion)
    universidades.add(nuevaUniversidad)
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
        print("Ingrese la fecha de ingreso: ")
        val fechaIngreso = readLine() ?: ""
        print("Ingrese el promedio de calificaciones: ")
        val promedioCalificaciones = readLine()?.toDoubleOrNull() ?: 0.0
        print("¿Está activo? (true/false): ")
        val esEstudianteActivo = readLine()?.toBoolean() ?: false

        val nuevoEstudiante = Estudiante(
            nombre,
            edad,
            fechaIngreso,
            promedioCalificaciones,
            esEstudianteActivo,
            idUniversidad
        )
        estudiantes.add(nuevoEstudiante)
        universidadSeleccionada.estudiantes.add(nuevoEstudiante)
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

    if (indiceEstudiante != null && indiceEstudiante in 0 until estudiantes.size) {
        val estudianteSeleccionado = estudiantes[indiceEstudiante]
        println("Editar información del estudiante ${estudianteSeleccionado.nombre}:")

        print("Nuevo nombre: ")
        val nuevoNombre = readLine() ?: estudianteSeleccionado.nombre

        print("Nueva edad: ")
        val nuevaEdad = readLine()?.toIntOrNull() ?: estudianteSeleccionado.edad

        print("Nueva fecha de ingreso: ")
        val nuevaFechaIngreso = readLine() ?: estudianteSeleccionado.fechaIngreso

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

    if (indiceUniversidad != null && indiceUniversidad in 0 until universidades.size) {
        val universidadSeleccionada = universidades[indiceUniversidad]
        println("Editar información de la universidad ${universidadSeleccionada.nombre}:")

        print("Nuevo nombre: ")
        val nuevoNombre = readLine() ?: universidadSeleccionada.nombre

        print("Nueva ubicación: ")
        val nuevaUbicacion = readLine() ?: universidadSeleccionada.ubicacion

        print("Nueva fecha de fundación: ")
        val nuevaFechaFundacion = readLine() ?: universidadSeleccionada.fechaFundacion

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

    if (indiceUniversidad != null && indiceUniversidad in 0 until universidades.size) {
        val universidadSeleccionada = universidades[indiceUniversidad]
        // Eliminar estudiantes asociados a la universidad
        estudiantes.removeAll { it.idUniversidad == universidadSeleccionada.id }
        // Eliminar la universidad
        universidades.removeAt(indiceUniversidad)
        println("Universidad eliminada correctamente.")
    } else {
        println("Índice de universidad no válido.")
    }
}

fun mostrarUniversidades(universidades: List<Universidad>) {
    println("Lista de Universidades:")
    universidades.forEachIndexed { index, universidad ->
        println("$index. ${universidad.nombre}")
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
            if (estudianteSeleccionado in universidad.estudiantes) {
                universidad.estudiantes.remove(estudianteSeleccionado)
                break
            }
        }

        // Eliminar el estudiante de la lista general
        estudiantes.removeAt(indiceEstudiante)

        println("Estudiante eliminado correctamente.")
    } else {
        println("Índice de estudiante no válido.")
    }
}
fun mostrarEstudiantes(estudiantes: List<Estudiante>) {
    println("Lista de Estudiantes:")
    estudiantes.forEachIndexed { index, estudiante ->
        println("$index. ${estudiante.nombre}")
    }
}
// Nueva función para mostrar estudiantes agrupados por universidad
// Nueva función para mostrar estudiantes por universidad
fun mostrarEstudiantesPorUniversidad(universidades: List<Universidad>) {
    println("Estudiantes agrupados por Universidad:")
    for (universidad in universidades) {
        println("-----------------------------------")
        universidad.mostrarInformacion()
        println("Estudiantes:")
        for (estudiante in universidad.estudiantes) {
            println(" - Nombre: ${estudiante.nombre}, Edad: ${estudiante.edad}, " +
                    "Fecha de Ingreso: ${estudiante.fechaIngreso}, Promedio: ${estudiante.promedioCalificaciones}, " +
                    "Activo: ${estudiante.esEstudianteActivo}")
        }
        println("\n")
    }
}

// En la función guardarDatos

fun guardarDatos(datos: List<String>, nombreArchivo: String) {
    File(nombreArchivo).writeText(datos.joinToString("\n"))
}
