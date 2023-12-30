package com.example.healthplus

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthplus.databinding.FragmentProfileBinding
import com.example.healthplus.databinding.FragmentStatsBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Date


class StatsFragment : Fragment() {
    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!
    private lateinit var calChart: BarChart
    private lateinit var weightChart: LineChart
    private val database =
        Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        val view = binding.root
        calChart = binding.barChartView
        weightChart = binding.barChartView2


        val weightList = ArrayList<Int>()
        val dateList = ArrayList<String>()
        val calList = ArrayList<Double>()



        retrieveUserData(getAndroidId(requireContext())) { userData ->
            if (userData != null) {
                //Saker händer med User Objekt
                //Det här är en for-loop som loopar igenom allt som är under daily.
                //Det enda du behöver ha koll på är variablerna weightEntry.value hämtar värde på vikt historiker osv...
                //Här är allt du behöver jobba med.
                for (weightEntry in userData.weightEntries) {
                    weightEntry.value?.let { weightList.add(it.toInt()) }
                    weightEntry.calories?.let { calList.add(it.toDouble()) }
                    weightEntry.date?.let { dateList.add(it.toString()) }
                }

                showBarChart(dateList,calList)
                showLineChart(weightList,dateList)

            } else {
                //Inget Händer
            }
        }

        return view
    }

    private fun showLineChart(weightList: ArrayList<Int>, dateList: ArrayList<String>){
        val weightListPresent = ArrayList<Int>()
        val dateListPresent = ArrayList<String>()
        val entriesWeight = ArrayList<Entry>()

        val dateSize = dateList.size //används för att kolla antal sparade data i databasen
        val title2 = "Weight"
        var nrLoopsWeight = dateSize

        if (nrLoopsWeight > 91) {
            nrLoopsWeight = 91}
        for (i in 0 until nrLoopsWeight) {  // skapar kortare arrayerna
            weightListPresent.add(weightList[i])
        }

        for (i in 0 until nrLoopsWeight) {  // skapar kortare arrayerna
            dateListPresent.add(dateList[i])
        }

        for (i in 0 until weightListPresent.size) {
            val lineEntry = BarEntry(i.toFloat(), weightListPresent[i].toFloat())
            entriesWeight.add(lineEntry)

            val dataSet = LineDataSet(entriesWeight, title2)
            dataSet.valueTextColor = Color.WHITE
            dataSet.color = Color.parseColor("#008080")
//            dataSet.lineWidth = 2f
//            dataSet.circleRadius = 5f

            val lineData = LineData(dataSet)
            weightChart.data = lineData
            weightChart.invalidate()
            initLineDataSet(dataSet,dateListPresent)
        }
    }
    private fun showBarChart( dateList: ArrayList<String>, calList: ArrayList<Double>) {


        // nya kortare arrayer att presentera

        val dateListPresent = ArrayList<String>()
        val calListPresent = ArrayList<Double>()

        val entriesCal = ArrayList<BarEntry>()  // scapar bars


        val dateSize = dateList.size //används för att kolla antal sparade data i databasen

        val title1 = "Calories"


        var nrLoopsCal = dateSize


        if (nrLoopsCal > 11){nrLoopsCal = 11 }
            for (i in 0 until nrLoopsCal) {  // skapar kortare arrayerna
                calListPresent.add(calList[i])
        }

            for (i in 0 until nrLoopsCal) {  // skapar kortare arrayerna
                dateListPresent.add(dateList[i])
            }


            // fit the data into a bar
            for (i in 0 until calListPresent.size) {
                val barEntry = BarEntry(i.toFloat(), calListPresent[i].toFloat())
                entriesCal.add(barEntry)
            }

            val barDataSet = BarDataSet(entriesCal, title1)
            barDataSet.valueTextColor = Color.WHITE
            barDataSet.color = Color.parseColor("#008080")

            val data = BarData(barDataSet)

            calChart.data = data
            calChart.invalidate()
            initBarDataSet(barDataSet,dateListPresent)

        }


     private fun initBarDataSet(barDataSet: BarDataSet, dateListPresent: ArrayList<String> ) {

        calChart.setBorderColor(Color.WHITE)
         calChart.setBackgroundResource(R.drawable.sharp_corners)
// hiding the grey background of the chart, default false if not set
        calChart.setDrawGridBackground(false)
// remove the bar shadow, default false if not set
        calChart.setDrawBarShadow(false)
// remove border of the chart, default false if not set
        calChart.setDrawBorders(true)

// remove the description label text located at the lower right corner
        val description = Description()
        description.isEnabled = false
        calChart.description = description

// setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
        calChart.animateY(1500)
// setting animation for x-axis, the bar will pop up separately within the time we set
        calChart.animateX(1500)

        val xAxis = calChart.xAxis
         xAxis.labelRotationAngle = 45f
         xAxis.textColor = Color.WHITE
// change the position of x-axis to the bottom
        xAxis.position = XAxis.XAxisPosition.BOTTOM
// set the horizontal distance of the grid line
        xAxis.granularity = 1f
// hiding the x-axis line, default true if not set
        xAxis.setDrawAxisLine(true)
// hiding the vertical grid lines, default true if not set
        xAxis.setDrawGridLines(false)

        val leftAxis = calChart.axisLeft
// hiding the left y-axis line, default true if not set
        leftAxis.setDrawAxisLine(false)
         leftAxis.textColor = Color.WHITE

        val rightAxis = calChart.axisRight
// hiding the right y-axis line, default true if not set
        rightAxis.setDrawAxisLine(false)
         rightAxis.textColor = Color.WHITE
         rightAxis.setDrawLabels(false)
        val legend = calChart.legend
// setting the shape of the legend form to line, default square shape
        legend.form = Legend.LegendForm.LINE
         legend.textColor = Color.WHITE
         legend
         calChart.setExtraBottomOffset(20f)
         barDataSet.valueTextSize = 11f
// set
         xAxis.valueFormatter = object : ValueFormatter() {
             override fun getFormattedValue(value: Float): String {
                 val index = value.toInt()
                 return if (index >= 0 && index < dateListPresent.size) {
                     val inputDateStr = dateListPresent[index]
                     val inputFormat = SimpleDateFormat("yyyy-MM-dd")
                     val outputFormat = SimpleDateFormat("MMM dd")

                     try {
                         val date = inputFormat.parse(inputDateStr)
                         return outputFormat.format(date)
                     } catch (e: Exception) {
                         e.printStackTrace()
                         // Return the original string if parsing fails
                         inputDateStr
                     }
                 } else {
                     ""
                 }
             }
         }

    }

    private fun initLineDataSet(lineDataSet: LineDataSet, dateListPresent: ArrayList<String>) {
        // Customize the LineDataSet
        lineDataSet.color = Color.parseColor("#008080")
        lineDataSet.lineWidth = 2f
        lineDataSet.circleRadius = 5f
        lineDataSet.setDrawValues(true) // Set to true if you want to display values on data points

        val description = Description()
        description.isEnabled = false

        // Customize the LineChart
        weightChart.apply {
            setBorderColor(Color.WHITE)
            setBackgroundResource(R.drawable.sharp_corners)
            setDrawGridBackground(false)
            setDrawBorders(true)
            setDescription(description)
            weightChart.animateX(1500)
            weightChart.animateY(1500)
        }

        val xAxis = weightChart.xAxis
        xAxis.apply {
            xAxis.labelRotationAngle = 45f
            textColor = Color.WHITE
            position = XAxis.XAxisPosition.BOTTOM
            granularity = 1f
            setDrawAxisLine(true)
            setDrawGridLines(false)

            xAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    val index = value.toInt()
                    return if (index >= 0 && index < dateListPresent.size) {
                        val inputDateStr = dateListPresent[index]
                        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
                        val outputFormat = SimpleDateFormat("MMM dd")

                        try {
                            val date = inputFormat.parse(inputDateStr)
                            return outputFormat.format(date)
                        } catch (e: Exception) {
                            e.printStackTrace()
                            // Return the original string if parsing fails
                            inputDateStr
                        }
                    } else {
                        ""
                    }
                }
            }
        }

        val leftAxis = weightChart.axisLeft
        leftAxis.apply {
            setDrawAxisLine(false)
            textColor = Color.WHITE
        }

        val rightAxis = weightChart.axisRight
        rightAxis.apply {
            setDrawAxisLine(false)
            textColor = Color.WHITE
            setDrawLabels(false)
        }

        val legend = weightChart.legend
        legend.apply {
            form = Legend.LegendForm.LINE
            textColor = Color.WHITE
        }
        weightChart.setExtraBottomOffset(10f)
        lineDataSet.valueTextSize = 11f
    }


