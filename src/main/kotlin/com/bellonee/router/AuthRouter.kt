package com.bellonee.router


import com.bellonee.data.dto.request.*
import com.bellonee.data.dto.response.generateHttpResponse
import com.bellonee.service.AuthController
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.koin.ktor.ext.inject


fun Route.authApi() {

    val authController by inject<AuthController>()

    route("/auth") {

        authenticate {

            route("/idp") {

                // This is the single endpoint for both signing and registration using firebase
                post("/google") {
                    val idpAuthenticationRequest = call.receive<IdpAuthenticationRequest>()
                    val idpAuthenticationResponse = authController.idpAuthentication(idpAuthenticationRequest, this.context)
                    val response = generateHttpResponse(idpAuthenticationResponse)
                    call.respond(response.code, response.body)
                }
            }
        }
            // This is the signIn for are user using JWT
        post("/signin") {
            val signInRequest = call.receive<SignInRequest>()
            val signInResponse = authController.signIn(signInRequest)
            val response = generateHttpResponse(signInResponse)
            call.respond(response.code, response.body)
        }
            // This is the signUp for User using JWT
        post("/signup") {
            val signUpRequest = call.receive<SignUpRequest>()
            val signUpResponse = authController.signUp(signUpRequest)
            val response = generateHttpResponse(signUpResponse)
            call.respond(response.code, response.body)
        }


        route("/account") {

            authenticate("jwt") {

                get {
                    val accountResponse = authController.getAccountById(this.context)
                    val response = generateHttpResponse(accountResponse)
                    call.respond(response.code, response.body)
                }
            }
        }
    }

}