package com.example.healthplus

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.example.healthplus.databinding.FragmentBmiBinding
import com.github.anastr.speedviewlib.SpeedView
import com.github.anastr.speedviewlib.components.Section
import com.github.anastr.speedviewlib.components.Style
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlin.math.pow

class BmiFragment : Fragment() {

    private var _binding: FragmentBmiBinding? = null
    private val binding get() = _binding!!

    val database =
        Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBmiBinding.inflate(inflater, container, false)
        val view = binding.root
        val weightField = binding.weightBmi
        val heightField = binding.heightBmi
        val ageField = binding.ageBmi
        val button = binding.buttonBmi
        val meter = binding.speedView
        val textview = binding.textView
        val infoButton = binding.bmiInfo
        val androidId = getAndroidId(requireContext())
        retrieveUserData(androidId) { userData ->
            if (userData != null) {
                //Saker händer med User Objekt
                binding.ageBmi.setText(userData.age)
                binding.heightBmi.setText(userData.height)
                binding.weightBmi.setText(userData.lastWeight)
            } else {
                //Inget Händer
            }
        }

        infoButton.setOnClickListener{
            showPopupBmi(it)
        }

        meter.clearSections()
        val section1 = Section(0f, .46f, Color.parseColor("#2175f3"),meter.speedometerWidth ,Style.BUTT)
        val section2 = Section(.46f, .62f, Color.parseColor("#40bc64"),meter.speedometerWidth ,Style.BUTT)
        val section3 = Section(.62f, 1f, Color.parseColor("#fc5448"),meter.speedometerWidth ,Style.BUTT)

        meter.addSections(section1)
        meter.addSections(section2)
        meter.addSections(section3)

        meter.tickNumber = 5
        meter.ticks = arrayListOf(.03f, .46f, .62f, .97f)

//        meter.addSections(
//            Section(0f, .462f, Color.parseColor("#2175f3"))
//            , Section(.462f, .624f, Color.parseColor("#40bc64"))
//            , Section(.624f, .1f, Color.parseColor("#fc5448")))

        button.setOnClickListener {
            try {
                var weight = weightField.text.toString()
                var height = heightField.text.toString()
                var heightInCm = height.toFloat() / 100
                var age = weightField.text.toString()

                var bmi = weight.toFloat() / (heightInCm.pow(2))
                meter.speedTo(bmi)

                if (bmi >= 18.5 && bmi <= 24.9){
                    textview.text = "Normal"
                    textview.setTextColor(Color.parseColor("#40bc64"))
                }
                if (bmi >= 17 && bmi <= 18.4){
                    textview.text = "Underweight"
                    textview.setTextColor(Color.parseColor("#21a6f3"))
                }
                if (bmi <= 16.9){
                    textview.text = "Severely underweight"
                    textview.setTextColor(Color.parseColor("#2175f3"))
                }
                if (bmi >= 25 && bmi <= 29.9){
                    textview.text = "Overweight"
                    textview.setTextColor(Color.parseColor("#fc5448"))
                }

                if (bmi >= 30) {
                    textview.text = "Obese"
                    textview.setTextColor(Color.parseColor("#f53022"))
                }

            } catch (e: Exception) {
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

    private fun showPopupBmi(view: View) {
        // Inflate the popup_layout.xml
        val inflater = LayoutInflater.from(requireContext())
        val popupView: View = inflater.inflate(R.layout.popup_bmi, null)

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





/*
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

 */



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
                    val lastWeight = dataSnapshot.child("lastWeight").getValue(String::class.java) ?: ""

                    val weightEntries = mutableListOf<WeightEntry>()
                    val weightSnapshot = dataSnapshot.child("weight")

                    for (entrySnapshot in weightSnapshot.children) {
                        val value = entrySnapshot.child("value").getValue(String::class.java) ?: ""
                        val nowDate = entrySnapshot.child("date").getValue(String::class.java) ?: ""
                        val calories = entrySnapshot.child("calories").getValue(String::class.java) ?: ""

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




/*
    private fun updateUI(user: User?) {
        // Update UI elements with user data
        if (user != null) {
            binding.ageBmi.setText(user.age)
            binding.heightBmi.setText(user.height)
            binding.weightBmi.setText(user.lastWeight)
        }
    }

 */
}