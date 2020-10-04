package com.example.algoritmogeneticosimple

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import java.util.ArrayList

/*
iniciar_variables()
        poblacion_button!!.setOnClickListener {
            poblacion = poblacion_edit_text!!.text.toString().toInt()
            generacion_edit_text!!.isEnabled = true
            generacion_button!!.isEnabled = true
            poblacion_button!!.visibility = View.GONE
            poblacion_edit_text!!.isEnabled = false
            generacion_edit_text!!.filters = arrayOf<InputFilter>(InputFilterMinMax(0, 500))
            generacion_edit_text!!.hint = "Defina cantidad de Generaciones<500"
        }
        cromosomas_button!!.setOnClickListener{
            cromosomas = cromosomas_edit_text!!.text.toString().toInt()
            individuos_edit_text!!.isEnabled = true
            individuos_button!!.isEnabled = true
            cromosomas_button!!.visibility = View.GONE
            cromosomas_edit_text!!.isEnabled = false
            max = dos_elevadoal(cromosomas) - 1
            individuos_edit_text!!.hint = "Min: 0 Max: $max"
            individuos_edit_text!!.filters = arrayOf<InputFilter>(InputFilterMinMax(0, max))
            individuos = ArrayList()
            individuos_button!!.text = "Nº " + (contador_individuos + 1)
            contador_individuos++
        }
        generacion_button!!.setOnClickListener{
            generacion = generacion_edit_text!!.text.toString().toInt()
            cromosomas_edit_text!!.isEnabled = true
            cromosomas_button!!.isEnabled = true
            generacion_button!!.visibility = View.GONE
            generacion_edit_text!!.isEnabled = false
        }
        indice_button!!.setOnClickListener{
            indice = indice_edit_text!!.text.toString().toInt()
            nexgeneration = ArrayList()
            Progress_bar!!.visibility = View.VISIBLE
            realizarMutacion()
        }
        individuos_button!!.setOnClickListener{
            if (contador_individuos < poblacion) {
                individuos!!.add(
                    completarbinarios(
                        decimalToBinary(
                            individuos_edit_text!!.text.toString().toInt()
                        )
                    )
                )
                individuos_button!!.text = "Nº " + (contador_individuos + 1)
                contador_individuos++
                datos = datos + "+" + individuos_edit_text!!.text.toString()
                individuos_edit_text!!.setText("")
            } else {
                individuos!!.add(
                    completarbinarios(
                        decimalToBinary(
                            individuos_edit_text!!.text.toString().toInt()
                        )
                    )
                )
                individuos_button!!.visibility = View.GONE
                individuos_edit_text!!.isEnabled = false
                indice_button!!.isEnabled = true
                indice_edit_text!!.isEnabled = true
                indice_edit_text!!.filters = arrayOf<InputFilter>(InputFilterMinMax(0, cromosomas - 1))
                indice_edit_text!!.hint = "Max: $cromosomas"
            }
        }
 */
private var contador_individuos = 0
var max = 0
private var datos = ""





private var poblacion = 0
private var generacion = 0
private var cromosomas = 0
private var individuos: ArrayList<String>? = null
var nexgeneration: ArrayList<String>? = null
private var indice = 0
/*private fun iniciar_variables() {
    poblacion_edit_text!!.isEnabled = true
    poblacion_button!!.isEnabled = true
    //
    cromosomas_button!!.isEnabled = false
    cromosomas_edit_text!!.isEnabled = false
    //
    generacion_button!!.isEnabled = false
    generacion_edit_text!!.isEnabled = false
    //
    indice_button!!.isEnabled = false
    indice_edit_text!!.isEnabled = false
    //
    individuos_button!!.isEnabled = false
    individuos_edit_text!!.isEnabled = false
    //
}*/

private fun decimalToBinary(n: Int): String {
    return if (n == 0 || n == 1) {
        n.toString() + ""
    } else {
        decimalToBinary(n / 2) + n % 2
    }
}



private fun Context.realizarMutacion() {
    for (i in 0 until generacion) {
        seleccion_cruze()
        val maximosgenes = insertar()
        individuos = nuevos_genes(maximosgenes)
        nexgeneration!!.clear()
    }
    //Progress_bar!!.visibility = View.GONE
    val intent = Intent(this, view_data_algoritmo::class.java)
    intent.putExtra("data", individuos)
    //startActivity(intent)
}

private fun nuevos_genes(maximosgenes: ArrayList<String>): ArrayList<String> {
    if (maximosgenes.size == poblacion) {
        return maximosgenes
    } else if (maximosgenes.size == poblacion - 1) {
        maximosgenes.add(maximosgenes[0])
        return maximosgenes
    } else {
//            if(maximosgenes.size()%2==0){
        var cantidad_ini = 0
        if (maximosgenes.size != 0) {
            cantidad_ini = maximosgenes.size
        }
        var i = maximosgenes.size - 1
        var a = 0
        while (i < poblacion - 1) {
            if (cantidad_ini > a) {
                maximosgenes.add(maximosgenes[a])
            } else {
                a = 0
                maximosgenes.add(maximosgenes[a])
            }
            i++
            a++
        }
        //            }
//              else{  for (int i=maximosgenes.size()-1, a=0,b=maximosgenes.size()-1;i<poblacion-1;a++,b--,i+=2){
//
//                    if(((maximosgenes.size()-1)/2)==a){
//                        maximosgenes.add(maximosgenes.get(a));
//                        a=0;InputFilterMinMax
//                        b=maximosgenes.size()-1;
//                    }else{
//                        maximosgenes.add(maximosgenes.get(a));
//                        maximosgenes.add(maximosgenes.get(b));
//                    }
//                }
//            }
    }
    individuos!!.clear()
    return maximosgenes
}

var porcentaje = 0.0
var maxsetentaPorCiento = 0.0
private fun insertar(): ArrayList<String> {
    val enteros = ArrayList<Int>()
    var sumatotal = 0
    if (porcentaje < 0.9) {
        porcentaje += 0.1
    }
    maxsetentaPorCiento = max * porcentaje
    val nuevosMaximos = ArrayList<String>()
    for (i in 0 until poblacion) {
        val a = nexgeneration!![i].toInt(2)
        Log.e("cruze", nexgeneration!![i])
        Log.e("decimal", a.toString() + "")
        enteros.add(a)
        sumatotal = sumatotal + a
    }
    for (i in 0 until poblacion) {
        if (maxsetentaPorCiento < enteros[i]) {
            nuevosMaximos.add(nexgeneration!![i])
        }
    }
    if (nuevosMaximos.size == 0) {
        porcentaje = -0.1
        insertar()
    }
    return nuevosMaximos
}

private fun seleccion_cruze() {
    var i = 0
    var a = poblacion - 1
    while (i < poblacion / 2) {
        cruze(individuos!![i].toCharArray(), individuos!![a].toCharArray())
        i++
        a--
    }
}

private fun cruze(array1: CharArray, array2: CharArray) {
    var temp: Char
    var i = 0
    var a = cromosomas - indice
    while (i < indice) {
        temp = array1[i]
        array1[i] = array2[a]
        array2[a] = temp
        i++
        a++
    }
    nexgeneration!!.add(String(array1))
    nexgeneration!!.add(String(array2))
}



private fun dos_elevadoal(maximo: Int): Int {
    var a = 1
    for (i in 0 until maximo) {
        a = a * 2
    }
    return a
}