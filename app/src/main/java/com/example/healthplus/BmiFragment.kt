package com.example.healthplus

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.github.anastr.speedviewlib.SpeedView
import com.github.anastr.speedviewlib.components.Style
import java.lang.Math.pow
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

        meter.makeSections(3, Color.CYAN, Style.ROUND)
        meter.sections[0].color = Color.GREEN
        meter.sections[1].color = Color.BLUE
        meter.sections[2].color = Color.RED

        button.setOnClickListener {
            var weight = weightField.text.toString()
            var height = weightField.text.toString()
            var age = weightField.text.toString()

            var bmi = weight.toFloat() / height.toFloat().pow(2)

            meter.speedTo(bmi)
        }

        return view
    }

}