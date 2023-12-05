package com.bellonee.data.databaseConnectivities


import com.bellonee.data.tables.Users
import com.bellonee.data.tables.Tokens
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.newFixedThreadPoolContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import kotlin.coroutines.CoroutineContext
import org.koin.core.component.KoinComponent

@OptIn(DelicateCoroutinesApi::class)
class DatabaseConnection : DatabaseProviderContract, KoinComponent {

    private val dispatcher: CoroutineContext

    init {
        dispatcher = newFixedThreadPoolContext(5, "database-pool")
    }

    override fun init() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(Users)
            SchemaUtils.create(Tokens)
        }
    }
    private fun hikari(): HikariDataSource {
        HikariConfig().run {
            driverClassName = driverClass
            jdbcUrl = "jdbc:postgresql://${host}:${port}/${dbname}"
            username = user
            password = dbpassword
            isAutoCommit = false
            maximumPoolSize = 5
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
            return HikariDataSource(this)
        }
    }

    companion object DatabaseConfig {
        const val driverClass = "org.postgresql.Driver"
        const val host = "localhost"
        const val port = 5432
        const val dbname = "firebaseInt"
        const val user = "postgres"
        const val dbpassword = "Bellonee_1994"
    }
}

interface DatabaseProviderContract {
    fun init()
}
