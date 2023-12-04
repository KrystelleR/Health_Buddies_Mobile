package com.varsitycollege.xbcad.healthbuddies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class StepsFragment:  Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_steps, container, false)
        val lineChart: LineChart = view.findViewById(R.id.lineChart)

        // Reference to your Firebase Database
        val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().reference

// Replace "userId" with the actual UID of the user you want to retrieve data for
        val userId = Firebase.auth.currentUser?.uid
// Reference to the specific user's steps data
        val userStepsReference = databaseReference.child("UserSteps").child(userId.toString())

// Add a ValueEventListener to get the data
        userStepsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get UserSteps object from dataSnapshot
                val userSteps = dataSnapshot.getValue(data.UserSteps::class.java)

                if (userSteps != null) {
                    // Access individual fields as needed
                    val hour00 = userSteps._00h00
                    val hour01 = userSteps._01h00
                    val hour02 = userSteps._02h00
                    val hour03 = userSteps._03h00
                    val hour04 = userSteps._04h00
                    val hour05 = userSteps._05h00
                    val hour06 = userSteps._06h00
                    val hour07 = userSteps._07h00
                    val hour08 = userSteps._08h00
                    val hour09 = userSteps._09h00
                    val hour10 = userSteps._10h00
                    val hour11 = userSteps._11h00
                    val hour12 = userSteps._12h00
                    val hour13 = userSteps._13h00
                    val hour14 = userSteps._14h00
                    val hour15 = userSteps._15h00
                    val hour16 = userSteps._16h00
                    val hour17 = userSteps._17h00
                    val hour18 = userSteps._18h00
                    val hour19 = userSteps._19h00
                    val hour20 = userSteps._20h00
                    val hour21 = userSteps._21h00
                    val hour22 = userSteps._22h00
                    val hour23 = userSteps._23h00


                    // Now you can use this data to update your UI or perform other actions

                    // Sample data
                    val entries = mutableListOf<Entry>()
                    entries.add(Entry(0f, userSteps._00h00.toFloat()))
                    entries.add(Entry(1f, userSteps._01h00.toFloat()))
                    entries.add(Entry(2f, userSteps._02h00.toFloat()))
                    entries.add(Entry(3f, userSteps._03h00.toFloat()))
                    entries.add(Entry(4f, userSteps._04h00.toFloat()))
                    entries.add(Entry(5f, userSteps._05h00.toFloat()))
                    entries.add(Entry(6f, userSteps._06h00.toFloat()))
                    entries.add(Entry(7f, userSteps._07h00.toFloat()))
                    entries.add(Entry(8f, userSteps._08h00.toFloat()))
                    entries.add(Entry(9f, userSteps._09h00.toFloat()))
                    entries.add(Entry(10f, userSteps._10h00.toFloat()))
                    entries.add(Entry(11f, userSteps._11h00.toFloat()))
                    entries.add(Entry(12f, userSteps._12h00.toFloat()))
                    entries.add(Entry(13f, userSteps._13h00.toFloat()))
                    entries.add(Entry(14f, userSteps._14h00.toFloat()))
                    entries.add(Entry(15f, userSteps._15h00.toFloat()))
                    entries.add(Entry(16f, userSteps._16h00.toFloat()))
                    entries.add(Entry(17f, userSteps._17h00.toFloat()))
                    entries.add(Entry(18f, userSteps._18h00.toFloat()))
                    entries.add(Entry(19f, userSteps._19h00.toFloat()))
                    entries.add(Entry(20f, userSteps._20h00.toFloat()))
                    entries.add(Entry(21f, userSteps._21h00.toFloat()))
                    entries.add(Entry(22f, userSteps._22h00.toFloat()))
                    entries.add(Entry(23f, userSteps._23h00.toFloat()))


                    // Create a dataset and set properties
                    val dataSet = LineDataSet(entries, "Steps per hour")
                    dataSet.color = resources.getColor(R.color.teal_200)
                    dataSet.valueTextColor = resources.getColor(R.color.white)
                    dataSet.valueTextSize = 12f // Set text size
                    dataSet.lineWidth = 3f // Set line thickness
                    dataSet.mode = LineDataSet.Mode.LINEAR // Set line style

                    // Create a LineData object with the dataset
                    val lineData = LineData(dataSet)

                    // Customize X-axis
                    val xAxis: XAxis = lineChart.xAxis
                    xAxis.position = XAxis.XAxisPosition.BOTTOM
                    xAxis.setDrawGridLines(false)
                    xAxis.valueFormatter = IndexAxisValueFormatter(arrayOf(
                        "00:00", "01:00", "02:00", "03:00", "04:00", "05:00", "06:00", "07:00", "08:00", "09:00", "10:00",
                        "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00",
                        "21:00", "22:00", "23:00"
                    ))

                    // Customize Y-axis
                    val yAxis: YAxis = lineChart.axisLeft
                    yAxis.setDrawGridLines(true)

                    // Set data to the chart
                    lineChart.data = lineData

                    lineChart.description.text = "My Steps Progress"
                    lineChart.description.textColor= resources.getColor(R.color.white)
                    lineChart.description.textSize=12f

                    // Refresh the chart
                    lineChart.invalidate()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })

        return view
    }
}

