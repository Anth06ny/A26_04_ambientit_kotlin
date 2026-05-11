package com.example.a26_04_ambientit_kotlin.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

//Suspend sera expliqué dans le chapitre des coroutines
suspend fun main() {
//    val res = KtorWeatherApi.loadWeathers("Nice").joinToString(separator = "\n") {
//        it.getResume()
//    }
//
//    print(res)

    val myFlow = KtorWeatherApi.loadWeathersWithFlow("Nice", "Toulouse", "", "paris")

    myFlow
        .filter { it.wind.speed < 3 }
        .map {
            it.name + " wind : " + it.wind.speed
        }
        .catch { println("Ca a planté") }
        .collect {
            println(it)
        }

}

object KtorWeatherApi {
    private const val API_URL =
        "https://api.openweathermap.org/data/2.5/find?appid=b80967f0a6bd10d23e44848547b26550&units=metric&lang=fr&q="

    //Création et réglage du client
    private val client = HttpClient {
        install(Logging) {
            //(import io.ktor.client.plugins.logging.Logger)
            logger = object : Logger {
                override fun log(message: String) {
                    println(message)
                }
            }
            level = LogLevel.INFO  // TRACE, HEADERS, BODY, etc.
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
        //engine { proxy = ProxyBuilder.http("monproxy:1234") }
    }

    //GET Le JSON reçu sera parser en List<WeatherDTO>,
    //Crash si le JSON ne correspond pas
    suspend fun loadWeathers(cityName: String): List<WeatherEntity> {

        if (cityName.length <= 3) {
            throw Exception("Il faut 3 caractères minimum")
        }

        val response = client.get(API_URL + cityName)
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body<WeatherAPIResult>().list.onEach {
            it.weather.forEach {
                it.icon = "https://openweathermap.org/img/wn/${it.icon}@4x.png"
            }
        }
    }

    fun loadWeathersWithFlow(vararg cities: String) = flow<WeatherEntity> {
        cities.forEach { cityName ->
            val list = loadWeathers(cityName)
            list.forEach {
                emit(it)
            }
            delay(1000)
        }
    }

    //Ferme le Client mais celui ci ne sera plus utilisable. Uniquement pour le main
    fun close() = client.close()

    //Avancés : Exemple avec Flow
    //fun loadWeathersFlow() = flow<List<WeatherDTO>> {
    //    emit(client.get(API_URL).body())
    //}
}

/* -------------------------------- */
// WEATHER
/* -------------------------------- */
@Serializable
data class WeatherAPIResult(val list: List<WeatherEntity>)

@Serializable
data class WeatherEntity(
    val id: Int, val name: String, var main: TempEntity,
    var weather: List<DescriptionEntity>,
    var wind: WindEntity
) {

    fun getResume() = """
            Il fait ${main.temp}° à $name (id=$id) avec un vent de ${wind.speed} m/s
            -Description : ${weather.firstOrNull()?.description ?: "-"}
            -Icône : ${weather.firstOrNull()?.icon ?: "-"}
        """.trimIndent()

}

@Serializable
data class TempEntity(var temp: Double)

@Serializable
data class DescriptionEntity(var description: String, var icon: String)

@Serializable
data class WindEntity(var speed: Double)