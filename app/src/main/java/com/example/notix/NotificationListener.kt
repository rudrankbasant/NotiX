package com.example.notix

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {

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

            /*"-------- onNotificationPosted(): " + "ID :" + newNotification.id +
                    "\n" + newNotification.notification.extras.getString("android.title") +
                    "\n"+ newNotification.notification.extras.getString("android.text") +
                    "\n"+ newNotification.notification.tickerText +
                    "\n" + newNotification.packageName */

        )
    }
}