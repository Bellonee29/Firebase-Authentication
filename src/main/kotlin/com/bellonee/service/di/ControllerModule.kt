package com.bellonee.service.di

import com.bellonee.service.AuthController
import com.bellonee.service.DefaultAuthController
import org.koin.dsl.module

object ControllerModule {
    val koinBeans = module {
        single<AuthController> { DefaultAuthController() }
    }
}