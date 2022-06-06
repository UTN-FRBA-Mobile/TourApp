package com.unnamedgroup.tourapp.model.rest

data class NewUserREST(
    val name: String,
    val email: String,
    val dni: String,
    val password: String,
    val token: String,
)