package com.varsitycollege.xbcad.healthbuddies

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.github.mikephil.charting.components.Legend


class Nutrition : AppCompatActivity() {

    // Pie chart declarations
    lateinit var pieChart: PieChart
    lateinit var piechart_mealList: ArrayList<PieEntry>

    // Firebase references
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser
    private val userId: String = currentUser?.uid ?: ""

    private val userCaloriesRef = FirebaseDatabase.getInstance().getReference("UserCalories").child(userId)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nutrition)

        // Progress bar code
        val progressBar: ProgressBar = findViewById(R.id.progresbar_nutrition)

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
    }

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
        pieChart.description.setPosition(460f, 480f)
        pieChart.legend.xOffset=-40f
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
