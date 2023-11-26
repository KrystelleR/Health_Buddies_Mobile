package com.varsitycollege.xbcad.healthbuddies

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class UserCaloriesListActivity : AppCompatActivity(), UserCaloriesAdapter.ResetButtonClickListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var userCaloriesAdapter: UserCaloriesAdapter
    private lateinit var userCaloriesRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_user_calories_list)



        // Initialize RecyclerView and adapter
        recyclerView = findViewById(R.id.recyclerViewUserCalories)
        userCaloriesAdapter = UserCaloriesAdapter(this, emptyList(),this)
        recyclerView.adapter = userCaloriesAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            userCaloriesRef = FirebaseDatabase.getInstance().getReference("UserCalories").child(userId)

            // Retrieve data from the database
            userCaloriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    updateAdapterData(snapshot)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    showError("Database error. Please try again.")
                }
            })
        } else {
            // Handle the case where userId is null (e.g., user not authenticated)
            showError("User not authenticated")
            finish() // Close the activity if the user is not authenticated
        }
    }

    override fun onResetButtonClick(mealType: String) {
        resetMealData(mealType)
    }
    private fun updateAdapterData(snapshot: DataSnapshot) {
        val userCaloriesItemList = mutableListOf<UserCaloriesItem>()
        for (childSnapshot in snapshot.children) {
            val mealType = childSnapshot.key
            val mealName = childSnapshot.child("MealName").getValue(String::class.java) ?: ""
            val calories = childSnapshot.child("Calories").getValue(Long::class.java) ?: 0L
            val userCaloriesItem = UserCaloriesItem(mealType.orEmpty(), mealName, calories)
            userCaloriesItemList.add(userCaloriesItem)
        }
        userCaloriesAdapter.updateData(userCaloriesItemList)
    }

    private fun showError(message: String) {
        // Implement error handling as needed
    }

    private fun resetMealData(mealType: String) {
        // Create a confirmation dialog
        val confirmationDialog = AlertDialog.Builder(this)
            .setTitle("Confirm Reset")
            .setMessage("Are you sure you want to reset this meal?")
            .setPositiveButton("Yes") { _, _ ->
                // User clicked Yes, proceed with reset
                performReset(mealType)
                // Show a success dialog
                showSuccessDialog()
            }
            .setNegativeButton("No") { dialog, _ ->
                // User clicked No, dismiss the dialog
                dialog.dismiss()
            }
            .create()

        // Show the confirmation dialog
        confirmationDialog.show()
    }

    private fun performReset(mealType: String) {
        // Get the current user's ID
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            // Reference to the user's meals for the specified meal type
            val userMealRef = FirebaseDatabase.getInstance().getReference("UserCalories").child(userId)
                .child(mealType)

            // Update the MealName and Calories for the specified meal type
            userMealRef.child("MealName").setValue("Haven't Eaten")
            userMealRef.child("Calories").setValue(0)




        } else {
            // Handle the case where userId is null (e.g., user not authenticated)
            showError("User not authenticated")
            finish() // Close the activity if the user is not authenticated
        }
    }

    private fun showSuccessDialog() {

            // Create a success dialog
            val successDialog = AlertDialog.Builder(this)
                .setTitle("Success")
                .setMessage("Meal reset successfully!")
                .setPositiveButton("OK") { dialog, _ ->
                    // User clicked OK, dismiss the dialog
                    dialog.dismiss()
                    // Navigate to the Nutrition page
                    navigateToNutritionPage()
                }
                .create()

            // Show the success dialog
            successDialog.show()

    }




    private fun navigateToNutritionPage() {
        // Implement the logic to navigate to the Nutrition page here
        // You can use Intent to start a new activity
        val intent = Intent(this, Nutrition::class.java)
        startActivity(intent)
        finish() // Optionally finish the current activity
    }

}
