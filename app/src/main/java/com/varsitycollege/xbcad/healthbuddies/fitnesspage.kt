package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView

class fitnesspage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fitnesspage)

        val beginner = findViewById<CardView>(R.id.beginnercv)
        beginner.setOnClickListener(){
            val Intent = Intent(this, beginnervideos::class.java)
            startActivity(Intent)
        }

        val intermediate = findViewById<CardView>(R.id.intermediatecv)
        intermediate.setOnClickListener(){
            val Intent = Intent(this, intermediatevideos::class.java)
            startActivity(Intent)
        }

        val advanced = findViewById<CardView>(R.id.advancedcv)
        advanced.setOnClickListener(){
            val Intent = Intent(this, advancedvideos::class.java)
            startActivity(Intent)
        }
    }
}