package com.example.practical_11_19012021095

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.content.ContentValues
import android.database.Cursor
import android.widget.Toast


class DBNoteController(context: Context) {

    private var dbHelper: DBHelper
    private lateinit var database: SQLiteDatabase
    private var contextprivate = context

    init {
        dbHelper = DBHelper(context)
    }

    fun close() {
        dbHelper.close()
    }

    fun addNote(note : Notes){
        database = dbHelper.writableDatabase

        val values = ContentValues()

        values.put(DBHelper.COL_NOTE_ID,note.getId())
        values.put(DBHelper.COL_NOTE_TITLE,note.getTitle())
        values.put(DBHelper.COL_NOTE_SUBTITLE,note.getSubTitle())
        values.put(DBHelper.COL_NOTE_DESCRIPTION,note.getDescription())
        values.put(DBHelper.COL_NOTE_TIME,note.getModifiedTime())
        values.put(DBHelper.COL_NOTE_REMINDER_TIME,note.getReminderTime())
        values.put(DBHelper.COL_NOTE_ISREMINDER,note.getIsReminder())

        database.insert(DBHelper.NOTE_TABLE, null, values)

        Toast.makeText(contextprivate,"Note Added!",Toast.LENGTH_SHORT).show()
        database.close()
    }

    fun getNote(id: String) : Notes {
        database = dbHelper.readableDatabase

        val cursor: Cursor = database.query(
            DBHelper.NOTE_TABLE,
            DBHelper.note_columns,
            DBHelper.COL_NOTE_ID + " =?",
            arrayOf(id),
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val note = Notes(
            cursor.getString(1).toString(),
            cursor.getString(2).toString(),
            cursor.getString(3).toString(),
            cursor.getString(4).toString(),
            cursor.getInt(5),
            cursor.getString(6).toString()
        )

        note.setId(cursor.getString(0))

        return note
    }

    fun getAllNotes(): ArrayList<Notes>? {
        val db = dbHelper.writableDatabase
        val notesList: ArrayList<Notes> = ArrayList()

        // Select All Query
        val selectQuery = "SELECT  * FROM " + DBHelper.NOTE_TABLE

        val cursor = db.rawQuery(selectQuery, null)

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                val note = Notes(
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6)
                )
                note.setId(cursor.getString(0))

                // Adding Note to list
                notesList.add(note)
            } while (cursor.moveToNext())
        }

        // return Notes list
        return notesList
    }

    // Updating single Note
    fun updateNote(note: Notes): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues()
        values.put(DBHelper.COL_NOTE_TITLE, note.getTitle())
        values.put(DBHelper.COL_NOTE_SUBTITLE, note.getSubTitle())
        values.put(DBHelper.COL_NOTE_DESCRIPTION, note.getDescription())
        values.put(DBHelper.COL_NOTE_TIME, note.getModifiedTime())
        values.put(DBHelper.COL_NOTE_ISREMINDER, note.getIsReminder())
        values.put(DBHelper.COL_NOTE_REMINDER_TIME,note.getReminderTime())

        // updating row
        return db.update(
            DBHelper.NOTE_TABLE, values,
            DBHelper.COL_NOTE_ID + " = ?",
            arrayOf(
                note.getId()
            )
        )
    }

    // Deleting single Note
    fun deleteNote(note : Notes) {
        val db = dbHelper.writableDatabase
        db.delete(
            DBHelper.NOTE_TABLE,
            DBHelper.COL_NOTE_ID + " = ?",
            arrayOf(note.getId())
        )
        db.close()
    }

    // Deleting single Note by ID
    fun deleteNote(_id: String) {
        val db = dbHelper.writableDatabase
        db.delete(
            DBHelper.NOTE_TABLE,
            DBHelper.COL_NOTE_ID + " = ?",
            arrayOf(_id)
        )
        db.close()
    }

}