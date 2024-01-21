package com.example.examenib

class BDMemoria {
    companion object {
        val listaUniversidades = arrayListOf<Universidad>()

        init {

            listaUniversidades.add(
                Universidad(
                    "EPN",
                    "Quito",
                    "1869-08-27",
                    52.000,
                    arrayListOf(
                        Estudiante("Alisson Curay",24,"2018-12-01",true,9.5),
                        Estudiante("Estafania Vela",21,"2020-10-09",true,8.5)
                    )
                )
            )
            listaUniversidades.add(
                Universidad(
                    "ESPE",
                    "Latacunga",
                    "1922-06-19",
                    42.000,
                    arrayListOf(
                        Estudiante("María Perez",18,"2017-11-02",true,10.0),
                        Estudiante("Juanita Sarsoza",20,"2022-03-19",false,7.5)
                    )
                )
            )
        }
    }
}