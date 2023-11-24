package com.varsitycollege.xbcad.healthbuddies

import android.app.Dialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import java.util.*

class advancedvideos : AppCompatActivity() {

    var currentMoveMinutes : Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_advancedvideos)


        val myMinutes = findViewById<TextView>(R.id.currentmmtxt)


        val database = FirebaseDatabase.getInstance()
        // Getting user details from db
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            // Assuming userDetails.uid is the user's UID
            val userUid = currentUser.uid
            // Reference to the user's data in the Realtime Database
            val userRef = database.getReference("UserMinutes").child(userUid) // Corrected line

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot contains the user details data
                        val userMinutes = dataSnapshot.getValue(data.UserMinutes::class.java)
                        // Now you can use the userDetails object as needed
                        if (userMinutes != null) {
                            currentMoveMinutes = userMinutes.minutes
                            myMinutes.text = currentMoveMinutes.toString()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(
                        ContentValues.TAG,
                        "Error reading user details from the database",
                        databaseError.toException()
                    )
                }
            })


            val addbtn = findViewById<Button>(R.id.editmmbtn)
            addbtn.setOnClickListener {
                val edit = Dialog(this)
                edit.setContentView(R.layout.addmmdialog)

                // Use edit.findViewById to get views inside the dialog
                val number = edit.findViewById<TextView>(R.id.value)
                number.text = currentMoveMinutes.toString()

                val adding = edit.findViewById<ImageButton>(R.id.addbtn)
                val subtracting = edit.findViewById<ImageButton>(R.id.minusbtn)

                val editvalue = edit.findViewById<Button>(R.id.enterbtn)
                editvalue.setOnClickListener {
                    // Update the user's minutes in the Realtime Database
                    val currentUser = Firebase.auth.currentUser
                    if (currentUser != null) {
                        val userUid = currentUser.uid
                        val userRef = database.getReference("UserMinutes").child(userUid)

                        // Create a HashMap to update the minutes field
                        val updates = HashMap<String, Any>()
                        updates["minutes"] = currentMoveMinutes

                        // Update the value in the database
                        userRef.updateChildren(updates)
                            .addOnSuccessListener {
                                val myMinutes = findViewById<TextView>(R.id.currentmmtxt)
                                myMinutes.text = currentMoveMinutes.toString()
                                // Update was successful
                                Toast.makeText(this@advancedvideos, "Minutes updated successfully", Toast.LENGTH_SHORT).show()
                                edit.dismiss() // Dismiss the dialog after successful update
                            }
                            .addOnFailureListener { e ->
                                // Handle the error
                                Log.e(ContentValues.TAG, "Error updating minutes in the database", e)
                                Toast.makeText(this@advancedvideos, "Error updating minutes", Toast.LENGTH_SHORT).show()
                            }
                    }
                }


                adding.setOnClickListener {
                    if (currentMoveMinutes <= 720) {
                        currentMoveMinutes++
                        number.text = currentMoveMinutes.toString()
                    }
                }

                subtracting.setOnClickListener {
                    if (currentMoveMinutes != 0) {
                        currentMoveMinutes--
                        number.text = currentMoveMinutes.toString()
                    }
                }

                // Show the dialog
                edit.show()
            }


            val myvideo1 = findViewById<WebView>(R.id.video1)
            val myvideo2 = findViewById<WebView>(R.id.video2)
            val myvideo3 = findViewById<WebView>(R.id.video3)
            val myvideo4 = findViewById<WebView>(R.id.video4)
            val myvideo5 = findViewById<WebView>(R.id.video5)
            val myvideo6 = findViewById<WebView>(R.id.video6)
            val myvideo7 = findViewById<WebView>(R.id.video7)
            val myvideo8 = findViewById<WebView>(R.id.video8)

            val video1 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/GbjtG26ZSAo?si=LrUcZWuowUV-cSav\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video2 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/W-1bDW1ujsE?si=LHpsvCanBm16gfQd\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video3 =
                "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/6e2ibUq65tA?si=ym3s0KCleA5g5UUm\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video4 =
                "<iframe width=\"560\" height=\"315\" src=\"https://www.youtube.com/embed/5F3o8iMQ_Lo?si=ZUuwnCWv6_jb7pBq\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video5 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/WpIFlh5whcs?si=flFkARoXaSbboCX7\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video6 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/lc1Ag9m7XQo?si=fBU_XEi0z-g6lGjx\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video7 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/m4GrRPbrb3Y?si=ASK10WoN4keDONFP\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video8 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/FNFYZ2n90RI?si=l0-EjpYhXd8ikhjB\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"


            myvideo1.loadData(video1, "text/html", "utf-8")
            myvideo1.settings.javaScriptEnabled = true
            myvideo1.webChromeClient = WebChromeClient()

            myvideo2.loadData(video2, "text/html", "utf-8")
            myvideo2.settings.javaScriptEnabled = true
            myvideo2.webChromeClient = WebChromeClient()

            myvideo3.loadData(video3, "text/html", "utf-8")
            myvideo3.settings.javaScriptEnabled = true
            myvideo3.webChromeClient = WebChromeClient()

            myvideo4.loadData(video4, "text/html", "utf-8")
            myvideo4.settings.javaScriptEnabled = true
            myvideo4.webChromeClient = WebChromeClient()

            myvideo5.loadData(video5, "text/html", "utf-8")
            myvideo5.settings.javaScriptEnabled = true
            myvideo5.webChromeClient = WebChromeClient()

            myvideo6.loadData(video6, "text/html", "utf-8")
            myvideo6.settings.javaScriptEnabled = true
            myvideo6.webChromeClient = WebChromeClient()

            myvideo7.loadData(video7, "text/html", "utf-8")
            myvideo7.settings.javaScriptEnabled = true
            myvideo7.webChromeClient = WebChromeClient()

            myvideo8.loadData(video8, "text/html", "utf-8")
            myvideo8.settings.javaScriptEnabled = true
            myvideo8.webChromeClient = WebChromeClient()
        }
    }
}