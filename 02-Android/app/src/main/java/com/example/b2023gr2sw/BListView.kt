package com.example.b2023gr2sw

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView

class BListView : AppCompatActivity() {
    val arreglo = BBaseDatosMemoria.arregloBEntrenador
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blist_view)
        val listview = findViewById<ListView>(R.id.lv_list_view)
        val adaptador = ArrayAdapter(
            this, //este es el contexto
            android.R.layout.simple_list_item_1, // como se va a ver (XML)
            arreglo
        )
        listview.adapter = adaptador
        adaptador.notifyDataSetChanged() // notificar a la interfaz de los cambios

        // escuchar al botón
        val botonAnadirListView = findViewById<Button>(R.id.btn_anadir_list_view)
        botonAnadirListView.setOnClickListener{
            //click, de momento solo va a escuchar al botón
        }


    }
}