package com.example.practical_11_19012021095

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase


class DBUserController(context: Context) {
    private var dbHelper: DBHelper
    private lateinit var database: SQLiteDatabase
    private val pContext = context

    init{
        dbHelper = DBHelper(context)
    }

    fun close(){
        dbHelper.close()
    }

    fun addUser(user : LoginInfo){
        database = dbHelper.writableDatabase

        val values = ContentValues()

        values.put(DBHelper.COL_USER_EMAIL,user.getEmail())
        values.put(DBHelper.COL_USER_NAME,user.getUsername())
        values.put(DBHelper.COL_USER_PHONE,user.getPhone())
        values.put(DBHelper.COL_USER_CITY,user.getCity())
        values.put(DBHelper.COL_USER_PASSWORD,user.getPassword())

        database.insert(DBHelper.USER_TABLE, null, values)
        database.close()

    }

    fun getUser(id:Int):LoginInfo{
        database = dbHelper.readableDatabase

        val cursor: Cursor = database.query(
            DBHelper.USER_TABLE,
            DBHelper.user_columns,
            DBHelper.COL_USER_ID + " =?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        cursor.moveToFirst()

        val user = LoginInfo(
            cursor.getString(1),
            cursor.getInt(2),
            cursor.getString(3),
            cursor.getString(4),
            cursor.getString(5)
        )
        user.setId(cursor.getInt(0))
        return user
    }

    fun verifyUser(email : String,password : String) : LoginInfo? {
        database = dbHelper.readableDatabase

        val cursor: Cursor = database.rawQuery(
            "Select * from user where ${DBHelper.COL_USER_EMAIL}=? and ${DBHelper.COL_USER_PASSWORD}=?",
            arrayOf(email, password)
        )
        cursor.moveToFirst()

        if(cursor.count > 0) {

            val user = LoginInfo(
                cursor.getString(1),
                cursor.getInt(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5)
            )
            user.setId(cursor.getInt(0))
            return user
        }
        return null
    }

}