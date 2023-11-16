package com.varsitycollege.xbcad.healthbuddies

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.*
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class settingspage : AppCompatActivity() {

    var myuid: String=""
    var mysetDetails: Boolean =false
    var myusername: String? =""
    var myemail: String =""
    var myage: Int =0
    var myheight: String = ""
    var myweight: String = ""
    var mymetric: Boolean=  true
    var myimperial:Boolean= false
    var myprofileimg: String=""
    var mydailysteps: Int=0
    var mymoveminutes: Int=0
    var mygoalweight: String = ""
    var mywatergoal: Int =0
    var mysleepgoal: Int =0
    var mydailycalories: Int =0
    var mygender: String = ""
    var myaboutme: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settingspage)

        //get all values
        val usernametv = findViewById<EditText>(R.id.usernametxt)
        val agetv = findViewById<Spinner>(R.id.ageSpinner)
        val heighttv = findViewById<TextView>(R.id.heighttxt)
        val weighttv = findViewById<TextView>(R.id.weighttxt)
        val gendertv = findViewById<TextView>(R.id.gendertxt)
        val emailtv = findViewById<TextView>(R.id.emailtxt)
        val metricsw = findViewById<Switch>(R.id.metricswitch)
        val imperialsw = findViewById<Switch>(R.id.imperialswitch)
        val stepstv = findViewById<TextView>(R.id.stepstxt)
        val myweighttv = findViewById<TextView>(R.id.myweighttxt)
        val minutestv = findViewById<TextView>(R.id.minutestxt)
        val dailywatertv = findViewById<TextView>(R.id.dailywatertxt)
        val caloriestv = findViewById<TextView>(R.id.caloriestxt)
        val sleeptv = findViewById<TextView>(R.id.sleeptxt)


        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.age_array,
            android.R.layout.simple_spinner_item
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        agetv.adapter = adapter

        // Set a listener to handle the selected item
        agetv.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedAge = parent.getItemAtPosition(position) as String
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Handle no selection if needed
            }
        }


        val pickImage = findViewById<androidx.appcompat.widget.AppCompatImageButton>(R.id.add_image)
        pickImage.setOnClickListener() {
            pickImage()
        }


        val database = FirebaseDatabase.getInstance()

