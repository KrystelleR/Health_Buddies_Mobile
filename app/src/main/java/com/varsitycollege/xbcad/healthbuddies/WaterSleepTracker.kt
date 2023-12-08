package com.varsitycollege.xbcad.healthbuddies

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.Calendar

class WaterSleepTracker : Fragment(),TimePickerDialog.OnTimeSetListener {

    private lateinit var customWaterEditText: EditText
    private lateinit var saveButton: AppCompatButton
    private lateinit var btnWentToSleep: AppCompatButton
    private lateinit var btnWokeUp: AppCompatButton
    private lateinit var txtHrsSlept : TextView
    private lateinit var userWaterRef: DatabaseReference
    private lateinit var userSleepRef: DatabaseReference
    private lateinit var daysSpinner: Spinner
    private lateinit var userId: String

    private lateinit var switch250ml: Switch
    private lateinit var switch500ml: Switch
    private lateinit var switch1000ml: Switch


    var btnTracker =0
    var wentToSleepHr=0
    var wentToSleepMinute = 0
    var wokeUpHour =0
    var wokeUpMinue =0

    var day =0
    var month =0
    var year =0
    var hour=0
    var minute =0

    var savedDay =0
    var savedMonth =0
    var savedYear =0
    var savedHour=0
    var savedMinute =0

