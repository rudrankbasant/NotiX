package com.dscvit.notix.services


/*
class MyForegroundService : Service() {
    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val input = intent?.getStringExtra(Constants.inputExtra)
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0)
// 1
        val notification = NotificationCompat.Builder(this, channelID)
            .setContentTitle(notificationTitle)
            .setContentText(input)
            .setSmallIcon(R.drawable.ic_android_24dp)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)
// 2
        return START_NOT_STICKY
    }
}*/
