package com.example.a26_04_ambientit_kotlin.exo

import java.util.Random

fun main() {

    var chaine = "toto"

    println("tot" + "o".random() == chaine)

}

class RandomName {
    private val list = arrayListOf("Toto", "Tata", "Titi")
    private var oldName = ""

    fun add(name: String?) = if (!name.isNullOrBlank() && name !in list) list.add(name) else false

    fun next() = list.random()

    fun addAll(vararg name: String) {
        for (n in name) {
            add(n)
        }
    }

    fun addAll2(vararg name: String) = name.forEach { add(it) }

    fun nextDiff(): String {
        var newValue = next()
        while (newValue == oldName) {
            newValue = next()
        }

        oldName = newValue
        return newValue
    }

    fun nextDiff2(): String {
        oldName = list.filter { it != oldName }.random()
        return oldName
    }

    fun nextDiff3() = list.filter { it != oldName }.random().also { oldName = it }

    fun next2() = Pair(nextDiff(), nextDiff())


}


class ThermometerEntity(val min: Int, val max: Int, value: Int) {
    var value = value.coerceIn(min, max)
        set(newValue) {
            field = newValue.coerceIn(min, max)
        }

    companion object {
        fun getCelsiusThermometer() = ThermometerEntity(-30, 50, 0)
        fun getFahrenheitThermometer() = ThermometerEntity(20, 120, 32)
    }
}

class PrintRandomIntEntity(val max: Int) {
    private val random = Random()

    init {
        println(random.nextInt(max))
        println(random.nextInt(max))
        println(random.nextInt(max))
    }

    constructor() : this(100) {
        println(random.nextInt(max))
    }
}

class HouseEntity(var color: String, width: Int, length: Int) {
    var area = width * length
}

data class CarEntity(var marque: String = "", var model: String = "") {
    var color = ""
}