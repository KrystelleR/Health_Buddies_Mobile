package com.varsitycollege.xbcad.healthbuddies

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.github.mikephil.charting.components.Legend
import com.varsitycollege.xbcad.healthbuddies.databinding.ActivityNutritionBinding


class Nutrition : AppCompatActivity() {

    // Pie chart declarations
    lateinit var pieChart: PieChart
    lateinit var piechart_mealList: ArrayList<PieEntry>

    // Firebase references
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private val userId: String = currentUser?.uid ?: ""

    private val userCaloriesRef = FirebaseDatabase.getInstance().getReference("UserCalories").child(userId)

    // progress bar
    private lateinit var progressBar: ProgressBar

    // Firebase references

    private lateinit var usersRef: DatabaseReference

    private var requestCamera: ActivityResultLauncher<String>?= null
    private lateinit var binding: ActivityNutritionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_nutrition)
        binding = ActivityNutritionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Progress bar code
        progressBar= findViewById(R.id.progresbar_nutrition)
        // Initialize Firebase references
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        // Handle nullable userId by providing a default value (empty string)
        usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId ?: "")
        // Fetch data and update progress bar
        fetchAndUpdateProgressBar()

        // Pie chart code
        pieChart = findViewById(R.id.pieChart_nutrition)
        piechart_mealList = ArrayList()

        // Retrieve data from the database and update the PieChart
        fetchCaloriesDataAndUpdatePieChart()

        // Navigation to Meals page
        val btnMeals = findViewById<Button>(R.id.btn_meals)
        btnMeals.setOnClickListener {
            val intent = Intent(this, Meals::class.java)
            startActivity(intent)
        }

        // Navigation to Add Energy page
        val btnEnergy = findViewById<Button>(R.id.btn_add_energy)
        btnEnergy.setOnClickListener {
            val intent = Intent(this, AddEnergy::class.java)
            startActivity(intent)
        }


        // Navigation to user's meals page
        val btnEditUsermeals = findViewById<Button>(R.id.btn_edit_meals)
        btnEditUsermeals.setOnClickListener {
            val intent = Intent(this, UserCaloriesListActivity::class.java)
            startActivity(intent)
        }

            requestCamera = registerForActivityResult(
                ActivityResultContracts
                .RequestPermission(), ){
                if(it){
                    val intent = Intent(this, barcodescanner:: class.java)
                    startActivity(intent)

                }else{
                    Toast.makeText(this, "Permission Not Granted", Toast.LENGTH_SHORT).show()
                }
            }

            val scanner = findViewById<CardView>(R.id.scanner_cardView)
            scanner.setOnClickListener(){
                requestCamera?.launch(android.Manifest.permission.CAMERA)
            }
        }

    //update pie chart with db values
    private fun fetchCaloriesDataAndUpdatePieChart() {
        userCaloriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Data found, populate PieChart with meal entries
                    for (mealSnapshot in snapshot.children) {
                        val mealName = mealSnapshot.key
                        val calories = mealSnapshot.child("Calories").getValue(Long::class.java)

                        if (mealName != null && calories != null) {
                            piechart_mealList.add(PieEntry(calories.toFloat(), mealName))
                        }
                    }

                    updatePieChart()
                } else {
                    // Handle case where data doesn't exist
                    Toast.makeText(applicationContext, "No data found in the database", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(applicationContext, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun fetchAndUpdateProgressBar() {
        val txt_neededEnergy = findViewById<TextView>(R.id.txt_needed_energy)
        val txt_energyLeft =findViewById<TextView>(R.id.txt_energy_left)
        // Fetch daily calories from Users table
        usersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(usersSnapshot: DataSnapshot) {
                if (usersSnapshot.exists()) {
                    val dailyCalories = usersSnapshot.child("dailyCalories").getValue(Long::class.java) ?: 0

                    // Fetch calories for each meal category from UserCalories table
                    userCaloriesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(userCaloriesSnapshot: DataSnapshot) {
                            var consumedCalories = 0L

                            for (mealSnapshot in userCaloriesSnapshot.children) {
                                val calories = mealSnapshot.child("Calories").getValue(Long::class.java) ?: 0
                                consumedCalories += calories
                            }

                            txt_neededEnergy.text= dailyCalories.toString()

                            if(dailyCalories.toDouble() - consumedCalories.toDouble() >0){
                                txt_energyLeft.text= (dailyCalories.toDouble() -consumedCalories.toDouble()).toInt().toString()
                            }else{
                                txt_energyLeft.text = "GOAL REACHED. Good job!!!"

                                setCaloriesGoalTrue()
                            }


                            // Calculate progress percentage
                            val progress = (consumedCalories.toDouble() / dailyCalories.toDouble() * 100).toInt()

                            // Update the progress bar
                            progressBar.progress = progress
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun setCaloriesGoalTrue() {
        // Reference to UserCollectPoints node for the current user
        val userCollectPointsRef = FirebaseDatabase.getInstance().getReference("UserCollectPoints").child(userId)

        // Check if caloriesGoal is false before setting it to true
        userCollectPointsRef.child("caloriesGoal").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get the current value of caloriesGoal, defaulting to false if not present
                val currentGoal = snapshot.getValue(Boolean::class.java) ?: false

                // If caloriesGoal is currently false, set it to true
                if (!currentGoal) {
                    userCollectPointsRef.child("caloriesGoal").setValue(true)

                    // Reference to Users node for the current user
                    val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                    // Add 10 to userCurrency in the Users structure
                    userRef.child("userCurrency").addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(userCurrencySnapshot: DataSnapshot) {
                            // Get the current value of userCurrency, defaulting to 0 if not present
                            val currentCurrency = userCurrencySnapshot.getValue(Long::class.java) ?: 0

                            // Calculate the updated value of userCurrency
                            val updatedCurrency = currentCurrency + 10

                            // Set the updated value of userCurrency in the Users structure
                            userRef.child("userCurrency").setValue(updatedCurrency)

                            // Show congratulatory dialog box
                            showCongratulationsDialog()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle error during data retrieval
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error during data retrieval
            }
        })
    }

    private fun showCongratulationsDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Congratulations!")
            .setMessage("Nutrition Goal Completed. You have been awarded 10 points :)")
            .setPositiveButton("OK") { _, _ ->
                // Handle OK button click if needed
            }
            .create()

        dialog.show()
    }

    private fun updatePieChart() {
        val pieDataSet = PieDataSet(piechart_mealList, "")
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS, 1000)
        pieDataSet.valueTextSize = 15f
        pieDataSet.valueTextColor = Color.WHITE

        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.description.text = "My Calories (kcal)"
        pieChart.description.textColor = Color.BLACK
        pieChart.description.textSize = 15f
        pieChart.description.textColor = Color.WHITE

        pieChart.legend.xOffset=-58f
        pieChart.legend.textSize = 15f
        pieChart.legend.formSize = 15f
        pieChart.centerText = "Calories"

        // Ensure legend is placed below the chart with some margin
        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        legend.orientation = Legend.LegendOrientation.HORIZONTAL
        legend.setDrawInside(false)
        legend.formSize = 15f
        legend.textSize = 15f
        legend.xEntrySpace = 25f // Add some space between legend entries

        pieChart.animateY(2000)
    }

    //if back button on phone is pressed then navigate to main activity
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }



}
