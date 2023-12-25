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
        val androidId = getAndroidId(requireContext())
        retrieveUserData(androidId)

        meter.makeSections(3, Color.BLACK, Style.BUTT)
        meter.sections[0].color = Color.parseColor("#21a6f3")
        meter.sections[1].color = Color.parseColor("#40bc64")
        meter.sections[2].color = Color.parseColor("#fc5448")

        button.setOnClickListener {


            try {
                var weight = weightField.text.toString()
                var height = heightField.text.toString()
                var heightInCm = height.toFloat() / 100
                var age = weightField.text.toString()

                var bmi = weight.toFloat() / (heightInCm.pow(2))
                meter.speedTo(bmi)
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
            binding.ageBmi.setText(user.age)
            binding.heightBmi.setText(user.height)
            binding.weightBmi.setText(user.weight)
        }
    }
}