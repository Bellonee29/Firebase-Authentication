package com.bellonee.utils


import com.bellonee.data.dto.request.SignInRequest
import com.bellonee.data.dto.request.SignUpRequest
import com.bellonee.data.dto.request.UpdatePasswordRequest
import com.bellonee.data.dto.response.AuthResponse
import com.bellonee.exception.BadRequestException
import com.bellonee.exception.UnauthorizedActivityException
import com.bellonee.model.User
import com.bellonee.repository.TokenRepository
import com.bellonee.repository.UserRepository
import com.bellonee.auth.PasswordEncryptorContract
import com.bellonee.auth.TokenProvider
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.text.SimpleDateFormat

abstract class BaseController : KoinComponent {

    private val userDao by inject<UserRepository>()
    private val refreshTokensDao by inject<TokenRepository>()
    private val passwordEncryption by inject<PasswordEncryptorContract>()
    private val tokenProvider by inject<TokenProvider>()
    private val simpleDateFormat = SimpleDateFormat("'Date: 'yyyy-MM-dd' Time: 'HH:mm:ss")

    internal fun validateSignInFieldsOrThrowException(
        signInRequest: SignInRequest
    ) {
        val message = when {
            (signInRequest.email.isBlank() or (signInRequest.password.isBlank())) -> "Credentials fields should not be blank"
            (!signInRequest.email.isEmailValid()) -> "Email invalid"
            (signInRequest.password.length !in (8..50)) -> "Password should be of min 8 and max 50 character in length"
            else -> return
        }

        throw BadRequestException(message)
    }

    internal fun validateSignUpFieldsOrThrowException(
        signUpRequest: SignUpRequest
    ) {
        val message = when {
            (signUpRequest.fullName.isBlank()
                    or signUpRequest.email.isBlank()
                    or (signUpRequest.password.isBlank())
                    or (signUpRequest.confirmPassword.isBlank())) -> "Fields should not be blank"
            (!signUpRequest.email.isEmailValid()) -> "Email invalid"
            (signUpRequest.password.length !in (8..50)) -> "Password should be of min 8 and max 50 character in length"
            (signUpRequest.confirmPassword.length !in (8..50)) -> "Password should be of min 8 and max 50 character in length"
            (signUpRequest.password != signUpRequest.confirmPassword) -> "Passwords do not match"
            else -> return
        }

        throw BadRequestException(message)
    }


    internal suspend fun verifyEmail(email: String) {
        if (!userDao.isEmailAvailable(email)) {
            throw BadRequestException("Authentication failed: Email is already taken")
        }
    }

    internal fun getEncryptedPassword(password: String): String {
        return passwordEncryption.encryptPassword(password)
    }

    internal fun getConvertedTokenExpirationTime(token: String): String {
        val expirationTime = tokenProvider.getTokenExpiration(token)
        return simpleDateFormat.format(expirationTime)
    }

    internal fun getConvertedCurrentTime(): String = simpleDateFormat.format((System.currentTimeMillis()))

    internal fun getTokenType(token: String): String {
        return tokenProvider.verifyTokenType(token)
    }



}