package com.example.healthplus

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import com.github.anastr.speedviewlib.SpeedView
import com.github.anastr.speedviewlib.components.Style
import kotlin.math.pow

class BmiFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bmi, container, false)
        val weightField = view.findViewById<EditText>(R.id.weight_bmi)
        val heightField = view.findViewById<EditText>(R.id.height_bmi)
        val ageField = view.findViewById<EditText>(R.id.age_bmi)
        val button = view.findViewById<Button>(R.id.button_bmi)
        val meter = view.findViewById<SpeedView>(R.id.speedView)

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
}