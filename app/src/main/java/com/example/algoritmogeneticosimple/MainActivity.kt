package com.example.algoritmogeneticosimple

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var viewModelMain: ViewModelMain
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelMain = run {
            ViewModelProvider(this).get(ViewModelMain::class.java)
        }
        setContentView(R.layout.activity_main)

        generar_button.setOnClickListener {
            contraseña_text.setText(viewModelMain.generarPassword())
            empezar_analisis()
        }

    }

    private fun empezar_analisis() {
        viewModelMain.iniciarStudio().observe(this, Observer {
            when(it){
                is Resource.Loading->{
                    progressBar.visibility= View.VISIBLE
                }
                is Resource.Success->{
                    progressBar.visibility= View.GONE
                    textView3.text= it.data
                }
                is Resource.Failure->{
                    progressBar.visibility= View.GONE
                    Log.e("mensaje", it.exception.message!!)
                    Toast.makeText(this@MainActivity,"${it.exception.message!!}",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

}