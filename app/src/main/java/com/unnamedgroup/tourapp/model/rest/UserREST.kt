package com.unnamedgroup.tourapp.model.rest

import com.unnamedgroup.tourapp.model.business.User

data class UserREST(
    val id: Int,
    val name: String,
    val email: String,
    val dni: String,
    val token: String,
) {
    fun toUser(): User {
        return User(
            id,
            name,
            email,
            dni,
            token,
        )
    }
}