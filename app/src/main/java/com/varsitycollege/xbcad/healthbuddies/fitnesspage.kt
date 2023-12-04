package com.varsitycollege.xbcad.healthbuddies

import android.content.ContentValues
import android.content.Intent
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text

class fitnesspage :  AppCompatActivity(), SensorEventListener {

    private var sensorManager: SensorManager? = null
    private var running = false
    private val REQUEST_ACTIVITY_RECOGNITION = 101
    private var totalSteps = 0f
    private var previousTotalSteps = 0f

    private var currentMove: Int =0
    private var moveGoal: Int =0
    private var stepsGoal:Int =0

    // Firebase
    private lateinit var database: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_fitnesspage)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        databaseReference = database.getReference("UserSteps") // Change "userSteps" to your desired path

        //step counter
        loadData()
        resetSteps()

        // Adding a context of SENSOR_SERVICE as Sensor Manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

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
                            //get goals
                            val goalSteps = userDetails.dailySteps
                            val goalMove = userDetails.moveMinutes

                            val dailygoaltv = findViewById<TextView>(R.id.dailygoaltxt)
                            dailygoaltv.text  = goalSteps.toString() + " steps"
                            stepsGoal = userDetails.dailySteps

                            val leftSteps = findViewById<TextView>(R.id.stepslefttxt)
                            leftSteps.text = calculateLeftSteps(goalSteps, totalSteps.toInt()).toString() + " steps"
                            val remainingSteps = calculateLeftSteps(stepsGoal, totalSteps.toInt())

                            // Assuming you have the ProgressBar reference in your activity or fragment
                            val progressBar: ProgressBar = findViewById(R.id.pbTimer)
                            val maxValue = stepsGoal // Set your desired maximum value here
                            progressBar.max = maxValue
                            val progressValue = totalSteps.toInt() // Set your desired progress value here
                            progressBar.progress = progressValue


                            if (remainingSteps <= 0) {
                                leftSteps.text = "Well done on completing your goal! You have earned 10 coins!\n"

                                val userGoalRef = database.getReference("UserCollectPoints").child(userUid)
                                userGoalRef?.child("stepsGoal")?.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                                        // Check if the value is not null before converting to Boolean
                                        val hasStepsGoal = dataSnapshot.getValue(Boolean::class.java)

                                        if (hasStepsGoal == null || !hasStepsGoal) {
                                            userRef?.child("userCurrency")?.addListenerForSingleValueEvent(object :
                                                ValueEventListener {
                                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        // dataSnapshot contains the user currency data
                                                        var userCurrencyValue = dataSnapshot.getValue(Int::class.java)

                                                        // Check if the value is not null before converting to Int
                                                        if (userCurrencyValue != null) {
                                                            // Add 10 points for completion
                                                            userCurrencyValue += 10
                                                            userRef?.child("userCurrency")?.setValue(userCurrencyValue)

                                                            // Set the stepsGoal to true
                                                            userGoalRef?.child("stepsGoal")?.setValue(true)
                                                        }
                                                    } else {
                                                        Log.d(ContentValues.TAG, "User currency data does not exist")
                                                    }
                                                }

                                                override fun onCancelled(databaseError: DatabaseError) {
                                                    Log.e(
                                                        ContentValues.TAG,
                                                        "Error reading user currency from the database",
                                                        databaseError.toException()
                                                    )
                                                }
                                            })
                                        }
                                    }

                                    override fun onCancelled(databaseError: DatabaseError) {
                                        Log.e(
                                            ContentValues.TAG,
                                            "Error reading stepsGoal from the database",
                                            databaseError.toException()
                                        )
                                    }
                                })
                            } else {
                                leftSteps.text = "$remainingSteps steps"
                            }



                            val userRefMinutes = database.getReference("UserMinutes").child(userUid)
                            userRefMinutes.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(dataSnapshot: DataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // dataSnapshot contains the user details data
                                        val UserMinutes =
                                            dataSnapshot.getValue(data.UserMinutes::class.java)
                                        // Now you can use the userDetails object as needed
                                        if (UserMinutes != null) {

                                            var myMoveMinutes = UserMinutes.minutes
                                            val dailymovetv =
                                                findViewById<TextView>(R.id.moveminutestxt)
                                            dailymovetv.text = " $myMoveMinutes/ $goalMove minutes"

                                            val progressBarNutrition: ProgressBar = findViewById(R.id.progresbar_nutrition)
                                            val maxValueNutrition = goalMove // Set your desired maximum value here
                                            progressBarNutrition.max = maxValueNutrition
                                            val progressValueNutrition = myMoveMinutes // Set your desired progress value here
                                            progressBarNutrition.progress = progressValueNutrition



                                            if (myMoveMinutes >= goalMove) {
                                                val congrats =
                                                    findViewById<TextView>(R.id.congratsmovetxt)
                                                congrats.text =
                                                    "Well done on completing your move goal! You have earned 10 coins!\n"


                                                // Check if the user has already earned points for moveGoal
                                                val userGoalRef =
                                                    database.getReference("UserCollectPoints")
                                                        .child(userUid)
                                                userGoalRef?.child("moveGoal")
                                                    ?.addListenerForSingleValueEvent(object :
                                                        ValueEventListener {
                                                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                                                            // Check if the value is not null before converting to Boolean
                                                            val hasMoveGoal =
                                                                dataSnapshot.getValue(Boolean::class.java)

                                                            if (hasMoveGoal == null || !hasMoveGoal) {
                                                                userRef?.child("userCurrency")
                                                                    ?.addListenerForSingleValueEvent(
                                                                        object :
                                                                            ValueEventListener {
                                                                            override fun onDataChange(
                                                                                dataSnapshot: DataSnapshot
                                                                            ) {
                                                                                if (dataSnapshot.exists()) {
                                                                                    // dataSnapshot contains the user currency data
                                                                                    var userCurrencyValue =
                                                                                        dataSnapshot.getValue(
                                                                                            Int::class.java
                                                                                        )

                                                                                    // Check if the value is not null before converting to Int
                                                                                    if (userCurrencyValue != null) {
                                                                                        // Add 10 points for completing moveGoal
                                                                                        userCurrencyValue += 10
                                                                                        userRef?.child(
                                                                                            "userCurrency"
                                                                                        )?.setValue(
                                                                                            userCurrencyValue
                                                                                        )

                                                                                        // Set the moveGoal to true
                                                                                        userGoalRef?.child(
                                                                                            "moveGoal"
                                                                                        )?.setValue(
                                                                                            true
                                                                                        )
                                                                                    }
                                                                                } else {
                                                                                    Log.d(
                                                                                        ContentValues.TAG,
                                                                                        "User currency data does not exist"
                                                                                    )
                                                                                }
                                                                            }

                                                                            override fun onCancelled(
                                                                                databaseError: DatabaseError
                                                                            ) {
                                                                                Log.e(
                                                                                    ContentValues.TAG,
                                                                                    "Error reading user currency from the database",
                                                                                    databaseError.toException()
                                                                                )
                                                                            }
                                                                        })
                                                            }
                                                        }

                                                        override fun onCancelled(databaseError: DatabaseError) {
                                                            Log.e(
                                                                ContentValues.TAG,
                                                                "Error reading moveGoal from the database",
                                                                databaseError.toException()
                                                            )
                                                        }
                                                    })
                                            }


                                        } else {
                                            Log.d(ContentValues.TAG, "User details do not exist")
                                        }
                                    }
                                }

                                override fun onCancelled(databaseError: DatabaseError) {
                                    Log.e(ContentValues.TAG, "Error reading user details from the database", databaseError.toException())
                                }
                            })
                        }






                    } else {
                        Log.d(ContentValues.TAG, "User details do not exist")
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
        }











        //navigation
        val beginner = findViewById<CardView>(R.id.beginnercv)
        beginner.setOnClickListener(){
            val intent = Intent(this, beginnervideos::class.java)
            startActivity(intent)
        }

        val intermediate = findViewById<CardView>(R.id.intermediatecv)
        intermediate.setOnClickListener(){
            val intent = Intent(this, intermediatevideos::class.java)
            startActivity(intent)
        }

        val advanced = findViewById<CardView>(R.id.advancedcv)
        advanced.setOnClickListener(){
            val intent = Intent(this, advancedvideos::class.java)
            startActivity(intent)
        }

        val progress1 = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.viewprogressbtn1)
        val progress2 = findViewById<androidx.appcompat.widget.AppCompatButton>(R.id.viewprogressbtn2)

        progress1.setOnClickListener(){
            val intent = Intent(this, fitnessgraph::class.java)
            startActivity(intent)
        }
        progress2.setOnClickListener(){
            val intent = Intent(this, fitnessgraph::class.java)
            startActivity(intent)
        }
    }



    override fun onResume() {
        super.onResume()
        running = true

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACTIVITY_RECOGNITION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
                REQUEST_ACTIVITY_RECOGNITION
            )
        } else {
            // Permission is granted, register the sensor listener
            registerStepCounterSensor()
        }
    }

    public fun calculateLeftSteps(goalSteps:Int, currentSteps:Int):Int{
        val leftSteps = goalSteps - currentSteps
        return leftSteps
    }

    private fun registerStepCounterSensor() {
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_ACTIVITY_RECOGNITION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, register the sensor listener
                registerStepCounterSensor()
            } else {
                // Permission denied, handle accordingly (e.g., show a message to the user)
                Toast.makeText(this, "Step Counter Not Found on Device", Toast.LENGTH_SHORT).show()
            }
        }
    }

        override fun onSensorChanged(event: SensorEvent?) {
            var tv_stepsTaken = findViewById<TextView>(R.id.totalstepstxt)

            if (running) {
                totalSteps = event!!.values[0]
                val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
                tv_stepsTaken.text = ("$currentSteps steps")

                val leftSteps = findViewById<TextView>(R.id.stepslefttxt)
                val remainingSteps = calculateLeftSteps(stepsGoal, totalSteps.toInt())

                if (remainingSteps <= 0) {
                    leftSteps.text = "Well done on completing your goal! You have earned 10 coins!\n"
                } else {
                    leftSteps.text = "$remainingSteps steps"
                }

                // Update the corresponding hour in the database
                updateDatabaseHour(currentSteps)
            }
        }


    private fun updateDatabaseHour(steps: Int) {
        // Get the current hour in 24-hour format
        val currentHour = java.time.LocalTime.now().hour
        val currentUser = Firebase.auth.currentUser?.uid
        // Construct the field name based on the current hour
        val fieldName = "_${String.format("%02dh00", currentHour)}"

        // Update the database with the current steps for the corresponding hour
        databaseReference.child("$currentUser").child(fieldName).setValue(steps)
    }

    fun resetSteps() {
        var tv_stepsTaken = findViewById<TextView>(R.id.totalstepstxt)
        tv_stepsTaken.setOnClickListener {
            // This will give a toast message if the user want to reset the steps
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }

        tv_stepsTaken.setOnLongClickListener {

            previousTotalSteps = totalSteps

            // When the user will click long tap on the screen,
            // the steps will be reset to 0
            tv_stepsTaken.text = 0.toString()

            // This will save the data
            saveData()

            true
        }
    }

    private fun saveData() {

        // Shared Preferences will allow us to save
        // and retrieve data in the form of key,value pair.
        // In this function we will save data
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)

        val editor = sharedPreferences.edit()
        editor.putFloat("key1", previousTotalSteps)
        editor.apply()
    }

    private fun loadData() {

        // In this function we will retrieve data
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber = sharedPreferences.getFloat("key1", 0f)

        // Log.d is used for debugging purposes
        Log.d("MainActivity", "$savedNumber")

        previousTotalSteps = savedNumber
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // We do not have to write anything in this function for this app
    }
}