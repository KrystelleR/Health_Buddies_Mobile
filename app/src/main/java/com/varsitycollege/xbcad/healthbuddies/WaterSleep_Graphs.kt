package com.varsitycollege.xbcad.healthbuddies

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class WaterSleep_Graphs : Fragment() {

    private lateinit var progressBarWater: ProgressBar
    private lateinit var txtNeededWater: TextView
    private lateinit var txtWaterLeft: TextView
    private lateinit var resetWaterButton: AppCompatButton
    private lateinit var userWaterRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_water_sleep__graphs, container, false)
        val barChart: BarChart = view.findViewById(R.id.sleep_bar_chart)


        // Create a list of day labels
        val daysOfWeek = listOf("Sun-Mon", "Mon-Tue", "Tue-Wed", "Wed-Thur", "Thur-Fri", "Fri-Sat", "Sat-Sun")

        // Create a dummy dataset for the initial display
        val initialEntries = listOf(10f, 20f, 30f, 40f, 40f, 40f, 40f)
        val dataSet = BarDataSet(initialEntries.mapIndexed { index, value -> BarEntry(index.toFloat(), value) }, "Hours of sleep")
        val data = BarData(dataSet)
        barChart.data = data

        // Customize the appearance of the bar graph as needed
        barChart.setDrawBarShadow(false)
        barChart.setDrawValueAboveBar(true)
        barChart.description.isEnabled = false

        // Set X-axis labels
        val xAxis = barChart.xAxis
        xAxis.valueFormatter = DayAxisValueFormatter(daysOfWeek) // Custom formatter for days
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        // Refresh the chart to display the changes
        barChart.invalidate()

        // Initialize views
        txtNeededWater = view.findViewById(R.id.txt_needed_water)
        txtWaterLeft = view.findViewById(R.id.txt_water_left)
        progressBarWater = view.findViewById(R.id.progresbar_water)

        // Set up your Firebase reference
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        val userId = currentUser?.uid

        if (userId != null) {
            val userSleepRef = FirebaseDatabase.getInstance().getReference("UserSleepHours").child(userId)
            val usersRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
            userWaterRef = FirebaseDatabase.getInstance().getReference("UserWater").child(userId)

            userSleepRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val entries = ArrayList<BarEntry>()

                    for ((index, day) in daysOfWeek.withIndex()) {
                        val hours = snapshot.child(day).child("Hours").getValue(Float::class.java) ?: 0f
                        entries.add(BarEntry(index.toFloat(), hours))
                    }

                    val dataSet = BarDataSet(entries, "Hours of sleep")

                    // Clear the existing data before setting new data
                    barChart.clear()
                    barChart.data = BarData(dataSet)

                    // Customize the appearance of the bar graph as needed
                    barChart.setDrawBarShadow(false)
                    barChart.setDrawValueAboveBar(true)
                    barChart.description.isEnabled = false

                    val xAxis = barChart.xAxis
                    xAxis.valueFormatter = DayAxisValueFormatter(daysOfWeek)
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.granularity = 1f

                    // Refresh the chart to display the changes
                    barChart.invalidate()
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })

            usersRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val dailyWaterAmount = snapshot.child("dailyWaterAmount").getValue(Int::class.java) ?: 0
                    updateWaterProgress(dailyWaterAmount,userId)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })

            // Add a click listener to the resetWaterButton
            resetWaterButton=view.findViewById(R.id.reset_water_button)
            resetWaterButton.setOnClickListener {
                showResetWaterConfirmationDialog()
            }
        } else {
            // Handle the case where userId is null
        }

        return view
    }

    private fun showResetWaterConfirmationDialog() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Confirm Reset")
            .setMessage("Are you sure you want to reset the water amount to 0?")
            .setPositiveButton("Yes") { _, _ ->
                resetWaterAmount()


            }
            .setNegativeButton("No") { _, _ ->
                // User canceled the reset, do nothing
            }
            .create()

        dialog.show()
    }
    private fun showSuccessDialog() {
        val successDialog = AlertDialog.Builder(requireContext())
            .setTitle("Success!")
            .setMessage("Water amount reset to 0. Returning to Home page :)")
            .setPositiveButton("OK") { _, _ ->
                // Navigate to MainActivity
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
            .create()

        successDialog.show()
    }
    private fun resetWaterAmount() {
        // Set the Amount in UserWater to 0
        userWaterRef.child("Amount").setValue(0)

        showSuccessDialog()
        // Show a confirmation toast or dialog if needed
        // Toast.makeText(requireContext(), "Water amount reset to 0", Toast.LENGTH_SHORT).show()
    }
    private fun updateWaterProgress(dailyWaterAmount: Int, userId: String) {
        userWaterRef.child("Amount").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userWaterAmount = snapshot.getValue(Int::class.java) ?: 0
                val percentage = (userWaterAmount.toDouble() / dailyWaterAmount.toDouble() * 100).toInt()

                txtNeededWater.text = "$dailyWaterAmount ml"

                if (dailyWaterAmount - userWaterAmount > 0) {
                    txtWaterLeft.text = "${dailyWaterAmount - userWaterAmount} ml"
                } else {
                    txtWaterLeft.text = "Goal Reached. Good Job!!!"
                    setWaterGoalTrue(userId)
                }

                progressBarWater.progress = percentage
                progressBarWater.invalidate()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
    }
    private fun setWaterGoalTrue(userId: String) {
        // Reference to UserCollectPoints node for the current user
        val userCollectPointsRef = FirebaseDatabase.getInstance().getReference("UserCollectPoints").child(userId)

        // Check if caloriesGoal is false before setting it to true
        userCollectPointsRef.child("waterGoal").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Get the current value of caloriesGoal, defaulting to false if not present
                val currentGoal = snapshot.getValue(Boolean::class.java) ?: false

                // If caloriesGoal is currently false, set it to true
                if (!currentGoal) {
                    userCollectPointsRef.child("waterGoal").setValue(true)

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
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Congratulations!")
            .setMessage("Water Goal Completed. You have been awarded 10 points :)")
            .setPositiveButton("OK") { _, _ ->
                // Handle OK button click if needed
            }
            .create()

        dialog.show()
    }
}

class DayAxisValueFormatter(private val daysOfWeek: List<String>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return if (value >= 0 && value < daysOfWeek.size) {
            daysOfWeek[value.toInt()]
        } else {
            ""
        }
    }
}
