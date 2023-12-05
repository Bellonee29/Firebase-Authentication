package com.bellonee

import com.auth0.jwt.interfaces.JWTVerifier
import com.bellonee.auth.firebase.FirebaseAdmin
import com.bellonee.data.databaseConnectivities.DatabaseProviderContract
import com.bellonee.plugins.*
import com.bellonee.repository.UserRepository
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.koin.core.annotation.KoinReflectAPI
import org.koin.ktor.ext.inject

fun main() {
    embeddedServer(Netty, port = 8085, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

@OptIn(KoinReflectAPI::class)
fun Application.module() {

    val databaseProvider by inject<DatabaseProviderContract>()
    val userDao by inject<UserRepository>()
    val jwtVerifier by inject<JWTVerifier>()

//    val client = HttpClient(CIO) {
//        install(JsonFeature) {
//            serializer = KotlinxSerializer()
//        }
//    }
//    val apiKey = environment.config.property("onesignal.apiKey").getString()

    configureKoin()
    configureMonitoring()
    configureSerialization()
    configureSecurity(userDao, jwtVerifier)
    configureRouting()

    // initialize database
    databaseProvider.init()

    // initialize Firebase Admin SDK
    FirebaseAdmin.init()
}
