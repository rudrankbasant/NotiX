package com.example.notix

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.example.notix.database.NotixRepository
import com.example.notix.model.NotificationData
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener: NotificationListenerService() {

    @Inject
    lateinit var repository: NotixRepository

    companion object {
        const val TAG = "NotificationListener"
    }



    override fun onNotificationPosted(newNotification: StatusBarNotification) {
        Log.i(
            TAG,
            "------------OnNotificationPosted" +
                    "\n" + "newNotification: " + newNotification +
                    "\n" + "newNotification.notification: " + newNotification.notification +
                    "\n" + "ID :" + newNotification.id +
                    "\n" + "title: " + newNotification.notification.extras.getString("android.title") +
                    "\n" + "text: " + newNotification.notification.extras.getString("android.text") +
                    "\n" + "tickerText: " + newNotification.notification.tickerText +
                    "\n" + "postedTime: " + newNotification.postTime +
                    "\n" + "package name: " + newNotification.packageName


        )

        val notification = NotificationData(newNotification.id,
            newNotification.notification.extras.getString("android.title"),
            newNotification.notification.extras.getString("android.text"),
            newNotification.postTime,
            newNotification.packageName)


        runBlocking {
            repository.insert(notification)
        }




    }
}