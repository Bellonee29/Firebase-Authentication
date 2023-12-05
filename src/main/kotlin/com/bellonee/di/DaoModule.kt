package com.bellonee.di


import com.bellonee.data.databaseConnectivities.DatabaseConnection
import com.bellonee.data.databaseConnectivities.DatabaseProviderContract
import com.bellonee.data.tables.Tokens
import com.bellonee.data.tables.Users
import com.bellonee.repository.TokenRepository
import com.bellonee.repository.UserRepository
import org.koin.dsl.module

object DaoModule {
    val koinBeans = module {
        single<TokenRepository> { Tokens }
        single<UserRepository> { Users }
        single<DatabaseProviderContract> { DatabaseConnection() }
    }
}