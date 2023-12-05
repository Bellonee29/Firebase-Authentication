package com.bellonee.repository

import com.bellonee.model.User


interface UserRepository {
    suspend fun storeUser(fullName: String, email: String, password: String?): User
    //suspend fun storeId(email: String, username: String, password: String?): User
    suspend fun findByID(userId: Int): User?
    suspend fun findByEmail(email: String): User?
    suspend fun isEmailAvailable(email: String): Boolean
}
