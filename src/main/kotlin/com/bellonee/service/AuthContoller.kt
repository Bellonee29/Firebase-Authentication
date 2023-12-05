package com.bellonee.service

import com.bellonee.auth.firebase.FirebaseUserPrincipal
import com.bellonee.auth.TokenProvider
import com.bellonee.data.dto.request.*
import com.bellonee.data.dto.response.AccountResponse
import com.bellonee.data.dto.response.AuthResponse
import com.bellonee.data.dto.response.GeneralResponse
import com.bellonee.data.dto.response.Response
import com.bellonee.exception.BadRequestException
import com.bellonee.exception.UnauthorizedActivityException
import com.bellonee.repository.UserRepository
import com.bellonee.auth.principal.UserPrincipal
import com.bellonee.utils.BaseController
import io.ktor.application.*
import io.ktor.auth.principal
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class DefaultAuthController : BaseController(), AuthController, KoinComponent {

    private val userDao by inject<UserRepository>()
    private val tokenProvider by inject<TokenProvider>()

    override suspend fun idpAuthentication(
        idpAuthenticationRequest: IdpAuthenticationRequest,ctx: ApplicationCall
    ): Response {
        return try {
            val userEmail = ctx.principal<FirebaseUserPrincipal>()?.email
            userDao.findByEmail(userEmail!!)?.let { user ->
                val tokens = tokenProvider.createTokens(user)
                AuthResponse.success(
                    "Sign in successfully",
                    tokens.access_token,
                    tokens.refresh_token
                )
            } ?: userDao.storeUser(userEmail, idpAuthenticationRequest.email, null).let {
                val tokens = tokenProvider.createTokens(it)
                AuthResponse.success(
                    "Sign up successfully",
                    tokens.access_token,
                    tokens.refresh_token,
                )
            }
        } catch (e: BadRequestException) {
            GeneralResponse.failed(e.message)
        }
    }

    override suspend fun signIn(signInRequest: SignInRequest): Response {
        return try {
            validateSignInFieldsOrThrowException(signInRequest)
            userDao.findByEmail(signInRequest.email)?.let {
                //verifyPasswordOrThrowException(signInRequest.password, it)
                val tokens = tokenProvider.createTokens(it)
                AuthResponse.success(
                    "Sign in successfully",
                    tokens.access_token,
                    tokens.refresh_token
                )
            } ?: throw UnauthorizedActivityException("Authentication failed: Invalid credentials")
        } catch (e: BadRequestException) {
            GeneralResponse.failed(e.message)
        } catch (e: UnauthorizedActivityException) {
            GeneralResponse.unauthorized(e.message)
        }
    }

    override suspend fun signUp(signUpRequest: SignUpRequest): Response {
        return try {
            validateSignUpFieldsOrThrowException(signUpRequest)
            verifyEmail(signUpRequest.email)
            val encryptedPassword = getEncryptedPassword(signUpRequest.password)
            val user = userDao.storeUser(signUpRequest.fullName,signUpRequest.email, encryptedPassword)
            val tokens = tokenProvider.createTokens(user)
            AuthResponse.success(
                "Sign up successfully",
                tokens.access_token,
                tokens.refresh_token
            )
        } catch (e: BadRequestException) {
            GeneralResponse.failed(e.message)
        }
    }


    override suspend fun getAccountById(ctx: ApplicationCall): Response {
        return try {
            val userId = ctx.principal<UserPrincipal>()?.user?.id
            userDao.findByID(userId!!)?.let {
                AccountResponse.success("User found", it.id, it.fullName, it.email)
            } ?: throw UnauthorizedActivityException("User do not exist")
        } catch (e: UnauthorizedActivityException) {
            GeneralResponse.notFound(e.message)
        }
    }


}

interface AuthController {
    suspend fun idpAuthentication(
        idpAuthenticationRequest: IdpAuthenticationRequest,
        ctx: ApplicationCall
    ): Response

    suspend fun signIn(signInRequest: SignInRequest): Response
    suspend fun signUp(signUpRequest: SignUpRequest): Response
    suspend fun getAccountById(ctx: ApplicationCall): Response

}







