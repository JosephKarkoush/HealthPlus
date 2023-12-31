package com.example.healthplus

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthplus.databinding.FragmentFoodBinding
import android.widget.Button
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.launch
import java.time.LocalDate


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
    private var carb: String = ""

    val database =
        Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")


    lateinit var androidId: String


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFoodBinding.inflate(inflater, container, false)
        val view = binding.root
        val foodButton = binding.getFoodButton
        val dataText = binding.foodData
        val nameText = binding.foodNames
        val firstRow = binding.firstRow
        val secondRow = binding.secondRow
        val dataTable = binding.datatable
        androidId = getAndroidId(requireContext())
        nameText.setText(
            getString(R.string.food_details_name) + "\n" + getString(R.string.food_details_calories) + "\n" + getString(
                R.string.food_details_size
            ) + "\n" + getString(R.string.food_details_fat) + "\n" + getString(R.string.food_details_fat_saturated) + "\n"
                    + getString(R.string.food_details_protein) + "\n" + getString(R.string.food_details_sodium) + "\n" + getString(
                R.string.food_details_potassium
            ) + "\n" + getString(R.string.food_details_cholesterol) + "\n" + getString(R.string.food_details_fiber) + "\n" + getString(
                R.string.food_details_sugar
            )
        )


        val sharedViewModel =
            ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        foodButton.setOnClickListener {
            lifecycleScope.launch {
                if (binding.food.query.toString().isEmpty()) {
                    showPopup(it)
                } else {
                    sharedViewModel.setQueryString(binding.food.query.toString())
                }

                sharedViewModel.food.observe(viewLifecycleOwner) { food ->
                    if (food.isNotEmpty()) {
                        name = food[0].name
                        calories = food[0].calories
                        serving_size = food[0].serving_size_g
                        fat_total = food[0].fat_total_g
                        fat_saturated = food[0].fat_saturated_g
                        protein = food[0].protein_g
                        sodium = food[0].sodium_mg
                        potassium = food[0].potassium_mg
                        cholesterol = food[0].cholesterol_mg
                        fiber = food[0].fiber_g
                        sugar = food[0].sugar_g
                        carb = food[0].carbohydrates_total_g

                        dataText.setText(
                            "$name" + "\n" + "$calories" + "\n" + "$serving_size" + "\n" + "$fat_total" + "\n" + "$fat_saturated" + "\n"
                                    + "$protein" + "\n" + "$sodium" + "\n" + "$potassium" + "\n" + "$cholesterol" + "\n" + "$fiber" + "\n" + "$sugar"
                        )

                        binding.cal.setText(getString(R.string.calorie) + "   " + "$calories" + " ")
                        binding.fat.setText(getString(R.string.fat) + "   " + "$fat_total" + " g")
                        binding.carb.setText(getString(R.string.carbs) + "   " + "$carb" + " g")
                        binding.protin.setText(getString(R.string.protein) + "   " + "$protein" + " g")

                        firstRow.visibility = View.VISIBLE
                        secondRow.visibility = View.VISIBLE
                        dataTable.visibility = View.VISIBLE
                    } else {
                        // Handle the case when the API response is empty or null
                        // You can show a toast or a Snackbar to inform the user
                        Toast.makeText(activity, getString(R.string.datanotfound), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


        binding.add.setOnClickListener {
            lifecycleScope.launch {
                if (binding.food.query.toString().isEmpty()) {
                    showPopup(it)
                } else {
                    sharedViewModel.setQueryString(binding.food.query.toString())
                    Toast.makeText(
                        activity,
                        name + getString(R.string.isadded),
                        Toast.LENGTH_LONG
                    ).show()
                }

                sharedViewModel.food.observeOnce(viewLifecycleOwner) { food ->
                    if (food.isNotEmpty()) {
                        name = food[0].name
                        val caloriesNew = food[0].calories

                        retrieveUserData(androidId) { userData ->
                            if (userData != null) {
                                val dateList = ArrayList<String>()

                                for (weightEntry in userData.weightEntries) {
                                    weightEntry.date?.let { dateList.add(it) }
                                }

                                if (dateList.contains(LocalDate.now().toString())) {
                                    val targetWeightEntry = userData.weightEntries.find {
                                        it.date == LocalDate.now().toString()
                                    }
                                    val oldCalories = targetWeightEntry!!.calories?.toDouble()
                                    val newCalorie = oldCalories?.plus(caloriesNew.toDouble())
                                    updateUser(androidId, newCalorie.toString())
                                } else {
                                    updateNewDayUser(androidId, "0.0", userData.lastWeight)
                                    val targetWeightEntry = userData.weightEntries.find {
                                        it.date == LocalDate.now().toString()
                                    }
                                    if (targetWeightEntry != null) {
                                        Log.d("Joy", targetWeightEntry.calories.toString())
                                        val oldCalories = targetWeightEntry.calories?.toDouble()
                                        val newCalorie = oldCalories?.plus(caloriesNew.toDouble())
                                        updateUser(androidId, newCalorie.toString())
                                    }
                                }
                            } else {
                                // Handle the case when user data is null
                                // (e.g., user doesn't exist in the database)
                                Log.e("FoodFragment", "User data is null")
                            }
                        }
                    } else {
                        Toast.makeText(activity, getString(R.string.datanotfound), Toast.LENGTH_SHORT).show()
                    }
                }
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


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUser(
        deviceImei: String,
        calories: String,
    ) {
        val userRef = myRef.child("users").child(deviceImei)
        val nowDate = LocalDate.now().toString()
        val weightRef = userRef.child("daily").child(nowDate).child("calories")
        weightRef.setValue(calories)


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateNewDayUser(
        deviceImei: String,
        calories: String,
        lastWeight: String,
    ) {
        val userRef = myRef.child("users").child(deviceImei)
        val nowDate = LocalDate.now().toString()
        val weightEntry = WeightEntry(lastWeight, nowDate, calories)
        val weightRef = userRef.child("daily").child(nowDate)
        weightRef.setValue(weightEntry)


    }


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


    // Extension function to observe LiveData only once
    fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: Observer<T>) {
        observe(owner, object : Observer<T> {
            override fun onChanged(t: T) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

}