package com.unnamedgroup.tourapp.model.rest

import com.unnamedgroup.tourapp.model.business.User

data class UserREST(
    val id: Int,
    val email: String,
    val dni: String,
) {
    fun toUser(): User {
        return User(
            id,
            email,
            dni
        )
    }
}