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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

//Suspend sera expliqué dans le chapitre des coroutines
suspend fun main() {
    val user = KtorUserApi.loadUser()
    println(
        """
       Il s'appelle ${user.name} pour le contacter :
       Phone : ${user.coord?.phone ?: "-"}
       Mail : ${user.coord?.mail ?: "-"}
   """.trimIndent()
    )

    println("---------")

    for(user in KtorUserApi.loadUsers()) {
        println(
            """
       Il s'appelle ${user.name} pour le contacter :
       Phone : ${user.coord?.phone ?: "-"}
       Mail : ${user.coord?.mail ?: "-"}
   """.trimIndent()
        )
    }

    KtorUserApi.close()
}

object KtorUserApi {
    private const val API_URL =
        "https://www.amonteiro.fr/api/randomuser"

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

    //GET Le JSON reçu sera parser en List<UserDTO>,
    //Crash si le JSON ne correspond pas
    suspend fun loadUser(): UserDTO {
        val response = client.get(API_URL)
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body()
    }

    suspend fun loadUsers(): List<UserDTO> {
        val response = client.get(API_URL +"s")
        if (!response.status.isSuccess()) {
            throw Exception("Erreur API: ${response.status} - ${response.bodyAsText()}")
        }

        return response.body()
    }


    //Ferme le Client mais celui ci ne sera plus utilisable. Uniquement pour le main
    fun close() = client.close()
}

//DATA CLASS

//Possible qu'il y ait besoin de cette annotation en fonction du compilateur
@Serializable //KotlinX impose cette annotation
data class UserDTO(
    val age: Int,
    val name: String,
    val coord: CoordDTO?
)

@Serializable //KotlinX impose cette annotation
data class CoordDTO(
    val mail: String?,
    val phone: String?
)

