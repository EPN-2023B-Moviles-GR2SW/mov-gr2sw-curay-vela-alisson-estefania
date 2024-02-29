package com.example.exameniib.database

import com.example.exameniib.MainActivity
import com.example.exameniib.modelo.Universidad
import com.google.firebase.firestore.FirebaseFirestore

class FStoreUniversidad {
    private val db = FirebaseFirestore.getInstance()
    private val universidadesCollection = db.collection("Universidades")

    fun crearUniversidad(nuevaUniversidad: Universidad){
        val datosUniversidad = hashMapOf(
            "nombre" to nuevaUniversidad.nombre,
            "ubicacion" to nuevaUniversidad.ubicacion,
            "fechaFundacion" to nuevaUniversidad.fechaFundacion,
            "areaCobertura" to nuevaUniversidad.areaCobertura,
        )
        universidadesCollection
            .add(datosUniversidad)
            .addOnSuccessListener { documentReference ->
                val universidadId = documentReference.id
                nuevaUniversidad.id = universidadId
            }
            .addOnFailureListener {  }
    }

    fun eliminarUniversidad(){

        universidadesCollection.document(MainActivity.arrayUniversidad[MainActivity.posicionUniversidad].id)
            .delete()
            .addOnSuccessListener {
                MainActivity.arrayUniversidad.removeAt(MainActivity.posicionUniversidad)
            }
            .addOnFailureListener{  }
    }
    fun editarUniversidad(nuevaUniversidad: Universidad){
        val datosUniversidad = hashMapOf(
            "nombre" to nuevaUniversidad.nombre,
            "ubicacion" to nuevaUniversidad.ubicacion,
            "fechaFundacion" to nuevaUniversidad.fechaFundacion,
            "areaCobertura" to nuevaUniversidad.areaCobertura,
        )
        universidadesCollection.document(MainActivity.arrayUniversidad[MainActivity.posicionUniversidad].id)
            .set(datosUniversidad)
            .addOnSuccessListener {  }
            .addOnFailureListener {  }
    }
}