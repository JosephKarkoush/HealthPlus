package com.example.healthplus

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment

class CalCalculator : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_cal_calculator, container, false)
        val kgValue = view.findViewById<TextView>(R.id.editTextCM)
        val cmValue = view.findViewById<TextView>(R.id.editTextKG)
        val yearValue = view.findViewById<TextView>(R.id.editTextYear)
        val clcButton = view.findViewById<Button>(R.id.button_calculator)
        val calories = view.findViewById<TextView>(R.id.dailyCaloriesText)
        val protein = view.findViewById<TextView>(R.id.proteinLabel)
        val carbs = view.findViewById<TextView>(R.id.carbsLabel)
        val fat = view.findViewById<TextView>(R.id.fatLabel)


        clcButton.setOnClickListener {
            val weight = kgValue.text.toString().toFloatOrNull() ?: 0f
            val height = cmValue.text.toString().toFloatOrNull() ?: 0f
            val yearOfBirth = yearValue.text.toString().toIntOrNull() ?: 0

            if (weight > 0 && height > 0 && yearOfBirth > 0) {

                val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
                val age = currentYear - yearOfBirth

                val bmr = 88.362f + (13.397f * weight) + (4.799f * height) - (5.677f * age)

                calories.text = "Daily Calories: ${bmr.toInt()}"


                val proteinValue = weight * 0.8
                val carbsValue = weight * 5
                val fatValue = weight * 0.5

                protein.text = "Protein: ${proteinValue.toInt()}g"
                carbs.text = "Carbs: ${carbsValue.toInt()}g"
                fat.text = "Fat: ${fatValue.toInt()}g"
            } else {
                showErrorDialog()
            }
        }
        return view
    }

    private fun showErrorDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error")
        builder.setMessage("Please enter valid values for weight, height, and year of birth.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.show()
    }
}