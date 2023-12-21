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
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class FoodFragment : Fragment() {
    private var _binding : FragmentFoodBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private var name : String = ""
    private var calories : String = ""
    private var serving_size : String = ""
    private var fat_total : String = ""
    private var fat_saturated : String = ""
    private var protein : String = ""
    private var sodium : String = ""
    private var potassium : String = ""
    private var cholesterol : String = ""
    private var fiber : String = ""
    private var sugar : String = ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        val view = binding.root
        val foodButton = binding.getFoodButton
        val searchedProduct = binding.food
        val dataText = binding.foodData


        foodButton.setOnClickListener {
            lifecycleScope.launch {
                sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
                sharedViewModel.str = binding.food.query.toString()
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

                    dataText.setText("Name: $name" + "\n" +"Calories: $calories" + "\n" + "Size: $serving_size" + "\n" + "Fat: $fat_total" + "\n" + "Fat Saturated: $fat_saturated" + "\n"
                            + "Protein: $protein" + "\n" + "\n"+ "Sodium: $sodium" + "\n" + "Potassium: $potassium" + "\n" + "Cholesterol: $cholesterol" + "\n" + "Fiber: $fiber" + "\n" + "Sugar: $sugar")

                }

            }
        }


        return view
    }
}