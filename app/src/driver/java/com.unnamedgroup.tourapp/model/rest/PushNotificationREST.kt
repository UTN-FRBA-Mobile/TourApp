package com.unnamedgroup.tourapp.model.rest

data class PushNotificationREST(
    val to: String,
    val data: PushNotificationDataREST
)