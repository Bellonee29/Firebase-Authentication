package com.bellonee.data.dto.request
import kotlinx.serialization.Serializable

@Serializable
data class IdpAuthenticationRequest(
    val email: String
)