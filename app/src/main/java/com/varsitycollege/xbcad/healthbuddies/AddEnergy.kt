package com.varsitycollege.xbcad.healthbuddies

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.text.isDigitsOnly
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.varsitycollege.xbcad.healthbuddies.R

class AddEnergy : AppCompatActivity() {

    // Firebase references
    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private val userId = currentUser?.uid
    private val userCaloriesRef = userId?.let { FirebaseDatabase.getInstance().getReference("UserCalories").child(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_energy)

        val calories = intent.getIntExtra("calories", 0)
        val entered_cal = findViewById<EditText>(R.id.txt_entered_cal)
        entered_cal.setText(calories.toString())



        // Back button code
        val btnbackToNutrition = findViewById<AppCompatButton>(R.id.btn_back_add_energy)
        btnbackToNutrition.setOnClickListener {
            finish()
        }

        // Add energy spinner code
        val spinner: Spinner = findViewById(R.id.spinner_meals)

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            this,
            R.array.spinner_meal_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }

        // Button to capture energy
        val btnCaptureEnergy = findViewById<AppCompatButton>(R.id.btn_capture_energy)
        btnCaptureEnergy.setOnClickListener {
            captureEnergy()
        }
    }

    private fun captureEnergy() {
        // Get user input
        val enteredCaloriesEditText = findViewById<EditText>(R.id.txt_entered_cal)
        val enteredCalories = enteredCaloriesEditText.text.toString().trim()

        val selectedMealPosition = findViewById<Spinner>(R.id.spinner_meals).selectedItemPosition
        val selectedMeal = resources.getStringArray(R.array.spinner_meal_options)[selectedMealPosition]

        // Validate user input
        if (enteredCalories.isEmpty() || !enteredCalories.isDigitsOnly()) {
            showErrorDialog("Please enter a valid number for calories.")
            return
        }

        if (selectedMeal.isEmpty()) {
            showErrorDialog("Please select a meal type.")
            return
        }

        // Convert entered calories to Long
        val caloriesLong = enteredCalories.toLong()

        // Update the database
        updateCalories(selectedMeal, caloriesLong)
    }

    private fun updateCalories(mealType: String, calories: Long) {
        // Check if there is an existing value for the specified mealType
        userCaloriesRef?.child(mealType)?.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val existingCalories = snapshot.child("Calories").getValue(Long::class.java)
                val existingMealName = snapshot.child("MealName").getValue(String::class.java)

                if(mealType.equals("Snacks")){
                    val newCalories = existingCalories?.plus(calories)
                    if (newCalories != null) {
                        updateCaloriesDirectly(mealType, newCalories)
                    }
                }
                else if (existingCalories != null && existingMealName != null && existingCalories != 0L) {
                    showConfirmationDialog(existingCalories, existingMealName, mealType, calories)
                } else {
                    // Existing value is 0 or doesn't exist, update the value directly
                    updateCaloriesDirectly(mealType, calories)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                showErrorDialog("Database error. Please try again.")
            }
        })
    }

    private fun showConfirmationDialog(existingCalories: Long, existingMealName: String, mealType: String, calories: Long) {
        AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("There is an existing meal with calories: $existingCalories and meal name: '$existingMealName'. Do you want to update it?")
            .setPositiveButton("Yes") { _, _ ->
                // User confirmed, update the value directly
                updateCaloriesDirectly(mealType, calories)
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun updateCaloriesDirectly(mealType: String, calories: Long) {
        // Update the calories and mealName directly under the specific meal type
        userCaloriesRef?.child(mealType)?.child("MealName")?.setValue("custom meal")
        userCaloriesRef?.child(mealType)?.child("Calories")?.setValue(calories)

        // Show success dialog
        showSuccessDialog()
    }

    private fun showErrorDialog(message: String) {
        AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
    }

    private fun showSuccessDialog() {
        AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage("Your energy has been added successfully.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(this,Nutrition::class.java)
                startActivity(intent)
            }
            .show()
    }
}
