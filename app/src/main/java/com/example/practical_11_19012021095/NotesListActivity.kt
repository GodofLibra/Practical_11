package com.example.practical_11_19012021095

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.practical_11_19012021095.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotesListActivity : AppCompatActivity() {
    companion object{
        lateinit var listView:ListView
        lateinit var arrayList : ArrayList<Notes>
        lateinit var adapter : NotesAdapter

        fun updateStateOfListView(context: Context,dbNoteController: DBNoteController){
            arrayList = dbNoteController.getAllNotes()!!

            adapter = NotesAdapter(context,arrayList)

            listView.adapter = adapter
        }
    }
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_list)

        setStatusBarTransparent()

        val slider = findViewById<ImageView>(R.id.image_slider)

        slider.setBackgroundResource(R.drawable.silder_animation)

        val animation : AnimationDrawable = slider.background as AnimationDrawable
        animation.start()


        val bottomMenu = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        listView = findViewById(R.id.listView)
        val fButton = findViewById<FloatingActionButton>(R.id.floating_action_btn)

        bottomMenu.selectedItemId = R.id.notes

        val dbNoteController = DBNoteController(this)

        arrayList = dbNoteController.getAllNotes()!!

        adapter = NotesAdapter(this,arrayList)

        listView.adapter = adapter

        fButton.setOnClickListener {
            val boxView = LayoutInflater.from(this).inflate(R.layout.form_view, null)
            val alertDialog = AlertDialog.Builder(this).setView(boxView)

            val titleText = boxView.findViewById<TextView>(R.id.form_title)
            val subTitleText = boxView.findViewById<TextView>(R.id.form_sub_title)
            val descriptionText = boxView.findViewById<TextView>(R.id.form_description)
            val timePicker = boxView.findViewById<TimePicker>(R.id.form_time_picker)
            val switch = boxView.findViewById<Switch>(R.id.form_switch)

            alertDialog.apply {
                setTitle("Add Note here..")
                var isReminder = 0

                switch.setOnClickListener {
                    isReminder = if(isReminder==0) 1 else 0
                }

                setPositiveButton("Add") { dialogInterface: DialogInterface, i: Int ->
                    val alarmCalendar = Calendar.getInstance()
                    val year: Int = alarmCalendar.get(Calendar.YEAR)
                    val month: Int = alarmCalendar.get(Calendar.MONTH)
                    val day: Int = alarmCalendar.get(Calendar.DATE)

                    val time = SimpleDateFormat("MMM, dd yyyy hh:mm:ss a").format(alarmCalendar.time)

                    var reminderTime = ""
                    if(isReminder==1){
                        alarmCalendar.set(year, month, day, timePicker.hour, timePicker.minute, 0)

                        reminderTime = SimpleDateFormat("MMM, dd yyyy hh:mm:ss a").format(alarmCalendar.time)
                    }

                    val newItem = Notes(titleText.text.toString(),subTitleText.text.toString(),descriptionText.text.toString(),time,isReminder,reminderTime)

                    newItem.setId(Notes.randomID())

                    dbNoteController.addNote(newItem)

                    updateStateOfListView(this@NotesListActivity,dbNoteController)

                    if(isReminder==1) {
                        setAlarm(alarmCalendar.timeInMillis, "Start",newItem.getId())
                    }
                }
                setNegativeButton("Cancel"){ dialogInterface: DialogInterface, i: Int ->
                    Toast.makeText(applicationContext,"Canceled",Toast.LENGTH_SHORT).show()
                }
            }.show()
        }



        bottomMenu.setOnItemSelectedListener {
            when(it.itemId){
                R.id.dashboard -> {
                    Intent(this,DashBoardActivity::class.java).apply {
                        startActivity(this)
                    }
                    return@setOnItemSelectedListener true
                }
                R.id.notes->{
                    Intent(this,NotesListActivity::class.java).apply {
                        startActivity(this)
                    }
                    return@setOnItemSelectedListener  true
                }
                else -> {
                    return@setOnItemSelectedListener true
                }
            }
        }
    }
    private fun setAlarm(millisTime: Long, str: String,id:String)
    {
        val intent = Intent(this, MyReceiver::class.java)
//        intent.putExtra("Service1", str)
        val intentService = Intent(this, AlarmService::class.java)
        intentService.putExtra("id",id)

        val pendingIntent = PendingIntent.getBroadcast(this@NotesListActivity, 234324243, intent, 0)
        val pendingIntentService = PendingIntent.getService(this@NotesListActivity, 234324244, intentService, 0)

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        if(str == "Start") {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                millisTime,
                pendingIntent
            )
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                millisTime,
                pendingIntentService
            )
        }else if(str == "Stop")
        {
            alarmManager.cancel(pendingIntent)
        }
    }



    private fun setWindowFlag(flagTranslucentStatus: Int, b: Boolean) {
        val winParameters = window.attributes
        if(b){
            winParameters.flags =  winParameters.flags or flagTranslucentStatus
        }
        else{
            winParameters.flags =  winParameters.flags and flagTranslucentStatus.inv()
        }
        window.attributes = winParameters
    }

    private fun setStatusBarTransparent(){
        if (Build.VERSION.SDK_INT in 19..20) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
            }
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if(Build.VERSION.SDK_INT >= 21){
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}

