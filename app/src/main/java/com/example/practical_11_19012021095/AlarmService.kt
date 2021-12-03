package com.example.practical_11_19012021095


import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.widget.Toast

class AlarmService : Service() {

    val PERSONAL_CHANNEL_ID = "PersonalChannel"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        if (intent != null) {
            val id: String? = intent.getStringExtra("id")

            val dbNoteController = DBNoteController(applicationContext)

            Toast.makeText(applicationContext, id.toString(), Toast.LENGTH_SHORT).show()

            val data: Notes = dbNoteController.getNote(id!!)

            val notificationUtils = NotificationUtils(applicationContext)
            notificationUtils.createChannel(PERSONAL_CHANNEL_ID, "PERSONAL")
            notificationUtils.sendNotificationInDefaultChannel(
                data,
                101
            )
        }
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }
}