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
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Handler
import android.os.Looper
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.util.Calendar

class fitnesspage :  AppCompatActivity(), SensorEventListener {
    // Added SensorEventListener the MainActivity class
    // Implement all the members in the class MainActivity
    // after adding SensorEventListener

    //database
    private val handler = Handler(Looper.getMainLooper())
    private val database = FirebaseDatabase.getInstance()
    private val usersRef1 = database.getReference("UserSteps")
    private val user = FirebaseAuth.getInstance().currentUser

    private var isSensorNotified = false

    // we have assigned sensorManger to nullable
    private var sensorManager: SensorManager? = null

    // Creating a variable which will give the running status
    // and initially given the boolean value as false
    private var running = false

    private val REQUEST_ACTIVITY_RECOGNITION = 101

    // Creating a variable which will counts total steps
    // and it has been given the value of 0 float
    private var totalSteps = 0f
    var stepGoal : Int = 0
    var stepsleft: Int =0
    var moveminutes: Int =0
    var mmgoal: Int =0


    // Creating a variable  which counts previous total
    // steps and it has also been given the value of 0 float
    private var previousTotalSteps = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Hide the ActionBar
        supportActionBar?.hide()
        setContentView(R.layout.activity_fitnesspage)


        val stepstv = findViewById<TextView>(R.id.totalstepstxt)
        stepstv.text = totalSteps.toString()

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
                        val UserDetails = dataSnapshot.getValue(data.UserDetails::class.java)
                        // Now you can use the userDetails object as needed
                        if (UserDetails != null) {
                            stepGoal = UserDetails.dailySteps.toInt()
                            mmgoal = UserDetails.moveMinutes.toInt()

                            val dailygoal = findViewById<TextView>(R.id.dailygoaltxt)
                            dailygoal.text = "$stepGoal steps"
                            stepsleft = stepGoal - totalSteps.toInt()

                            val leftSteps = findViewById<TextView>(R.id.stepslefttxt)
                            leftSteps.text = "$stepsleft steps"

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


            val userRef1 = database.getReference("UserMinutes").child(userUid) // Corrected line

            userRef1.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot contains the user details data
                        val UserMinutes = dataSnapshot.getValue(data.UserMinutes::class.java)
                        // Now you can use the userDetails object as needed
                        if (UserMinutes != null) {
                            moveminutes = UserMinutes.minutes

                            val moveminutestv = findViewById<TextView>(R.id.moveminutestxt)
                            moveminutestv.text = "$moveminutes/ $mmgoal minutes"
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

            //step counter
            loadData()
            resetStepsAndMinutes()

            val beginner = findViewById<CardView>(R.id.beginnercv)
            beginner.setOnClickListener() {
                val Intent = Intent(this, beginnervideos::class.java)
                startActivity(Intent)
            }

            val intermediate = findViewById<CardView>(R.id.intermediatecv)
            intermediate.setOnClickListener() {
                val Intent = Intent(this, intermediatevideos::class.java)
                startActivity(Intent)
            }

            val advanced = findViewById<CardView>(R.id.advancedcv)
            advanced.setOnClickListener() {
                val Intent = Intent(this, advancedvideos::class.java)
                startActivity(Intent)
            }

            val stepsgraph = findViewById<AppCompatButton>(R.id.viewprogressbtn1)
            val movemgraph = findViewById<AppCompatButton>(R.id.viewprogressbtn2)

            stepsgraph.setOnClickListener() {
                val Intent = Intent(this, fitnessgraph::class.java)
                startActivity(Intent)
            }

            movemgraph.setOnClickListener() {
                val Intent = Intent(this, fitnessgraph::class.java)
                startActivity(Intent)
            }

            // Adding a context of SENSOR_SERVICE as Sensor Manager
            sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
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
        // Schedule the task to update steps every hour
        scheduleHourlyTask()
    }

    private fun scheduleHourlyTask() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val millisUntilNextHour = (60 - calendar.get(Calendar.MINUTE)) * 60 * 1000

        handler.postDelayed(object : Runnable {
            override fun run() {
                // Update steps in Firebase for the current hour
                updateStepsInFirebase(currentHour)
                // Schedule the next task for the next hour
                scheduleHourlyTask()
            }
        }, millisUntilNextHour.toLong())
    }

    private fun updateStepsInFirebase(hour: Int) {
        var theAmount = "_$hour" + "h00"
        val userStepsUpdate = mapOf(theAmount to totalSteps)
        usersRef1.child(user?.uid ?: "").updateChildren(userStepsUpdate)
            .addOnSuccessListener {
                Log.d("fitnesspage", "Steps updated for hour $hour")
            }
            .addOnFailureListener {
                Log.e("fitnesspage", "Failed to update steps for hour $hour", it)
            }
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
            if (event != null) {
                val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

                // Check if it's a new day
                if (currentHour == 0) {
                    // It's a new day, reset steps and minutes
                    resetStepsAndMinutes()
                }

                totalSteps = event.values[0]
                val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()

                updateStepsInFirebase(currentHour)

                tv_stepsTaken.text = ("$currentSteps steps")
            } else {
                if (!isSensorNotified) {
                    Toast.makeText(this, "Step Counter Not Found on Device", Toast.LENGTH_SHORT).show()
                    isSensorNotified = true
                }
            }
        }
    }

    private fun resetStepsAndMinutes() {
        // Reset steps
        previousTotalSteps = totalSteps
        val tv_stepsTaken = findViewById<TextView>(R.id.totalstepstxt)
        tv_stepsTaken.text = "0"

        // Reset minutes
        moveminutes = 0
        val moveminutestv = findViewById<TextView>(R.id.moveminutestxt)
        moveminutestv.text = "0 / $mmgoal minutes"

        // Update UI and save data
        updateStepsInFirebase(0)  // Assuming you want to update steps for the first hour of the new day
        saveData()
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
