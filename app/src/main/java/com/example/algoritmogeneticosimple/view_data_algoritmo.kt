package com.example.algoritmogeneticosimple

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_view_data_algoritmo.*
import java.util.*

class view_data_algoritmo : AppCompatActivity() {
    private var valor: ArrayList<String>? = null
    private var newValorStringNumber: ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_data_algoritmo)
        newValorStringNumber = ArrayList()
        valor = intent.extras!!.getStringArrayList("data")
        for (i in valor!!.indices) {
            newValorStringNumber!!.add(valor!![i] + " : " + valor!![i].toInt(2))
        }
        val arrayAdapter =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, newValorStringNumber!!)
        list_item!!.adapter = arrayAdapter
    }
}