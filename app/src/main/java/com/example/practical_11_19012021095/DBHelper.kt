package com.example.practical_11_19012021095

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context,DB_NAME,null,DB_VERSION) {

    companion object{
        val DB_NAME = "practical_11_db.db"
        val DB_VERSION = 1

        /* User Table */

        //Column Names
        val COL_USER_ID = "_id"
        val COL_USER_NAME = "_name"
        val COL_USER_PHONE = "_phone"
        val COL_USER_CITY = "_city"
        val COL_USER_EMAIL = "_email"
        val COL_USER_PASSWORD = "_password"

        val user_columns = arrayOf(
            COL_USER_ID,
            COL_USER_NAME,
            COL_USER_PHONE,
            COL_USER_CITY,
            COL_USER_EMAIL,
            COL_USER_PASSWORD
        )

        val USER_TABLE = "user"

        private val USER_TABLE_CREATE = (
                "CREATE TABLE " + USER_TABLE +
                "(" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT NOT NULL, " +
                COL_USER_PHONE + " INTEGER NOT NULL," +
                COL_USER_CITY + " TEXT NOT NULL," +
                COL_USER_EMAIL + " TEXT NOT NULL," +
                COL_USER_PASSWORD + " TEXT NOT NULL" +
                ");"
                )

        private val USER_TABLE_DROP = "DROP TABLE $USER_TABLE"

        /* User Table */

        /*Note Table*/

        //Column Names
        val COL_NOTE_ID = "_id"
        val COL_NOTE_TITLE = "_title"
        val COL_NOTE_SUBTITLE = "_subtitle"
        val COL_NOTE_DESCRIPTION = "_description"
        val COL_NOTE_REMINDER_TIME = "_reminder_time"
        val COL_NOTE_TIME = "_time"
        val COL_NOTE_ISREMINDER = "_isreminder"

        val note_columns = arrayOf(
            COL_NOTE_ID,
            COL_NOTE_TITLE,
            COL_NOTE_SUBTITLE,
            COL_NOTE_DESCRIPTION,
            COL_NOTE_TIME,
            COL_NOTE_ISREMINDER,
            COL_NOTE_REMINDER_TIME
        )

        val NOTE_TABLE = "note"

        private val NOTE_TABLE_CREATE = (
                "CREATE TABLE " + NOTE_TABLE +
                        "(" +
                        COL_NOTE_ID + " TEXT PRIMARY KEY, " +
                        COL_NOTE_TITLE + " TEXT NOT NULL, " +
                        COL_NOTE_SUBTITLE + " TEXT NOT NULL," +
                        COL_NOTE_DESCRIPTION + " TEXT NOT NULL," +
                        COL_NOTE_TIME + " TEXT NOT NULL," +
                        COL_NOTE_ISREMINDER + " INTEGER, " +
                        COL_NOTE_REMINDER_TIME + " TEXT" +
                        ");"
                )

        private val NOTE_TABLE_DROP = "DROP TABLE $NOTE_TABLE"

        /*Note Table*/

    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(USER_TABLE_CREATE)
        db?.execSQL(NOTE_TABLE_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(USER_TABLE_DROP)
        db?.execSQL(NOTE_TABLE_DROP)
        onCreate(db)
    }

}