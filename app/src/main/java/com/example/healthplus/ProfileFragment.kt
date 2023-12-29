package com.example.healthplus

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthplus.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.defaultDecayAnimationSpec
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import java.time.LocalDate


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val database =
        Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")

    lateinit var androidId: String


    @SuppressLint("HardwareIds")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        val updateButton = binding.start
        val manRadio = binding.radioButton1
        val womanRadio = binding.radioButton2
        val image = binding.image
        image.setBackgroundResource(R.drawable.man_1)
        androidId = getAndroidId(requireContext())

        manRadio.setOnClickListener {
            val image = binding.image
            image.setBackgroundResource(R.drawable.man_1)
        }

        womanRadio.setOnClickListener {
            val image = binding.image
            image.setBackgroundResource(R.drawable.woman_1)

        }


        updateButton.setOnClickListener {
            val name = binding.name.text.toString()
            var gender = ""
            if (binding.radioButton1.isChecked)
                gender = "Male"
            else gender = "Female"
            val age = binding.age.text.toString()
            val weight = binding.weight.text.toString()
            val height = binding.height.text.toString()




            updateUser(androidId, name, gender, age, weight, height, weight)
            retrieveUserData(androidId) { userData ->
                if (userData != null) {
                    //Saker händer med User Objekt
                    binding.name.setText(userData.name)
                    binding.age.setText(userData.age)
                    binding.weight.setText(userData.lastWeight)
                    binding.height.setText(userData.height)
                    if (userData.gender == "Male") {
                        binding.radioButton1.isChecked = true
                        binding.radioButton2.isChecked = false
                        binding.image.setBackgroundResource(R.drawable.man_1)
                    } else {
                        binding.radioButton1.isChecked = false
                        binding.radioButton2.isChecked = true
                        binding.image.setBackgroundResource(R.drawable.woman_1)
                    }
                    /*
                    for (weightEntry in userData.weightEntries) {
                        println("Weight: ${weightEntry.value}")
                    }
                     */
                } else {
                    //Inget Händer
                }
            }
            Toast.makeText(activity, "Profile Is Up To Date", Toast.LENGTH_LONG).show()

        }
        retrieveUserData(androidId) { userData ->
            if (userData != null) {
                //Saker händer med User Objekt
                binding.name.setText(userData.name)
                binding.age.setText(userData.age)
                binding.weight.setText(userData.lastWeight)
                binding.height.setText(userData.height)
                if (userData.gender == "Male") {
                    binding.radioButton1.isChecked = true
                    binding.radioButton2.isChecked = false
                    binding.image.setBackgroundResource(R.drawable.man_1)
                } else {
                    binding.radioButton1.isChecked = false
                    binding.radioButton2.isChecked = true
                    binding.image.setBackgroundResource(R.drawable.woman_1)
                }
            } else {
                //Inget Händer
            }
        }
        return view
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateUser(
        deviceImei: String,
        name: String,
        gender: String,
        age: String,
        weight: String,
        height: String,
        lastWeight: String,
    ) {
        val userRef = myRef.child("users").child(deviceImei)
        userRef.child("name").setValue(name)
        userRef.child("gender").setValue(gender)
        userRef.child("age").setValue(age)
        userRef.child("height").setValue(height)
        userRef.child("lastWeight").setValue(lastWeight)
        val nowDate = LocalDate.now().toString()




        val weightEntry = WeightEntry(weight, nowDate, "0.0")
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


}
