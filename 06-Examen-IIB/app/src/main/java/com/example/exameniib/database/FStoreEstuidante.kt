package com.example.exameniib.database

import com.example.exameniib.ListaEstudiantes
import com.example.exameniib.MainActivity
import com.example.exameniib.modelo.Estudiante
import com.google.firebase.firestore.FirebaseFirestore

class FStoreEstuidante {
    private val db = FirebaseFirestore.getInstance()
    private val estudiantesCollection = db.collection("Universidades")
        .document(MainActivity.arrayUniversidad[MainActivity.posicionUniversidad].id)
        .collection("estudiantes")
    fun crearEstudiante(nuevoEstudiante: Estudiante){
        val datosEstudiante = hashMapOf(
            "nombre" to nuevoEstudiante.nombre,
            "edad" to nuevoEstudiante.edad,
            "fechaIngreso" to nuevoEstudiante.fechaIngreso,
            "estado" to nuevoEstudiante.estado,
            "promedioCalificaciones" to nuevoEstudiante.promedioCalificaciones
        )
        estudiantesCollection
            .add(datosEstudiante)
            .addOnSuccessListener { documentReference ->
                val estudianteId = documentReference.id
                nuevoEstudiante.id = estudianteId
            }
            .addOnFailureListener {  }

    }
    fun eliminarEstudiante(){

        estudiantesCollection.document(ListaEstudiantes.arrayEstudiantes[ListaEstudiantes.posicionElementoEstudiantes].id)
            .delete()
            .addOnSuccessListener {
                MainActivity.arrayUniversidad[MainActivity.posicionUniversidad]
                    .listaEstudiantes.removeAt(ListaEstudiantes.posicionElementoEstudiantes)

                ListaEstudiantes.arrayEstudiantes.removeAt(ListaEstudiantes.posicionElementoEstudiantes)
            }
            .addOnFailureListener{  }
    }
    fun editarEstudiante(nuevoEstudiante: Estudiante){
        val datosEstudiante = hashMapOf(
            "nombre" to nuevoEstudiante.nombre,
            "edad" to nuevoEstudiante.edad,
            "fechaIngreso" to nuevoEstudiante.fechaIngreso,
            "estado" to nuevoEstudiante.estado,
            "promedioCalificaciones" to nuevoEstudiante.promedioCalificaciones
        )
        estudiantesCollection.document(
            MainActivity.arrayUniversidad[MainActivity.posicionUniversidad]
                .listaEstudiantes[ListaEstudiantes.posicionElementoEstudiantes].id
        )
            .set(datosEstudiante)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }
}