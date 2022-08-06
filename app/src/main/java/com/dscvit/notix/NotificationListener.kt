package com.dscvit.notix

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import com.dscvit.notix.database.NotixRepository
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.utils.SpamClassifier.isSpam
import com.dscvit.notix.utils.SpamClassifier.spamClassifyMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener : NotificationListenerService() {

    @Inject
    lateinit var repository: NotixRepository

    companion object {
        const val TAG = "NotificationListener"
    }


    override fun onNotificationPosted(newNotification: StatusBarNotification) {

        // Building the input message string
        var message = ""
        newNotification.notification.extras.apply {
            message += getString("android.title") + " "
            message += getString("android.text")
        }

        // Classify the notification
        val spamScore = spamClassifyMessage(this, message)
        val isSpam = isSpam(spamScore)


        var millis = newNotification.postTime

        // Logging the notification
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
                    "\n" + "packageName: " + newNotification.packageName +
                    "\n" + "spam score: " + spamScore.toString() +
                    "\n" + "isSpam: " + isSpam.toString()

            /*"-------- onNotificationPosted(): " + "ID :" + newNotification.id +
                    "\n" + newNotification.notification.extras.getString("android.title") +
                    "\n"+ newNotification.notification.extras.getString("android.text") +
                    "\n"+ newNotification.notification.tickerText +
                    "\n" + newNotification.packageName */

        )

        //change time zone
        val notification = NotificationData(
            newNotification.uid,
            newNotification.notification.extras.getString("android.title"),
            newNotification.notification.extras.getString("android.text"),
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
                .withZone(ZoneId.of("IST"))
                .format(Instant.ofEpochMilli(millis)),
            newNotification.packageName,
            false,
            spamScore
        )

        runBlocking {
            repository.insert(notification)
        }


    }
}