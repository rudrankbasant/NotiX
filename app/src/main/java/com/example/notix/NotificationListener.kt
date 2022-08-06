package com.example.notix

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notix.database.NotixRepository
import com.example.notix.model.NotificationData
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener: NotificationListenerService() {

    @Inject
    lateinit var repository: NotixRepository

    companion object {
        const val TAG = "NotificationListener"
    }



    override fun onNotificationPosted(newNotification: StatusBarNotification) {
        var millis = newNotification.postTime
        Log.i(
            TAG,
            "------------OnNotificationPosted" +
                    "\n" + "newNotification: " + newNotification +
                    "\n" + "key: " + newNotification.key +
                    "\n" + "newNotification.notification: " + newNotification.notification +
                    "\n" + "ID :" + newNotification.id +
                    "\n" + "title: " + newNotification.notification.extras.getString("android.title") +
                    "\n" + "text: " + newNotification.notification.extras.getString("android.text") +
                    "\n" + "tickerText: " + newNotification.notification.tickerText +
                    "\n" + "postedTime: " + DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
                .withZone(ZoneId.of("IST"))
                .format(Instant.ofEpochMilli(millis)) +
                    "\n" + "package name: " + newNotification.packageName
        )

        //change time zone
        val notification = NotificationData(newNotification.uid,
            newNotification.notification.extras.getString("android.title"),
            newNotification.notification.extras.getString("android.text"),
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
                .withZone(ZoneId.of("IST"))
                .format(Instant.ofEpochMilli(millis)),
            newNotification.packageName,
        false)

        runBlocking {
            repository.insert(notification)
        }





    }
}