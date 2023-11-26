package com.varsitycollege.xbcad.healthbuddies

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class Recipe : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_recipe)

        // Retrieve data from Intent
        val mealName: String? = intent.getStringExtra("MEAL_NAME")
        val mealType: String? = intent.getStringExtra("MEAL_TYPE")

        // Check if mealName and mealType are not null before using them
        mealName?.let { nonNullMealName ->
            mealType?.let { nonNullMealType ->
                // Construct the correct database reference based on mealType
                val databaseReference = FirebaseDatabase.getInstance().getReference("foods")
                    .child(nonNullMealType).child(nonNullMealName)

                // Initialize UI elements
                val txtHeaderRecipe = findViewById<TextView>(R.id.txt_headerRecipe)
                val txtCalories = findViewById<TextView>(R.id.txt_cals)
                val imgRecipe = findViewById<ImageView>(R.id.iv_meal_image)
                val btnRecipe = findViewById<AppCompatButton>(R.id.btn_recipe)
                val addToPlateCheckBox = findViewById<CheckBox>(R.id.chkbox_add_to_plate)

                // Show loading screen
                findViewById<FrameLayout>(R.id.overlay).visibility = View.VISIBLE

                databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        // Hide loading screen
                        findViewById<FrameLayout>(R.id.overlay).visibility = View.INVISIBLE

                        if (snapshot.exists()) {
                            // Data found, populate UI elements
                            val caloriesLong = snapshot.child("calories").getValue(Long::class.java)
                            val calories = caloriesLong?.toString() ?: snapshot.child("calories").getValue(String::class.java)

                            // set the retrieved data to your UI elements
                            txtHeaderRecipe.text = nonNullMealName
                            txtCalories.text = calories

                            // Set image UI elements
                            val imageUrl = snapshot.child("image_url").getValue(String::class.java)

                            imageUrl?.let {
                                Picasso.get().load(it).into(imgRecipe)
                            }

                            // Set recipe button click listener
                            val recipeUrl = snapshot.child("recipe_url").getValue(String::class.java)
                            btnRecipe.setOnClickListener {
                                openRecipeUrl(recipeUrl)
                            }

                            // Set onClickListener for the "Add to plate" checkbox
                            addToPlateCheckBox.setOnCheckedChangeListener { _, isChecked ->
                                if (isChecked) {
                                    // When checked, add the meal to the user's calories
                                    val caloriesLong = snapshot.child("calories").getValue(Long::class.java)
                                    addToPlate(nonNullMealType.replaceFirstChar { it.uppercaseChar() }, nonNullMealName, caloriesLong ?: 0L)
                                }
                            }
                        } else {
                            // Handle case where data doesn't exist
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        // Handle error

                        // Hide loading screen
                        findViewById<FrameLayout>(R.id.overlay).visibility = View.INVISIBLE
                    }
                })
            }
        }
    }

    private fun openRecipeUrl(url: String?) {
        url?.let {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    private fun addToPlate(mealType: String, mealName: String, calories: Long) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if (currentUser != null) {
            val userId = currentUser.uid

            val userCaloriesRef = FirebaseDatabase.getInstance().getReference("UserCalories").child(userId)

            // Check if there is an existing value for the specified mealType
            userCaloriesRef.child("${mealType}").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val existingCalories = snapshot.child("Calories").getValue(Long::class.java)
                    val existingMealName = snapshot.child("MealName").getValue(String::class.java)

                    if (existingCalories != null && existingMealName != null && existingCalories != 0L) {
                        // Existing value is not 0, prompt the user with a dialog
                        runOnUiThread {
                            showUpdateDialog(existingCalories,existingMealName, userCaloriesRef, mealType, mealName, calories)
                        }
                    } else {
                        // Existing value is 0 or doesn't exist, update the value directly
                        updateCalories(userCaloriesRef, mealType, mealName, calories)
                        showUpdateSuccessDialog()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })

        }
    }

    private fun updateCalories(userCaloriesRef: DatabaseReference, mealType: String, mealName: String, calories: Long) {
        // Update the calories and mealName directly under the specific meal type
        userCaloriesRef.child(mealType).child("MealName").setValue(mealName)
        userCaloriesRef.child(mealType).child("Calories").setValue(calories)

        showUpdateSuccessDialog()//notify user that the meal has been updated

    }

    private fun showUpdateDialog(existingCalories: Long, existingMealName: String,userCaloriesRef: DatabaseReference, mealType: String, mealName: String, calories: Long) {
        // Implement your dialog here, prompting the user to update the existing item
        // You can use an AlertDialog or any other UI component for this

        // Example: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Update Meal Item")
            .setMessage("There is an existing meal with calories: $existingCalories and meal name: '$existingMealName'.Do you want to update your current meal item for $mealType?")
            .setPositiveButton("Yes") { _, _ ->
                // If the user clicks "Yes," update the item under the specific meal type
                updateCalories(userCaloriesRef, mealType, mealName, calories)
            }
            .setNegativeButton("No") { _, _ ->
                // If the user clicks "No," do nothing or handle accordingly
            }
            .show()
    }

    //prompt user on successful meal update
    private fun showUpdateSuccessDialog() {
        val successDialog = AlertDialog.Builder(this)
            .setTitle("Meal Update Successful")
            .setMessage("Your meal has been successfully updated.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                val intent =Intent(this,Meals::class.java)
                startActivity(intent)
            }
            .create()

        successDialog.show()
    }


}
