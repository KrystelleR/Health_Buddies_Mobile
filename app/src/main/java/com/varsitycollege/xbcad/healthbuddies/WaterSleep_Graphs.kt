package com.varsitycollege.xbcad.healthbuddies

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
                    updateWaterProgress(dailyWaterAmount)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle database error
                }
            })
        } else {
            // Handle the case where userId is null
        }

        return view
    }

    private fun updateWaterProgress(dailyWaterAmount: Int) {
        userWaterRef.child("Amount").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userWaterAmount = snapshot.getValue(Int::class.java) ?: 0
                val percentage = (userWaterAmount.toDouble() / dailyWaterAmount.toDouble() * 100).toInt()

                txtNeededWater.text = "$dailyWaterAmount ml"

                if (dailyWaterAmount - userWaterAmount > 0) {
                    txtWaterLeft.text = "${dailyWaterAmount - userWaterAmount} ml"
                } else {
                    txtWaterLeft.text = "Goal Reached. Good Job!!!"
                }

                progressBarWater.progress = percentage
                progressBarWater.invalidate()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })
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
