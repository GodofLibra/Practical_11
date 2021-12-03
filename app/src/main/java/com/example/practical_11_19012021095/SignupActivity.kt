package com.example.practical_11_19012021095

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.practical_11_19012021095.R
import com.google.android.material.textfield.TextInputEditText

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        setStatusBarTransparent()

        val signupBtn = findViewById<AppCompatButton>(R.id.signup)
        val username = findViewById<TextInputEditText>(R.id.username)
        val phoneno = findViewById<TextInputEditText>(R.id.phoneno)
        val city = findViewById<TextInputEditText>(R.id.city)
        val email = findViewById<TextInputEditText>(R.id.email)
        val password = findViewById<TextInputEditText>(R.id.password)
        val cpassword = findViewById<TextInputEditText>(R.id.cpassword)

        val dbUserController : DBUserController = DBUserController(this)

        signupBtn.setOnClickListener {

            when {
                username.text.toString() == "" -> {
                    Toast.makeText(this, "Username is required!", Toast.LENGTH_SHORT).show()
                }
                phoneno.text.toString() == "" -> {
                    Toast.makeText(this, "Phone No. is required!", Toast.LENGTH_SHORT).show()
                }
                city.text.toString() == "" -> {
                    Toast.makeText(this, "City is required!", Toast.LENGTH_SHORT).show()
                }
                email.text.toString() == "" -> {
                    Toast.makeText(this, "Email is required!", Toast.LENGTH_SHORT).show()
                }
                password.text.toString() == "" -> {
                    Toast.makeText(this, "Password is required!", Toast.LENGTH_SHORT).show()
                }
                password.text.toString() != cpassword.text.toString() -> {
                    Toast.makeText(this, "Password Doesn't Match!", Toast.LENGTH_SHORT).show()
                }
                else -> {

                    val userData = LoginInfo(
                        username.text.toString(),
                        phoneno.text.toString().toInt(),
                        city.text.toString(),
                        email.text.toString(),
                        password.text.toString()
                    )

                    dbUserController.addUser(userData)

                    Intent(this, MainActivity::class.java).apply{
                        startActivity(this)
                    }
                }
            }
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

    override fun onDestroy() {
        super.onDestroy()
        Intent(this,MainActivity::class.java).apply {
            startActivity(this)
        }
    }

}