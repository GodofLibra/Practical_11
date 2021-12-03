package com.example.practical_11_19012021095


import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat


class NotificationUtils(context: Context) : ContextWrapper(context) {
    private var notificationManager: NotificationManager? = null
        private get() {
            if (field == null) {
                field = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            return field
        }
    var DEFAULT_CHANNEL_ID = "DEFAULT"
    var DEFAULT_CHANNEL_NAME = "DEFAULT CHANNEL"
    var pkgName = "notification.channel"
    fun createChannel(CHANNEL_ID: String?, CHANNEL_NAME: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // create android channel
            val newChannel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            )
            // Sets whether notifications posted to this channel should display notification lights
            newChannel.enableLights(true)

            // Sets whether notification posted to this channel should vibrate.
            newChannel.enableVibration(true)

            // Sets the notification light color for notifications posted to this channel
            newChannel.lightColor = Color.GREEN

            // Sets whether notifications posted to this channel appear on the lockscreen or not
            newChannel.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
            notificationManager!!.createNotificationChannel(newChannel)
        }
    }

    fun sendNotificationInChannel(
        notificationId: Int, resultIntent: Intent?,
        builder: NotificationCompat.Builder
    ) {
        val pi = PendingIntent.getActivity(
            this,
            0,
            resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        // for notification click action, also required on Gingerbread and below
        builder.setContentIntent(pi)
        notificationManager!!.notify(notificationId, builder.build())
    }

    fun sendNotificationInDefaultChannel(note: Notes,notificationId: Int) {
        val resultIntent = Intent(this, NotificationActivity::class.java)
        resultIntent.putExtra("note",note)
        sendNotificationInChannel(
            notificationId, resultIntent,
            getDefaultNotificationBuilder(note,DEFAULT_CHANNEL_ID)
        )
    }

    private fun getDefaultNotificationBuilder(
        note:Notes,
        channelId: String
    ): NotificationCompat.Builder {
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        return NotificationCompat.Builder(
            applicationContext,
            channelId )
            .setDefaults(Notification.DEFAULT_ALL)
            .setSmallIcon(R.drawable.stat_notify_more)
            .setTicker("Practical-11 TODO 20012022003-MIHIR")
            .setContentTitle(note.getTitle())
            .setContentText(note.getSubTitle())
            .setAutoCancel(true)
    }
    init {
        pkgName =
            if (context.packageName != null)
                context.packageName
            else
                pkgName

        DEFAULT_CHANNEL_ID = pkgName + "." + DEFAULT_CHANNEL_ID.toUpperCase()
        createChannel(DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME)
    }
}