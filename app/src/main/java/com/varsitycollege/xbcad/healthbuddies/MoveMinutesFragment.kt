package com.varsitycollege.xbcad.healthbuddies

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MoveMinutesFragment : Fragment() {

    var moveminutes: Int = 0
    var mmgoal: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_moveminutes, container, false)
        val barChart: BarChart = view.findViewById(R.id.barChart)

        val database = FirebaseDatabase.getInstance()
        // Getting user details from db
        val currentUser = Firebase.auth.currentUser
        if (currentUser != null) {
            // Assuming userDetails.uid is the user's UID
            val userUid = currentUser.uid
            // Reference to the user's data in the Realtime Database
            val userRef = database.getReference("Users").child(userUid) // Corrected line

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot contains the user details data
                        val UserDetails = dataSnapshot.getValue(data.UserDetails::class.java)
                        // Now you can use the userDetails object as needed
                        if (UserDetails != null) {
                            mmgoal = UserDetails.moveMinutes.toInt()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(
                        ContentValues.TAG,
                        "Error reading user details from the database",
                        databaseError.toException()
                    )
                }
            })


            val userRef1 = database.getReference("UserMinutes").child(userUid) // Corrected line

            userRef1.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // dataSnapshot contains the user details data
                        val UserMinutes = dataSnapshot.getValue(data.UserMinutes::class.java)
                        // Now you can use the userDetails object as needed
                        if (UserMinutes != null) {
                            moveminutes = UserMinutes.minutes

                            // Create sample data for the bar graph
                            val entries = ArrayList<BarEntry>()
                            entries.add(BarEntry(0f, moveminutes.toFloat()))
                            entries.add(BarEntry(1f, mmgoal.toFloat()))

                            // Create a list of day labels
                            val variables = listOf("Current", "My Goal")

                            val dataSet = BarDataSet(entries, "My Move Minutes")
                            dataSet.colors = listOf(resources.getColor(R.color.teal_200), resources.getColor(R.color.orange))
                            dataSet.valueTextSize = 12f // Set text size
                            dataSet.valueTextColor = resources.getColor(R.color.black)


                            val data = BarData(dataSet)

                            barChart.data = data

                            // Customize the appearance of the bar graph as needed
                            // For example:
                            barChart.setDrawBarShadow(false)
                            barChart.setDrawValueAboveBar(true)
                            barChart.description.isEnabled = false

                            // Set X-axis labels
                            val xAxis = barChart.xAxis
                            xAxis.valueFormatter =
                                DayAxisValueFormatter1(variables) // Custom formatter for days
                            xAxis.position = XAxis.XAxisPosition.BOTTOM
                            xAxis.granularity = 1f

                            // Refresh the chart to display the changes
                            barChart.invalidate()
                        }
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Log.e(
                        ContentValues.TAG,
                        "Error reading user details from the database",
                        databaseError.toException()
                    )
                }
            })
        }
        // Refresh the chart to display the changes
        barChart.invalidate()
        return view
    }
}
class DayAxisValueFormatter1(private val daysOfWeek: List<String>) : ValueFormatter() {

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return if (value >= 0 && value < daysOfWeek.size) {
            daysOfWeek[value.toInt()]
        } else {
            ""
        }
    }
}