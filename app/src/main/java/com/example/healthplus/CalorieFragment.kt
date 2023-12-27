package com.example.healthplus

import android.content.Context
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import com.example.healthplus.databinding.FragmentBmiBinding
import com.example.healthplus.databinding.FragmentCalorieBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlin.math.round

class CalorieFragment : Fragment() {
    private var _binding: FragmentCalorieBinding? = null
    private val binding get() = _binding!!


    val database =
        Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalorieBinding.inflate(inflater, container, false)
        val view = binding.root
        val calorieButton = binding.buttonCal
        val androidId = getAndroidId(requireContext())
        retrieveUserData(androidId)

        calorieButton.setOnClickListener {
            try {


            val weight = binding.weight.text.toString()
            val height = binding.height.text.toString()
            val yearOfBirth = binding.age.text.toString()
            val maleButton = binding.radioMale
            val femaleButton = binding.radioFemale
            val calorieIntake = binding.daily
            val macros = binding.macros
            val calorieText = binding.calorieText
            var calorie = 0.0
            var protein = 0.0
            var carb = 0.0
            var fat = 0.0

            if (weight.toDouble() > 0 && height.toDouble() > 0 && yearOfBirth.toDouble() > 0) {
                if(maleButton.isChecked){
                    calorie = round(655.0 + (9.6 * weight.toDouble()) + (1.8 * height.toDouble()) - (4.7 * yearOfBirth.toDouble()))
                    protein = round((0.25 * calorie)/4)
                    carb = round((0.45 * calorie)/4)
                    fat = round((0.30 * calorie)/4)
                    calorieText.setText("Daily Calorie Intake")
                    calorieIntake.setText(calorie.toString())
                    macros.setText("$protein" + "\n" + "$carb" + "\n" + "$fat")
                } else if(femaleButton.isChecked){
                    calorie = round(66 + (13.7 * weight.toDouble()) + (5 * height.toDouble()) - (6.8 * yearOfBirth.toDouble()))
                    protein = round((0.25 * calorie)/4)
                    carb = round((0.45 * calorie)/4)
                    fat = round((0.30 * calorie)/4)
                    calorieText.setText("Daily Calorie Intake")
                    calorieIntake.setText(calorie.toString())
                    macros.setText("$protein" + "\n" + "$carb" + "\n" + "$fat")
                }
            } else {
                showPopup(it)
            }

            }catch (e: Exception){
                showPopup(it)
            }
        }



        return view
    }



    private fun showPopup(view: View) {
        // Inflate the popup_layout.xml
        val inflater = LayoutInflater.from(requireContext())
        val popupView: View = inflater.inflate(R.layout.popup_layout, null)

        // Create the popup window
        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )

        // Set a dismiss listener to close the popup when the "Close Popup" button is clicked
        val btnClosePopup: Button = popupView.findViewById(R.id.btnClosePopup)
        btnClosePopup.setOnClickListener {
            popupWindow.dismiss()
        }

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

    }





    private fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
    }






    private fun retrieveUserData(deviceImei: String) {
        myRef.child(deviceImei).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    // Update UI with the retrieved user data
                    updateUI(user)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateUI(user: User?) {
        // Update UI elements with user data
        if (user != null) {
            binding.age.setText(user.age)
            binding.height.setText(user.height)
            binding.weight.setText(user.weight)
            if(user.gender == "Male")
                binding.radioMale.isChecked = true
            else
                binding.radioFemale.isChecked = true
        }
    }
}