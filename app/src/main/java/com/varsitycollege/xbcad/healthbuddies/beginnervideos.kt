package com.varsitycollege.xbcad.healthbuddies

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.HashMap

class beginnervideos : AppCompatActivity() {
    var currentMoveMinutes : Int =0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_beginnervideos)


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
                                Toast.makeText(
                                    this@beginnervideos,
                                    "Minutes updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                edit.dismiss() // Dismiss the dialog after successful update
                            }
                            .addOnFailureListener { e ->
                                // Handle the error
                                Log.e(
                                    ContentValues.TAG,
                                    "Error updating minutes in the database",
                                    e
                                )
                                Toast.makeText(
                                    this@beginnervideos,
                                    "Error updating minutes",
                                    Toast.LENGTH_SHORT
                                ).show()
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
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/VsgpUHUYuJI?si=EJRYfg-1ccM8suSd\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video2 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/-y5yuLuw7KQ?si=4Pd_zAMlkCkmIRv7\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video3 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/mrXbsRPgnvY?si=_UXakzQj_7DABawH\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video4 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/Imhi98dHa5w?si=qLGGKm_qlaGVfUJv\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video5 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/udgOXaw4jbI?si=63RWc3fTEIHm8zFr\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video6 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/d3LPrhI0v-w?si=nac0nsqIp2tMn1o5\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video7 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/NRDFTWvEF40?si=Zg_fu981TZtqc-oe\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"
            val video8 =
                "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/nuIWMFOotko?si=5su6NaOVuvEP8ghs\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>"

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