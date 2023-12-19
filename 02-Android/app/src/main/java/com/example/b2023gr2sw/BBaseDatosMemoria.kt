package com.example.b2023gr2sw

class BBaseDatosMemoria {
    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador.add(BEntrenador(1, "Alisson", "a@a.com"))
            arregloBEntrenador.add(BEntrenador(2, "Estefania", "b@b.com"))
            arregloBEntrenador.add(BEntrenador(3, "Alexander", "c@c.com"))
        }
    }
}