package com.bellonee.auth.principal


import com.bellonee.model.User
import io.ktor.auth.*

class UserPrincipal(val user: User) : Principal