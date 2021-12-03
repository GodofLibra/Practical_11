package com.example.practical_11_19012021095

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.practical_11_19012021095.R
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setStatusBarTransparent()

        val loginBtn = findViewById<AppCompatButton>(R.id.login)
        val email = findViewById<TextInputEditText>(R.id.email)
        val password = findViewById<TextInputEditText>(R.id.password)
        val account = findViewById<TextView>(R.id.account)

        val sharedPrefs = getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val dbUserController = DBUserController(this)

        account.setOnClickListener {
            Intent(this,SignupActivity::class.java).apply {
                startActivity(this)
            }
        }


        loginBtn.setOnClickListener{
                when {
                    email.text!!.toString() == "" -> {
                        Toast.makeText(this, "Email Is Required!", Toast.LENGTH_SHORT).show()
                    }
                    password.text!!.toString() == "" -> {
                        Toast.makeText(this, "Password is Required!", Toast.LENGTH_SHORT).show()
                    }
                    else -> {

                        val user = dbUserController.verifyUser(email.text.toString(),password.text.toString())
                        if(user!=null){

                            sharedPrefs.edit().putInt("user_id",user.getId()).commit()

                            Handler().postDelayed({
                                val intent = Intent(this, DashBoardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }, 0)
                        }
                        else{
                            Toast.makeText(this,"Fault",Toast.LENGTH_SHORT).show()
                            email.setText("", TextView.BufferType.EDITABLE)
                            password.setText("", TextView.BufferType.EDITABLE)
                            Toast.makeText(this,"Email and Password doesn't matched!",Toast.LENGTH_SHORT).show()
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
}