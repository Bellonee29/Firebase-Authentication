package com.bellonee.plugins

import com.auth0.jwt.interfaces.JWTVerifier
import com.bellonee.auth.JWTController
import com.bellonee.auth.PasswordEncryptor
import com.bellonee.auth.PasswordEncryptorContract
import com.bellonee.auth.TokenProvider

import com.bellonee.di.DaoModule
import com.bellonee.service.di.ControllerModule
import io.ktor.application.*
import org.koin.core.annotation.KoinReflectAPI
import org.koin.dsl.module
import org.koin.ktor.ext.Koin
import org.koin.logger.slf4jLogger

@KoinReflectAPI
fun Application.configureKoin() {

    install(feature = Koin) {
        slf4jLogger(level = org.koin.core.logger.Level.ERROR) //This params are the workaround itself
        modules(
            module {
                single<JWTVerifier> { JWTController.verifier }
                single<TokenProvider> { JWTController }
                single<PasswordEncryptorContract> { PasswordEncryptor }
            },
            DaoModule.koinBeans,
            ControllerModule.koinBeans
        )
    }
}
