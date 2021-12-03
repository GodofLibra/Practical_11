package com.example.practical_11_19012021095

import java.io.Serializable

class LoginInfo : Serializable {
    private var username : String = ""
    private var phoneno : Int = 0
    private var city : String = ""
    private var email : String = ""
    private var password : String = ""
    private var id : Int = -1

    constructor(username:String, phoneno:Int, city:String, email:String, password:String) {
        this.username = username
        this.phoneno = phoneno
        this.city = city
        this.email = email
        this.password = password
    }

    fun setId(id:Int){
        this.id = id
    }

    fun getId():Int{
        return this.id
    }

    fun getUsername():String {
        return this.username
    }

    fun getPassword():String {
        return this.password
    }

    fun getCity():String{
        return this.city
    }

    fun getEmail():String {
        return this.email
    }

    fun getPhone():Int{
        return this.phoneno
    }
}