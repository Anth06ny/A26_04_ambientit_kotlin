package com.example.a26_04_ambientit_kotlin.exo

import com.example.a26_04_ambientit_kotlin.data.remote.WindEntity

class MyLiveData<T>(value:T) {

    var value = value
        set(newValue) {
            field = newValue
            actions.forEach { it?.invoke(newValue) }
        }

    var actions : ArrayList<((T)->Unit)?> = ArrayList()

    fun addAction(action : (T)->Unit) = actions.add(action)
}

fun main() {
    //exo1()
    //exo2()

    var toto = MyLiveData("Coucou")
    toto.value = "hello"
    toto.actions += {
        println(it)
    }
    toto.value = "hello2"

    var toto2 = MyLiveData(WindEntity(5.0))
    toto2.addAction {
        println(it.speed)
    }

    toto2.value.speed = 6.0
    toto2.value = WindEntity(toto2.value.speed + 1)
    toto2.value = toto2.value.copy(speed = toto2.value.speed + 1)


}

data class UserEntity(var name: String, var old: Int)

fun exo2() {
    val compareUsersByName: (UserEntity, UserEntity) -> UserEntity = { u1, u2 -> if (u1.name.lowercase() <= u2.name.lowercase()) u1 else u2 }

    val compareUsersByOld: (UserEntity, UserEntity) -> UserEntity = { u1, u2 -> if (u1.old <= u2.old) u1 else u2 }

    val u1 = UserEntity("Bob", 19)
    val u2 = UserEntity("Toto", 45)
    val u3 = UserEntity("Charles", 26)
    println(compareUsers(u1, u2, u3, compareUsersByName)) // UserEntity(name=Bob old=19)
    println(compareUsers(u1, u2, u3, compareUsersByOld)) // UserEntity(name=Toto old=45)

    compareUsers(u1, u2, u3) { a, b ->
        if (Math.abs(a.old - 30) < Math.abs(b.old - 30)) a else b
    }


}

fun compareUsers(u1: UserEntity, u2: UserEntity, u3: UserEntity, comparator: (UserEntity, UserEntity) -> UserEntity): UserEntity {
    return comparator(comparator(u1, u2), u3)
}

fun exo1() {
    var minToMinHour: ((Int?) -> Pair<Int, Int>?)? = {
        if (it == null) null else Pair(it / 60, it % 60)
    }

    val minToMinHour2: (Int?) -> Pair<Int, Int>? = {
        it?.let { Pair(it / 60, it % 60) }
    }

    println(minToMinHour?.invoke(123))
    println(minToMinHour?.invoke(null))
    minToMinHour = null

}