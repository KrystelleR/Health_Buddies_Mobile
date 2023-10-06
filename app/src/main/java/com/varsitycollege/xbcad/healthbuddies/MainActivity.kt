package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val foodcard = findViewById<CardView>(R.id.foodcardbtn)
        val exercisecard = findViewById<CardView>(R.id.exercisecardbtn)
        val sleepcard = findViewById<CardView>(R.id.sleepcardbtn)

        foodcard.setOnClickListener {
            val intent = Intent(this, melodiespage::class.java)
            startActivity(intent)
        }

        exercisecard.setOnClickListener {
            val intent = Intent(this, fitnesspage::class.java)
            startActivity(intent)
        }

        sleepcard.setOnClickListener {
            val intent = Intent(this, sleeppage::class.java)
            startActivity(intent)
        }
    }
}