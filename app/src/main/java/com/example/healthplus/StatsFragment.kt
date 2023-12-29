package com.example.healthplus

import android.content.Context
import com.example.healthplus.R
import android.os.Bundle
import android.provider.Settings
import android.util.Log
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
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


class StatsFragment : Fragment() {


    val database =
        Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")

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




        retrieveUserData(getAndroidId(requireContext())) { userData ->
            if (userData != null) {
                //Saker händer med User Objekt
                //Det här är en for-loop som loopar igenom allt som är under daily.
                //Det enda du behöver ha koll på är variablerna weightEntry.value hämtar värde på vikt historiker osv...
                //Här är allt du behöver jobba med.
                for (weightEntry in userData.weightEntries) {
                    Log.d("Joy", "Weight: ${weightEntry.value}")
                    Log.d("Joy", "Calories: ${weightEntry.calories}")
                    Log.d("Joy", "Date: ${weightEntry.date}")
                }

            } else {
                //Inget Händer
            }
        }

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