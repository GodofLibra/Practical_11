package com.example.practical_11_19012021095

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.example.practical_11_19012021095.R
import java.text.SimpleDateFormat
import java.util.*

class NotesAdapter(private val context: Context, private val arrayList: ArrayList<Notes>) :
    BaseAdapter() {

    private lateinit var title : TextView
    private lateinit var subTitle : TextView
    private lateinit var description : TextView
    private lateinit var reminderTime : TextView
    private lateinit var modifiedTime : TextView
    private var isReminder : Int = 0
    private lateinit var editBtn : ImageButton
    private lateinit var deleteBtn : ImageButton
    private val dbNoteController = DBNoteController(context)


    override fun getCount(): Int {
        return arrayList.size
    }

    override fun getItem(position: Int): Any {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val convertView: View? = LayoutInflater.from(context).inflate(R.layout.item_box,parent,false)
        this.title = convertView!!.findViewById(R.id.textViewTitle)
        this.subTitle = convertView.findViewById(R.id.textViewSubTitle)
        this.description = convertView.findViewById(R.id.textViewDescription)
        this.reminderTime = convertView.findViewById(R.id.reminder_time)
        this.modifiedTime = convertView.findViewById(R.id.time)
        this.editBtn = convertView.findViewById(R.id.edit_btn)
        this.deleteBtn = convertView.findViewById(R.id.delete_btn)

        val noteId = arrayList[position].getId()
        val note = dbNoteController.getNote(noteId)

        this.title.text = note.getTitle()
        this.subTitle.text = note.getSubTitle()
        this.description.text = note.getDescription()
        this.reminderTime.text = note.getReminderTime()
        this.modifiedTime.text = note.getModifiedTime()
        this.isReminder = note.getIsReminder()

        this.editBtn.setOnClickListener {

            val boxView = LayoutInflater.from(context).inflate(R.layout.form_view, null)
            val alertDialog = AlertDialog.Builder(context).setView(boxView)

            val titleText = boxView.findViewById<TextView>(R.id.form_title)
            val subTitleText = boxView.findViewById<TextView>(R.id.form_sub_title)
            val descriptionText = boxView.findViewById<TextView>(R.id.form_description)
            val timePicker = boxView.findViewById<TimePicker>(R.id.form_time_picker)
            var switch = boxView.findViewById<Switch>(R.id.form_switch)

            alertDialog.apply {
                setTitle("Edit Note here..")

                titleText.text = arrayList[position].getTitle()
                subTitleText.text = arrayList[position].getSubTitle()
                descriptionText.text = arrayList[position].getDescription()
//                timePicker.minute = arrayList[position].getMinutes().toInt()

                switch.isChecked = isReminder==1


                switch.setOnClickListener {
                    isReminder = if(isReminder==0) 1 else 0
                }


                setPositiveButton("Update") { dialogInterface: DialogInterface, i: Int ->
                    val alarmCalendar = Calendar.getInstance()

                    val year: Int = alarmCalendar.get(Calendar.YEAR)
                    val month: Int = alarmCalendar.get(Calendar.MONTH)
                    val day: Int = alarmCalendar.get(Calendar.DATE)
                    val time = SimpleDateFormat("MMM, dd yyyy hh:mm:ss a").format(alarmCalendar.time)

                    alarmCalendar.set(year,month,day,timePicker.hour,timePicker.minute)


                    val note = Notes(
                        titleText.text.toString(),
                        subTitleText.text.toString(),
                        descriptionText.text.toString(),
                        time.toString(),
                        isReminder,
                        SimpleDateFormat("MMM, dd yyyy hh:mm:ss a").format(alarmCalendar.time),
                    )
                    note.setId(arrayList[position].getId())
                    dbNoteController.updateNote(note)

                    NotesListActivity.updateStateOfListView(context,dbNoteController)

                    Toast.makeText(context,"Note Updated!",Toast.LENGTH_SHORT).show()

                }
                setNegativeButton("Cancel"){ dialogInterface: DialogInterface, i: Int ->
                    Toast.makeText(context,"Canceled",Toast.LENGTH_SHORT).show()
                }
            }.show()
        }

        this.deleteBtn.setOnClickListener {
            DBNoteController(context).deleteNote(arrayList[position].getId())
            NotesListActivity.updateStateOfListView(context, dbNoteController)
        }

        return convertView
    }


}
