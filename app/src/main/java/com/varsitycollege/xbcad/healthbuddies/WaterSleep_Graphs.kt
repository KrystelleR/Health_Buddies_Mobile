package com.varsitycollege.xbcad.healthbuddies

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter


class WaterSleep_Graphs : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_water_sleep__graphs, container, false)
        val barChart: BarChart = view.findViewById(R.id.sleep_bar_chart)

        /// Create sample data for the bar graph
        val entries = ArrayList<BarEntry>()
        entries.add(BarEntry(0f, 10f))
        entries.add(BarEntry(1f, 20f))
        entries.add(BarEntry(2f, 30f))
        entries.add(BarEntry(3f, 40f))
        entries.add(BarEntry(4f, 40f))
        entries.add(BarEntry(5f, 40f))
        entries.add(BarEntry(6f, 40f))

        // Create a list of day labels
        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")

        val dataSet = BarDataSet(entries, "Hours of sleep")

        val data = BarData(dataSet)

        barChart.data = data

        // Customize the appearance of the bar graph as needed
        // For example:
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
        return view
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