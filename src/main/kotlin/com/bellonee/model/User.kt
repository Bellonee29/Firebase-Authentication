package com.bellonee.model

import com.bellonee.entity.EntityUser
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class User(
    val id: Int,
    val fullName: String,
    val email: String,
    @Contextual
    val createAt: LocalDateTime
) {
    companion object {
        fun fromEntity(entity: EntityUser) = User(
            entity.id.value,
            entity.fullName,
            entity.email,
            entity.createAt
        )
    }
}


