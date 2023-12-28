package com.example.healthplus

import com.example.healthplus.R
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class StatsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val lineChart: LineChart = view.findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart)
        val lineChart2: LineChart = view.findViewById<com.github.mikephil.charting.charts.LineChart>(R.id.lineChart2)

        // Create sample data
        val entries: List<Entry> = listOf(
            Entry(1f, 20f),
            Entry(2f, 35f),
            Entry(3f, 15f),
            Entry(4f, 25f)
        )

        // Create a data set
        val dataSet = LineDataSet(entries, "Sample Data")

        // Create a LineData object and set the data set
        val lineData = LineData(dataSet)
        val lineData2 = LineData(dataSet)

        // Set data to the chart
        lineChart.data = lineData
        lineChart2.data = lineData

        // Customize chart appearance (optional)
        val description = Description()
        description.text = "Simple Line Chart"
        lineChart.description = description
        lineChart2.description = description
    }
}