package com.unnamedgroup.tourapp.model.rest

class PushNotificationDataREST(
    val notificationType: Int,
    val tripId: Int,
    val newState: String,
    val tripName: String
)