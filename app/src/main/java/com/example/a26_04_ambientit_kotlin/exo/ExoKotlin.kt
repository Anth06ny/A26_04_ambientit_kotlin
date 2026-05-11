package com.example.a26_04_ambientit_kotlin.exo

import kotlin.random.Random

var v2 :  String?   = "dfghjkl"

fun main() {

    println(scanNumber("Question :"))

}

fun scanNumber(textToPrint: String) = scanText(textToPrint).toIntOrNull() ?: 0

//Exokotlin.kt
fun scanText(textToPrint: String): String {
    println(textToPrint)
    //Si ce qui est à gauche de l'Elvis operator est nulle, j'applique ce qui est à droite
    return readlnOrNull() ?: "-"
}

fun boulangerie(nbCroi:Int = 0, nbBag:Int =0, nbSand:Int =0 ) =
    nbCroi * PRICE_CROISSANT + nbBag * PRICE_BAGUETTE + nbSand * PRICE_SANDWITCH


fun toto(){
        //plein de requete
}