    var dayOfWeek =" "


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_water_sleep_tracker, container, false)


        daysSpinner =view.findViewById(R.id.days_spinner)

        // Initialize your views here using the inflated view
        customWaterEditText = view.findViewById(R.id.txt_customWater)
        saveButton = view.findViewById(R.id.btnSaveWaterSleep)
        switch250ml = view.findViewById(R.id.switch_250)
        switch500ml = view.findViewById(R.id.switch_500)
        switch1000ml = view.findViewById(R.id.switch_1000)

        btnWentToSleep=view.findViewById(R.id.btn_went_to_sleep)
        btnWokeUp=view.findViewById(R.id.btn_woke_up)
        txtHrsSlept=view.findViewById(R.id.txt_hrs_slept)


        //select the date and time of when the user went to sleep and woke up
        btnWentToSleep.setOnClickListener {

            btnTracker=1
           pickDate()
        }

        btnWokeUp.setOnClickListener {
            btnTracker=2
            pickDate()
        }

        // days slept spinner code
        val spinner: Spinner = view.findViewById(R.id.days_spinner)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_days_slept,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // Set up your Firebase reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        userId = currentUser?.uid ?: ""


        if (userId != null) {
            userWaterRef = FirebaseDatabase.getInstance().getReference("UserWater").child(userId)
            userSleepRef =FirebaseDatabase.getInstance().getReference("UserSleepHours").child(userId)
        }

        // Add a listener to each Switch to handle exclusive selection
        val switches = listOf(switch250ml, switch500ml, switch1000ml)
        for (currentSwitch in switches) {
            currentSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // If the current switch is checked, uncheck other switches
                    for (otherSwitch in switches) {
                        if (otherSwitch != currentSwitch) {
                            otherSwitch.isChecked = false
                        }
                    }
                }
            }
        }



        saveButton.setOnClickListener {
            // Check if water data is provided
            val customWaterText = customWaterEditText.text.toString().trim()

            // Flag to indicate whether water data is provided
            val isWaterDataProvided = customWaterText.isNotEmpty()&& isNumeric(customWaterText) || switch250ml.isChecked || switch500ml.isChecked || switch1000ml.isChecked

            // Check if sleep data is provided
            val period: String = daysSpinner.selectedItem.toString()
            val isSleepDataProvided = period.isNotEmpty() && btnWokeUp.text != "- : - -" && btnWentToSleep.text != "- : - -" && txtHrsSlept.text != "add hours"

            // Build a single confirmation dialog for both water and sleep data
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Confirmation")
            builder.setMessage("Do you want to proceed with saving these values?")


            var isSleepFormatValid =true
            var isWaterFormatValid =true
            builder.setPositiveButton("Yes") { _, _ ->

                // If water data is provided, update water data
                if (isWaterDataProvided==true) {
                    updateWaterData()

                }else if(customWaterEditText.text.isEmpty()){

                }else if(isWaterDataProvided==false){
                    Toast.makeText(requireContext(), "Failed to update water :( Please enter a valid water amount", Toast.LENGTH_LONG).show()
                    isWaterFormatValid=false
                }

                // If sleep data is provided, update sleep data
                if (isSleepDataProvided) {
                    updateSleepData()
                }


                if(isWaterFormatValid==true){

                    if((period.isEmpty() && txtHrsSlept.text != "add hours")||(period.isEmpty() && btnWokeUp.text != "- : - -") || (period.isEmpty() &&btnWentToSleep.text != "- : - -")){
                        Toast.makeText(requireContext(), "Failed to update sleep :( Please enter all sleep details", Toast.LENGTH_LONG).show()
                    }else if ((period.isNotEmpty() && btnWokeUp.text == "- : - -")|| (period.isNotEmpty() && btnWentToSleep.text == "- : - -" ) ||(period.isNotEmpty() && txtHrsSlept.text == "add hours")||(period.isNotEmpty() && txtHrsSlept.text == "add hours"&& btnWentToSleep.text == "- : - -" && btnWokeUp.text == "- : - -"))
                    {
                        Toast.makeText(requireContext(), "Failed to update sleep :( Please enter all sleep details", Toast.LENGTH_LONG).show()
                    }

                    else{
                        // Show success dialog
                        showSuccessDialog()
                    }

                }



            }

            builder.setNegativeButton("No") { _, _ ->
                // Handle negative action if needed
            }

            builder.show()
        }


        // Get the selected item






        return view
    }

    // Function to update water data
    private fun updateWaterData() {
        val customWaterText = customWaterEditText.text.toString().trim()

        var customWaterAmount: Int
        if (customWaterText.isEmpty()) {
            customWaterAmount = 0
        } else {
            customWaterAmount = customWaterText.toDouble().toInt()
        }

        // Get the selected radio button ID
        val selectedRadioButtonId = getSelectedSwitch()

        // Update UserWater node in the database based on the selected radio button
        if (userId != null) {
            Log.d("WaterTracker","ondatachange triggered")
            // Retrieve the current amount
            userWaterRef.child("Amount").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val currentAmount = snapshot.getValue(Int::class.java) ?: 0

                    val newAmount = currentAmount + customWaterAmount + getSwitchAmount(selectedRadioButtonId)
                    userWaterRef.child("Amount").setValue(newAmount)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                    Toast.makeText(requireContext(), "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
            Log.d("WaterTracker","after onDataChange")
        }
    }

    // Function to update sleep data
    private fun updateSleepData() {
        val period: String = daysSpinner.selectedItem.toString()

        // Convert txtHrsSlept.text to Int
        val hrsSleptText = txtHrsSlept.text.toString()
        if (hrsSleptText.isNotEmpty()) {
            try {
                // Update UserSleep node in the database based on the selected period
                userSleepRef.child("$period").child("Hours").setValue(hrsSleptText.toInt())
            } catch (e: NumberFormatException) {
                // Handle the case where txtHrsSlept.text is not a valid number
                Toast.makeText(requireContext(), "Invalid sleep hours format.", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle the case where txtHrsSlept.text is empty
            Toast.makeText(requireContext(), "Please add sleep hours.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun getDateTimeCalendar(){
        val cal=Calendar.getInstance()
        // assign values to variable declared above
        day =cal.get(Calendar.DAY_OF_MONTH)
        month =cal.get(Calendar.MONTH)
        year =cal.get(Calendar.YEAR)
        hour =cal.get(Calendar.HOUR)
        minute =cal.get(Calendar.MINUTE)

    }

    private fun pickDate() {
        getDateTimeCalendar()
        TimePickerDialog(requireContext(), this, hour, minute, true).show()
        //DatePickerDialog(requireContext(), this, year, month, day).show()
    }


    private fun showConfirmationDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Confirmation")
        builder.setMessage("Save sleep hours ?")

        builder.setPositiveButton("Yes") { _, _ ->
            // Show success dialog
            showSuccessDialog()
        }

        builder.setNegativeButton("No") { _, _ ->
            // Handle negative action if needed
        }

        builder.show()
    }

    private fun showSuccessDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Success")
        builder.setMessage("Data has been successfully saved! Returning to Home page :)")

        builder.setPositiveButton("OK") { _, _ ->

            // Navigate to MainActivity
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(intent)
            //requireActivity().finish() // Optional: finish the current activity if needed
        }

        builder.show()
    }
    // Function to get the selected radio button ID
    private fun getSelectedSwitch(): Int {
        return when {
            switch250ml.isChecked -> R.id.switch_250
            switch500ml.isChecked -> R.id.switch_500
            switch1000ml.isChecked -> R.id.switch_1000
            else -> -1
        }
    }

    // Function to get the amount corresponding to the selected radio button
    private fun getSwitchAmount(selectedRadioButtonId: Int): Int {
        return when (selectedRadioButtonId) {
            R.id.switch_250 -> 250
            R.id.switch_500 -> 500
            R.id.switch_1000 -> 1000
            else -> 0
        }
    }

    //input validation
    private fun isNumeric(input: String): Boolean {
        return try {
            input.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        savedHour=p1
        savedMinute=p2

        if(btnTracker==1){

            btnWentToSleep.text = String.format("%d : %02d", savedHour, savedMinute)
            btnTracker=0

            wentToSleepMinute=savedMinute
            wentToSleepHr=savedHour

        }else if(btnTracker==2){
            btnWokeUp.text = String.format("%d : %02d", savedHour, savedMinute)
            btnTracker=0

            wokeUpMinue=savedMinute
            wokeUpHour=savedHour

        }

        if (btnWentToSleep.text!="- : - -" && btnWokeUp.text!="- : - -"){


            val timeDiff = calculateTimeDifference(wentToSleepHr,wentToSleepMinute,wokeUpHour,wokeUpMinue)
            txtHrsSlept.text =timeDiff.toString()
        }



    }

    private fun calculateTimeDifference(
        wentToSleepHour: Int,
        wentToSleepMinute: Int,
        wokeUpHour: Int,
        wokeUpMinute: Int
    ): Int {
        val wentToSleepTimeInMinutes = wentToSleepHour * 60 + wentToSleepMinute
        val wokeUpTimeInMinutes = wokeUpHour * 60 + wokeUpMinute

        var sleepInMins = 0

        // Check if the user has slept overnight
        if (wokeUpTimeInMinutes < wentToSleepTimeInMinutes) {
            // Calculate the sleep duration across midnight
            sleepInMins = (1440 - wentToSleepTimeInMinutes) + wokeUpTimeInMinutes
        } else {
            // else Calculate the normal sleep duration
            sleepInMins = wokeUpTimeInMinutes - wentToSleepTimeInMinutes
        }

        return sleepInMins / 60
    }



    private fun getDayOfWeekString(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "Sunday"
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            else -> ""
        }
    }
}