// Getting user details from db
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            // Assuming userDetails.uid is the user's UID
            val userUid = currentUser.uid
            // Reference to the user's data in the Realtime Database
            val userRef = database.getReference("Users").child(userUid) // Corrected line

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot contains the user details data
                        val userDetails = dataSnapshot.getValue(data.UserDetails::class.java)

                        // Now you can use the userDetails object as needed
                        if (userDetails != null) {
                            myuid = currentUser.uid
                            myusername = userDetails.username
                            myemail = userDetails.email
                            myage = userDetails.age
                            myheight = userDetails.height
                            myweight = userDetails.weight
                            mymetric = userDetails.metric
                            myimperial = userDetails.imperial
                            myprofileimg = userDetails.profileImage
                            mysetDetails = userDetails.setDetails
                            mygender = userDetails.gender
                            myaboutme = userDetails.aboutMe

                            Toast.makeText(this@settingspage, "my height: ${myheight}", Toast.LENGTH_SHORT).show()

                            // Set the TextView values here
                            heighttv.text = myheight.toString()
                            weighttv.text = myweight.toString()
                            gendertv.text = mygender
                            emailtv.text = myemail
                        }
                    } else {
                        Log.d(TAG, "User details do not exist")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Error reading user details from the database", databaseError.toException())
                }
            })

            // Listener to retrieve user goals
            userRef.child("user_goals").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot contains the user goals data
                        val userGoals = dataSnapshot.getValue(data.UserGoals::class.java)

                        // Now you can use the userGoals object as needed
                        if (userGoals != null) {
                            mydailysteps = userGoals.dailySteps
                            mygoalweight = userGoals.goalWeight
                            mymoveminutes = userGoals.moveMinutes
                            mywatergoal = userGoals.dailyWaterAmount
                            mysleepgoal = userGoals.sleep
                            mydailycalories = userGoals.dailyCalories

                            Toast.makeText(this@settingspage, "my daily steps: ${mydailysteps}", Toast.LENGTH_SHORT).show()

                            // Set the TextView values here
                            stepstv.text = mydailysteps.toString()
                            myweighttv.text = mygoalweight.toString()
                            minutestv.text = mymoveminutes.toString()
                            dailywatertv.text = mywatergoal.toString()
                            caloriestv.text = mydailycalories.toString()
                            sleeptv.text = mysleepgoal.toString()
                        }
                    } else {
                        Log.d(TAG, "User goals do not exist")
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(TAG, "Error reading user goals from the database", databaseError.toException())
                }
            })





            /*
            heighttv = findViewById<EditText>(R.id.heighttxt)
        val weighttv = findViewById<EditText>(R.id.weighttxt)
        val gendertv = findViewById<EditText>(R.id.gendertxt)
        val emailtv = findViewById<EditText>(R.id.emailtxt)
        val metricsw = findViewById<Switch>(R.id.metricswitch)
        val imperialsw = findViewById<Switch>(R.id.imperialswitch)
        val stepstv = findViewById<EditText>(R.id.stepstxt)
        val myweighttv = findViewById<EditText>(R.id.myweighttxt)
        val minutestv = findViewById<EditText>(R.id.minutestxt)
        val dailywatertv = findViewById<EditText>(R.id.dailywatertxt)
        val caloriestv = findViewById<EditText>(R.id.caloriestxt)
        val sleeptv
            */

        }


    }

    private fun pickImage() {
        val edit = Dialog(this)
        edit.setContentView(R.layout.pickimagedialog)

        val img1 = edit.findViewById<CardView>(R.id.img1cv)
        val img2 = edit.findViewById<CardView>(R.id.img2cv)
        val img3 = edit.findViewById<CardView>(R.id.img3cv)
        val img4 = edit.findViewById<CardView>(R.id.img4cv)
        val img5 = edit.findViewById<CardView>(R.id.img5cv)
        val img6 = edit.findViewById<CardView>(R.id.img6cv)
        val img7 = edit.findViewById<CardView>(R.id.img7cv)
        val img8 = edit.findViewById<CardView>(R.id.img8cv)
        val img9 = edit.findViewById<CardView>(R.id.img9cv)
        val img10 = edit.findViewById<CardView>(R.id.img10cv)
        val img11 = edit.findViewById<CardView>(R.id.img11cv)
        val img12 = edit.findViewById<CardView>(R.id.img12cv)

        img1.setOnClickListener(){
            setProfileImage(R.drawable.profileimg1)
            edit.dismiss()
        }
        img2.setOnClickListener(){
            setProfileImage(R.drawable.profileimg2)
            edit.dismiss()
        }
        img3.setOnClickListener(){
            setProfileImage(R.drawable.profileimg3)
            edit.dismiss()
        }
        img4.setOnClickListener(){
            setProfileImage(R.drawable.profileimg4)
            edit.dismiss()
        }
        img5.setOnClickListener(){
            setProfileImage(R.drawable.profileimg5)
            edit.dismiss()
        }
        img6.setOnClickListener(){
            setProfileImage(R.drawable.profileimg6)
            edit.dismiss()
        }
        img7.setOnClickListener(){
            setProfileImage(R.drawable.profileimg7)
            edit.dismiss()
        }
        img8.setOnClickListener(){
            setProfileImage(R.drawable.profileimg8)
            edit.dismiss()
        }
        img9.setOnClickListener(){
            setProfileImage(R.drawable.profileimg9)
            edit.dismiss()
        }
        img10.setOnClickListener(){
            setProfileImage(R.drawable.profileimg10)
            edit.dismiss()
        }
        img11.setOnClickListener(){
            setProfileImage(R.drawable.profileimg11)
            edit.dismiss()
        }
        img12.setOnClickListener(){
            setProfileImage(R.drawable.profileimg12)
            edit.dismiss()
        }

        edit.show()
    }

    private fun setProfileImage(resourceId: Int) {
        val profileImageView = findViewById<ImageView>(R.id.profile_image)
        profileImageView.setImageResource(resourceId)
    }
}