//Den här koden behöver inte ändras. DEN ÄR FÄRDIG

    private fun retrieveUserData(deviceImei: String, callback: (User?) -> Unit) {
        val userRef = myRef.child("users").child(deviceImei)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User data exists
                    val name = dataSnapshot.child("name").getValue(String::class.java) ?: ""
                    val gender = dataSnapshot.child("gender").getValue(String::class.java) ?: ""
                    val age = dataSnapshot.child("age").getValue(String::class.java) ?: ""
                    val height = dataSnapshot.child("height").getValue(String::class.java) ?: ""
                    val lastWeight =
                        dataSnapshot.child("lastWeight").getValue(String::class.java) ?: ""

                    val weightEntries = mutableListOf<WeightEntry>()
                    val weightSnapshot = dataSnapshot.child("daily")

                    for (entrySnapshot in weightSnapshot.children) {
                        val value = entrySnapshot.child("value").getValue(String::class.java) ?: ""
                        val nowDate = entrySnapshot.child("date").getValue(String::class.java) ?: ""
                        val calories =
                            entrySnapshot.child("calories").getValue(String::class.java) ?: ""


                        val weightEntry = WeightEntry(value, nowDate, calories)
                        weightEntries.add(weightEntry)
                    }

                    val userData = User(name, gender, age, height, lastWeight, weightEntries)
                    callback(userData)
                } else {
                    // User data doesn't exist
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
                callback(null)
            }
        })
    }


    private fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
    }
}