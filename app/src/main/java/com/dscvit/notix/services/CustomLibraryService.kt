package com.dscvit.notix.services

import android.content.Intent
import android.os.IBinder
import android.service.notification.NotificationListenerService

open class CustomLibraryService : NotificationListenerService() {

    override fun onBind(p0: Intent?): IBinder? = null
}