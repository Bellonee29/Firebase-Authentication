package com.bellonee.data.tables

import com.bellonee.entity.EntityUser
import com.bellonee.model.User
import com.bellonee.repository.UserRepository
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.LocalDateTime

object Users : IntIdTable(), UserRepository {
    val fullName = varchar("full_name", length = 256)
    val email = text("email")
    val password = text("password").nullable()
    val createAt = datetime("createAt").clientDefault { LocalDateTime.now() }

    override suspend fun storeUser(fullName: String, email: String, password: String?): User =
        newSuspendedTransaction(Dispatchers.IO) {
            EntityUser.new {
                this.fullName = fullName
                this.email = email
                this.password = password

            }.let {
                User.fromEntity(it)
            }
        }


    override suspend fun findByID(userId: Int): User? =
        newSuspendedTransaction(Dispatchers.IO) {
            EntityUser.findById(userId)
        }?.let {
            User.fromEntity(it)
        }

    override suspend fun findByEmail(email: String): User? =
        newSuspendedTransaction(Dispatchers.IO) {
            EntityUser.find {
                (Users.email eq email)
            }.firstOrNull()
        }?.let { User.fromEntity(it) }

    override suspend fun isEmailAvailable(email: String): Boolean {
        return newSuspendedTransaction(Dispatchers.IO) {
            EntityUser.find { Users.email eq email }.firstOrNull()
        } == null
    }


}
