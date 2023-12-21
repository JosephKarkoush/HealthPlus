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
    private var calories : Double = 0.0
    private var serving_size : Double = 0.0
    private var fat_total : Double = 0.0
    private var fat_saturated : Double = 0.0
    private var protein : Double = 0.0
    private var sodium : Double = 0.0
    private var potassium : Double = 0.0
    private var cholesterol : Double = 0.0
    private var fiber : Double = 0.0
    private var sugar : Double = 0.0
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
                val query = searchedProduct.query?.toString() ?: ""
                sharedViewModel.str = query
                sharedViewModel.food.observe(viewLifecycleOwner) { food ->
                    // Hantera matdata här
                    // These variables shadow the class-level properties
                    val name = food.name
                    val calories = food.calories
                }

                // These variables refer to the class-level properties
                dataText.setText("Name: $name" + "\n" +"Calories: $calories" + "\n" + "Size: $serving_size" + "\n" + "Fat: $fat_total" + "\n" + "Fat Saturated: $fat_saturated" + "\n"
                        + "Protein: $protein" + "\n" + "Size: $serving_size" + "\n"+ "Sodium: $sodium" + "\n" + "Potassium: $potassium" + "\n" + "Cholesterol: $cholesterol" + "\n" + "Fiber: $fiber" + "\n" + "Sugar: $sugar")
            }
        }


        return view
    }
}