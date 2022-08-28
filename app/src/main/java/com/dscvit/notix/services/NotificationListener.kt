package com.dscvit.notix.services


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.dscvit.notix.R
import com.dscvit.notix.database.NotixRepository
import com.dscvit.notix.model.NotificationData
import com.dscvit.notix.model.WhiteListData
import com.dscvit.notix.ui.MainActivity
import com.dscvit.notix.ui.history.HistoryViewModel
import com.dscvit.notix.utils.SpamClassifier.isSpam
import com.dscvit.notix.utils.SpamClassifier.spamClassifyMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.runBlocking
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@AndroidEntryPoint
class NotificationListener :
    NotificationListenerService() /*CustomLibraryService(), LifecycleOwner*/ {

    @Inject
    lateinit var repository: NotixRepository

    var allWhiteListedApps = mutableListOf<WhiteListData>()

    //private val mServiceLifecycleDispatcher = ServiceLifecycleDispatcher(this)

    companion object {
        const val TAG = "NotificationListener"
        private const val CHANNEL_ID = "channelID"
        private const val CHANNEL_NAME = "Notix Spam Classifier"
        private const val NOTIFICATION_ID = 1
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //mServiceLifecycleDispatcher.onServicePreSuperOnStart()

        Log.d("App Service Foreground", "Service started")
        /*startForeground(
            1, NotificationCompat.Builder(this, NotificationChannel.DEFAULT_CHANNEL_ID)
                .setContentTitle("Notix is Running")
                .setContentText("Finding and removing spam notifications")
                .setContentIntent(
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, MainActivity::class.java),
                        0
                    )
                )
                .build()
        )*/


        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(
                    this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
                )
            }

        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("NotiX is running")
            .setContentText("Cleaning spam notifications")
            .setSmallIcon(R.drawable.ic_x)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.ic_x))
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setContentIntent(pendingIntent)
            .build()
        val notificationManager = NotificationManagerCompat.from(this)

        // Notification ID cannot be 0.
        startForeground(1, notification)



        return START_STICKY


    }

    override fun onBind(intent: Intent?): IBinder? {
        return super.onBind(intent)
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.e("Listener is Connected-----------", "yup")
    }


    override fun onNotificationPosted(newNotification: StatusBarNotification) {

        // Building the input message string
        var message = ""
        newNotification.notification.extras.apply {
            message += getString("android.title") + " "
            message += getString("android.text")
        }
        newNotification.notification.getLargeIcon()
        // Classify the notification
        var spamScore = 0.0f
        val sharedPref = getSharedPreferences("spam_switch", Context.MODE_PRIVATE)
        val spamSwitchToggle = sharedPref.getString("switch_toggle", "true")
        if(spamSwitchToggle=="true"){
            if (newNotification.packageName != "com.whatsapp") {
                spamScore = spamClassifyMessage(this, message)
            }
        }else{
            spamScore=0.0f
        }

        val isSpam = isSpam(spamScore)


        // Logging the notification
        Log.i(
            TAG,
            "------------OnNotificationPosted" +
                    "\n" + "newNotification: " + newNotification +
                    "\n" + "newNotification.notification: " + newNotification.notification +
                    "\n" + "ID :" + newNotification.key +
                    "\n" + "title: " + newNotification.notification.extras.getString("android.title") +
                    "\n" + "text: " + newNotification.notification.extras.getString("android.text") +
                    "\n" + "tickerText: " + newNotification.notification.tickerText +
                    "\n" + "postedTime: " + newNotification.postTime +
                    "\n" + "packageName: " + newNotification.packageName +
                    "\n" + "spam score: " + spamScore.toString() +
                    "\n" + "isSpam: " + isSpam.toString()
        )
        var title = newNotification.notification.extras.getString("android.title")
        var desc = newNotification.notification.extras.getString("android.text")


        val patternGroupMessage = Regex("\\([0-9]+ messages\\):")
        //val patternGroupMessage2 = Regex(": \\+[0-9]+ [0-9]{5} [0-9]{5}")
        if (title != null) {
            if (title.contains(patternGroupMessage)) {

                var seperated = title.split(patternGroupMessage)
                var append = seperated[1].trim { it <= ' ' }
                desc = "$desc" + "x.gdscSender.x" + append
                title = seperated[0]
            } else if (title.contains(":")) {
                val seperated = title.split(":")
                title = seperated[0]
                desc = "$desc" + "x.gdscSender.x" + seperated[1]
            }
        }

        val millis = newNotification.postTime

        //change time zone
        val notification = NotificationData(
            newNotification.id,
            title?.trim { it <= ' ' },
            desc?.trim { it <= ' ' },
            newNotification.notification.`when`,
            DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
                .withZone(ZoneId.of("IST"))
                .format(Instant.ofEpochMilli(millis)),
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                .withZone(ZoneId.of("IST"))
                .format(Instant.ofEpochMilli(millis)),
            newNotification.packageName.trim { it <= ' ' },
            false,
            spamScore
        )

        var isNotNotixNotification = true
        if(newNotification.packageName == "com.dscvit.notix"){
            isNotNotixNotification = false
        }


        //Not storing in DB for some specific whatsapp desc
        var whatsappConditions = true
        val patternNewMessages = Regex("[0-9]+ new messages")
        val patternNewMessages2= Regex("[0-9]+ messages from [0-9]+ chats")
        val checkDescription = newNotification.notification.extras.getString("android.text")
        if (checkDescription != null) {
            if (checkDescription == "Checking for new messages" || checkDescription.contains(
                    patternNewMessages
                ) || checkDescription.contains(patternNewMessages2)
                /*|| isVoiceCallMessage(checkDescription)*/ || checkDescription == "null" || isDuplicate(title, desc)
            ) {
                whatsappConditions = false
            }
        }else{
                whatsappConditions = false
        }

        if (whatsappConditions && isNotNotixNotification) {
            runBlocking {
                /*var value = true

                val uniqueConversation = repository.getAllUniqueConversation()

                Log.d("Listener: ", "Here list received started: $uniqueConversation")
                for (i in uniqueConversation) {
                    if (i.title == notification.title && i.desc == notification.desc) {
                        Log.d("Listener filter -------", "${i.title} -> ${i.desc}")
                        value = false
                        break
                    }
                }*/
                 repository.insert(notification)
            }
        }

/*
        runBlocking {
            var whitelisted = false
            allWhiteListedApps = repository.getAllWhitelistedApps() as MutableList<WhiteListData>
            for(i in allWhiteListedApps){
                if(newNotification.packageName == i.pkgName){
                    Log.d("Pkg NAME -------", i.pkgName)
                    whitelisted = true
                    break
                }
            }
            Log.d("NL Whitelisted ----------", whitelisted.toString())
            if (whitelisted) {
                cancelNotification(newNotification.key)
                Log.d("NL Whitelisted Cancelled ----------", true.toString())
            }
        }*/

        if(isSpam){
            cancelNotification(newNotification.key)
        }




    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                lightColor = Color.BLACK
                enableLights(true)
            }
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    /*private fun isVoiceCallMessage(checkDescription: String?): Boolean {

    }*/

    private fun isDuplicate(title: String?, description: String?): Boolean {
        var value = false

        /*repository.allUniqueConversations.observe(this, Observer { list ->
            list?.let {
                Log.d("Listener: ", "Here list received started")
                for(i in list){
                    if(i.title == title && i.desc==description){
                        value = false
                    }
                }
            }
        })*/

        return value
    }

    /*override fun getLifecycle(): Lifecycle = mServiceLifecycleDispatcher.lifecycle

    override fun onBind(p0: Intent?): IBinder? {
        mServiceLifecycleDispatcher.onServicePreSuperOnBind()
        return super.onBind(p0)
    }

    override fun onCreate() {
        mServiceLifecycleDispatcher.onServicePreSuperOnCreate()
        super.onCreate()
    }

    //Deprecated, but you might need to add this if targeting really old API.
    override fun onStart(intent: Intent?, startId: Int) {
        mServiceLifecycleDispatcher.onServicePreSuperOnStart()
        super.onStart(intent, startId)
    }

    override fun onDestroy() {
        mServiceLifecycleDispatcher.onServicePreSuperOnDestroy()
        super.onDestroy()
    }*/


}