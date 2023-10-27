package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val regTextView = findViewById<Button>(R.id.loginBttn)
        regTextView.setOnClickListener {
            val registrationIntent = Intent(this, Store::class.java)
            startActivity(registrationIntent)
        }
    }
}