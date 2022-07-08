package com.unnamedgroup.tourapp.service

import com.unnamedgroup.tourapp.model.rest.PushNotificationREST
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PNService {

    @Headers(
        "Content-Type: application/json",
        "Accept: application/json",
        "Authorization: key=AAAAseoscP4:APA91bH39S726aZIp5omzorQbnUskmdEL3hQuBLNTZVsSTwqS2cycJQX5Fn77jt5VGBVF0yclXBy7DNH-Eg8wFVYdr1P4sqJbc0IDbWPvXEngDHTeB5ev7yr7HLMFcYmrkvZ3SOzsNhW"
    )
    @POST("fcm/send")
    fun createTripStateChangePN(@Body user: PushNotificationREST): Call<Void>

}