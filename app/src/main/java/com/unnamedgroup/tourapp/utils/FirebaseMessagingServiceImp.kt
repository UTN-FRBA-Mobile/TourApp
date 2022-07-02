package com.unnamedgroup.tourapp.utils

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService

class FirebaseMessagingServiceImp: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FirebaseMessaging", "Refreshed token: $token")
    }

}