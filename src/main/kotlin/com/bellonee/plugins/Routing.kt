package com.bellonee.plugins


import com.bellonee.router.authApi
import io.ktor.application.*
import io.ktor.routing.*


fun Application.configureRouting() {
    routing {
        authApi()
    }
}
