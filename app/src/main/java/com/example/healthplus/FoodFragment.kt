package com.example.healthplus

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthplus.databinding.FragmentFoodBinding
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch





class FoodFragment : Fragment() {
    private var _binding: FragmentFoodBinding? = null
    private val binding get() = _binding!!
    private var name: String = ""
    private var calories: String = ""
    private var serving_size: String = ""
    private var fat_total: String = ""
    private var fat_saturated: String = ""
    private var protein: String = ""
    private var sodium: String = ""
    private var potassium: String = ""
    private var cholesterol: String = ""
    private var fiber: String = ""
    private var sugar: String = ""
    private var  carb: String = ""





    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        val view = binding.root
        val foodButton = binding.getFoodButton
        val dataText = binding.foodData
        val nameText = binding.foodNames

        nameText.setText(
            "Name: " + "\n" + "Calories: " + "\n" + "Size: " + "\n" + "Fat: " + "\n" + "Fat Saturated: " + "\n"
                    + "Protein: " + "\n" + "Sodium: " + "\n" + "Potassium: " + "\n" + "Cholesterol: " + "\n" + "Fiber: " + "\n" + "Sugar: "
        )


        val sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)


        foodButton.setOnClickListener {
            lifecycleScope.launch {
                sharedViewModel.str = binding.food.query.toString()
                if (sharedViewModel.str == "") {
                } else {
                    sharedViewModel.food.observe(viewLifecycleOwner) { food ->
                        name = food.get(0).name
                        calories = food.get(0).calories
                        serving_size = food.get(0).serving_size_g
                        fat_total = food.get(0).fat_total_g
                        fat_saturated = food.get(0).fat_saturated_g
                        protein = food.get(0).protein_g
                        sodium = food.get(0).sodium_mg
                        potassium = food.get(0).potassium_mg
                        cholesterol = food.get(0).cholesterol_mg
                        fiber = food.get(0).fiber_g
                        sugar = food.get(0).sugar_g
                        carb = food.get(0).carbohydrates_total_g


                        dataText.setText(
                            "$name" + "\n" + "$calories" + "\n" + "$serving_size" + "\n" + "$fat_total" + "\n" + "$fat_saturated" + "\n"
                                    + "$protein" + "\n" + "$sodium" + "\n" + "$potassium" + "\n" + "$cholesterol" + "\n" + "$fiber" + "\n" + "$sugar"
                        )

                        binding.cal.setText(binding.cal.text.toString() + "\n\n$calories"+" g")
                        binding.fat.setText(binding.fat.text.toString() + "\n\n$fat_total"+" g")
                        binding.carb.setText(binding.carb.text.toString() + "\n\n$carb"+" g")
                        binding.protin.setText(binding.protin.text.toString() + "\n\n$protein"+" g")

                    }
                }
            }

        }

        return view
    }
}