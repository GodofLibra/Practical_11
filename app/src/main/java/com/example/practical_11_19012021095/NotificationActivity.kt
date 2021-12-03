package com.example.practical_11_19012021095

import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.practical_11_19012021095.R


class NotificationActivity : AppCompatActivity() {
    private lateinit var title : TextView
    private lateinit var subTitle : TextView
    private lateinit var description : TextView
    private lateinit var reminderTime : TextView
    private lateinit var modifiedTime : TextView
    private var isReminder : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification)

        val note = (intent.extras?.getSerializable("note") as? Notes)!!


        this.title = findViewById(R.id.notificationtextViewTitle)
        this.subTitle = findViewById(R.id.notificationtextViewSubTitle)
        this.description = findViewById(R.id.notificationtextViewDescription)
        this.reminderTime = findViewById(R.id.notificationreminder_time)
        this.modifiedTime = findViewById(R.id.notificationtime)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportActionBar?.title = "Reminder Of ${note.getTitle()}"

        this.title.text = note.getTitle()
        this.subTitle.text = note.getSubTitle()
        this.description.text = note.getDescription()
        this.reminderTime.text = note.getReminderTime()
        this.modifiedTime.text = note.getModifiedTime()
        this.isReminder = note.getIsReminder()

    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.notes_list -> {
                finish()
                return true
            }
        }
        return super.onContextItemSelected(item)
    }
}