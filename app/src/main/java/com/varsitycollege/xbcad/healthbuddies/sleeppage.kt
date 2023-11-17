package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class sleeppage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sleeppage)

        val melodycard = findViewById<CardView>(R.id.melodiescard)
        val breathingcard = findViewById<CardView>(R.id.breathingcard)
        val meditationcard = findViewById<CardView>(R.id.meditationcv)

        melodycard.setOnClickListener {
            val intent = Intent(this, melodiespage::class.java)
            startActivity(intent)
        }

        breathingcard.setOnClickListener {
            val intent = Intent(this, breathingpage::class.java)
            startActivity(intent)
        }

        meditationcard.setOnClickListener {
            val intent = Intent(this, meditationvideos::class.java)
            startActivity(intent)
        }
    }
}