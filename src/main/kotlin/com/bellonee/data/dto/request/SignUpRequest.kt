package com.bellonee.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class SignUpRequest(
    val email: String,
    val fullName: String,
    val password: String,
    val confirmPassword: String,
)
