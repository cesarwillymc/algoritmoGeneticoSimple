package com.example.algoritmogeneticosimple

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

class ViewModelMain :ViewModel(){
    private val cantPoblacion=10
    private val padres_seleccionados=5
    private val cromosomas_elite=2

    private lateinit var secretpass:List<Int>
    private var poblacion:MutableList<MutableList<Int>> = mutableListOf()
    fun generarPassword():String{
        secretpass=generarAleatoriosRango()
        Log.e("generarPassword", "$secretpass")
        return secretpass.toString()
    }
    private fun generarAleatoriosRango(init:Long=0L,final:Long=9999990999):MutableList<Int>{
        val random=Random.nextLong(init,final)
        return completarbinarios(random.toString(),10).map { it.toInt()- 48 } as MutableList<Int>
    }
    fun generarPoblacionInicial(){
        poblacion.clear()
        var init=0
        while (init<cantPoblacion){
            poblacion.add(generarAleatoriosRango())
            init++
        }
    }
    // Guarda los valores de entrenamiento que tienen un percentaje de mayor coincidencia con la contraseña
    fun entrenamiento(poblacion:MutableList<MutableList<Int>>): MutableList<ModeloFitness> {
        val puntuacionEntrenamiento: MutableList<ModeloFitness> = mutableListOf()
        for (cromosoma in poblacion){
            var cantDatosIguales = 0
            for (x in 0 until cantPoblacion){
                if(secretpass[x]== cromosoma[x])
                    cantDatosIguales++
            }
            val result= ModeloFitness(cromosoma,cantDatosIguales)
            puntuacionEntrenamiento.add(result)
        }
        return puntuacionEntrenamiento
    }
    // Se selecciona los padres que tienen mas coincidendencias y luego filtra a partir de 5 padres
    fun seleccionarPadres(puntuacionEntrenamiento:MutableList<ModeloFitness>): MutableList<ModeloFitness> {

        puntuacionEntrenamiento.sortBy { it.coincidencias }
        puntuacionEntrenamiento.reverse()
        val listaPadres:List<ModeloFitness> = puntuacionEntrenamiento.filterIndexed { index, modeloFitness ->
            (index<padres_seleccionados)
        }
        return listaPadres as MutableList<ModeloFitness>

    }
    // Se selecciona un punto de corte a apartir de los genes a y b
    fun crianza(padre1:List<Int>, padre2:List<Int>): List<Int> {
        val hijo:MutableList<Int> = mutableListOf()
        val genA = Random.nextInt(0,cantPoblacion)
        val genB = Random.nextInt(0,cantPoblacion)
        val startGen= min(genA,genB)
        val endGen = max(genA,genB)
        for (x in 0 until cantPoblacion){
            if (x<startGen || x>endGen){
                hijo.add(padre1[x])
            }else{
                hijo.add(padre2[x])
            }
        }
        return hijo.toList<Int>()
    }

    fun crearHijos(puntuacionEntrenamiento:MutableList<ModeloFitness>): MutableList<MutableList<Int>> {
        val hijos:MutableList<MutableList<Int>> = mutableListOf()
        val numeroNuevosHijos=poblacion.size-cromosomas_elite

        for(x in 0  until cromosomas_elite){
            hijos.add(puntuacionEntrenamiento[x].cromosoma.toMutableList())
        }
        for (i in 0 until numeroNuevosHijos){
            val padre1= puntuacionEntrenamiento[Random.nextInt(0,puntuacionEntrenamiento.size)]
            val padre2= puntuacionEntrenamiento[Random.nextInt(0,puntuacionEntrenamiento.size)]
            hijos.add(crianza(padre1.cromosoma,padre2.cromosoma).toMutableList())
        }
        return hijos
    }
    //Se selecciona cualquier hijo para hacerlo  mutar en un cromosoma
    fun mutacion(hijos:MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
        for(i in 0 until hijos.size){
            if (Random.nextDouble(0.0,1.0)>0.1)
                continue
            else{
                val posicionParaMutar= Random.nextInt(0,cantPoblacion)
                val mutacion= Random.nextInt(0,cantPoblacion)
                hijos[i][posicionParaMutar]= mutacion
            }
        }
        return hijos
    }


    var tiempo = 0

    suspend fun correrAplicacion():String = suspendCancellableCoroutine{
        var generaciones=0
        generarPoblacionInicial()
        while (true){
            try {
                val puntuacionEntrenamiento = entrenamiento(poblacion)
                for (i in puntuacionEntrenamiento){
                    if (i.coincidencias == cantPoblacion){
                        val mensaje = "Se encontro la contraseña en $generaciones generaciones donde \n contraseña: $secretpass \n contraseña encontrada: ${i.cromosoma}"
                        it.resume(mensaje)
                    }
                }
                val padres = seleccionarPadres(puntuacionEntrenamiento)
                val hijos= crearHijos(padres)
                poblacion= mutacion(hijos)
                generaciones++
                if (it.isCompleted){
                    break
                }
            }catch (e:Exception){
                it.resumeWithException(e)
            }
        }

    }


    fun iniciarStudio(): LiveData<Resource<String>> = liveData {
        emit(Resource.Loading())
        try {
            val encontrarRespuesta= correrAplicacion()
            emit(Resource.Success(encontrarRespuesta))
        }catch (e:Exception){
            emit(Resource.Failure(e))
        }
    }
    private fun completarbinarios(binary: String, tamanio:Int): String {
        val cantidaddigitos1 = binary.length
        var arr1 = binary
        if (cantidaddigitos1 < tamanio) {
            for (i in cantidaddigitos1 until tamanio) {
                arr1 = "0$arr1"
            }
        }
        return arr1
    }
}