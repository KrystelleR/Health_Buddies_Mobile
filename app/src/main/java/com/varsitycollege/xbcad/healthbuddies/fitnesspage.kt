package com.varsitycollege.xbcad.healthbuddies

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
import com.google.firebase.database.FirebaseDatabase
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




    // we have assigned sensorManger to nullable
    private var sensorManager: SensorManager? = null

    // Creating a variable which will give the running status
    // and initially given the boolean value as false
    private var running = false

    private val REQUEST_ACTIVITY_RECOGNITION = 101

    // Creating a variable which will counts total steps
    // and it has been given the value of 0 float
    private var totalSteps = 0f

    // Creating a variable  which counts previous total
    // steps and it has also been given the value of 0 float
    private var previousTotalSteps = 0f
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

        //step counter
        loadData()
        resetSteps()

        val stepsgraph = findViewById< AppCompatButton>(R.id.viewprogressbtn1)
        val movemgraph = findViewById<AppCompatButton>(R.id.viewprogressbtn2)

        stepsgraph.setOnClickListener(){
            val Intent = Intent(this, fitnessgraph::class.java)
            startActivity(Intent)
        }

        movemgraph.setOnClickListener(){
            val Intent = Intent(this, fitnessgraph::class.java)
            startActivity(Intent)
        }

        // Adding a context of SENSOR_SERVICE as Sensor Manager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
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
        val userStepsUpdate = mapOf("_$hour" to totalSteps)
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

        // Calling the TextView that we made in activity_main.xml
        // by the id given to that TextView
        var tv_stepsTaken = findViewById<TextView>(R.id.totalstepstxt)

        if (running) {
            totalSteps = event!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()

            // Update steps in Firebase for the current hour
            updateStepsInFirebase(Calendar.getInstance().get(Calendar.HOUR_OF_DAY))

            // It will show the current steps to the user
            tv_stepsTaken.text = ("$currentSteps steps")
        }
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
