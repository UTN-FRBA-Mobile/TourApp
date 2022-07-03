package com.unnamedgroup.tourapp.utils

import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavDeepLinkBuilder
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.unnamedgroup.tourapp.R
import com.unnamedgroup.tourapp.view.activity.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class FirebaseMessagingServiceImp: FirebaseMessagingService() {

    private fun createID(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt()
    }

    override fun onNewToken(token: String) {
        Log.d("FirebaseMessaging", "Refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("FirebaseMessaging", "From: ${remoteMessage.from}")

        var title: String? = null
        var text: String? = null
        var pendingIntent: PendingIntent? = null

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d("FirebaseMessaging", "Message data payload: ${remoteMessage.data}")

            if (remoteMessage.data["notificationType"] == "1") {
                title = getString(R.string.notification_trip_state_change_title)
                text = getString(R.string.notification_trip_state_change, remoteMessage.data["tripId"])
                val bundle = Bundle()
                bundle.putInt("tripId", remoteMessage.data["tripId"]!!.toInt())
                bundle.putBoolean("fromNotification", true)

                pendingIntent = NavDeepLinkBuilder(this)
                    .setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.nav_graph)
                    .setDestination(R.id.tripDetailsFragment)
                    .setArguments(bundle)
                    .createPendingIntent()

            } else {
                return
            }
        }
        // Check if message contains a notification payload.
        remoteMessage.notification?.let {
            Log.d("FirebaseMessaging", "Message Notification Body: ${it.body}")
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
        val builder = NotificationCompat.Builder(this, getString(R.string.default_notifications_channel))
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(text)
            .setContentText(title)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(this)) {
            notify(createID(), builder.build())
        }
    }

}