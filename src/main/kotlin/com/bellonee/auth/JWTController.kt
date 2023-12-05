package com.bellonee.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.bellonee.data.dto.response.TokenResponse
import com.bellonee.model.User
import java.util.*

object JWTController : TokenProvider {

    private const val secret = "bkFwb2xpdGE2OTk5"
    private const val issuer = "bkFwb2xpdGE2OTk5"
    private const val validityInMs: Long = 1200000L // 20 Minutes
    private const val refreshValidityInMs: Long = 3600000L * 24L * 30L // 30 days
    private val algorithm = Algorithm.HMAC512(secret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    override fun verifyToken(token: String): Int? {
        return verifier.verify(token).claims["userId"]?.asInt()
    }

    override fun getTokenExpiration(token: String): Date {
        return verifier.verify(token).expiresAt
    }

    /**
     * Produce token and refresh token for this combination of User and Account
     */
    override fun createTokens(user: User) = TokenResponse(
        createAccessToken(user, getTokenExpiration()),
        createRefreshToken(user, getTokenExpiration(refreshValidityInMs))
    )

    override fun verifyTokenType(token: String): String {
        return verifier.verify(token).claims["tokenType"]!!.asString()
    }

    private fun createAccessToken(user: User, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("userId", user.id)
        .withClaim("tokenType", "accessToken")
        .withExpiresAt(expiration)
        .sign(algorithm)

    private fun createRefreshToken(user: User, expiration: Date) = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withClaim("userId", user.id)
        .withClaim("tokenType", "refreshToken")
        .withExpiresAt(expiration)
        .sign(algorithm)

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getTokenExpiration(validity: Long = validityInMs) = Date(System.currentTimeMillis() + validity)
}
interface TokenProvider {
    fun createTokens(user: User): TokenResponse
    fun verifyTokenType(token: String): String
    fun verifyToken(token: String): Int?
    fun getTokenExpiration(token: String): Date
}


