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
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val database =
        Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")
    var deviceId = ""

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
        val androidId = getAndroidId(requireContext())

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
            updateUser(androidId, name, gender, age, weight, height)
            retrieveUserData(androidId)
            binding.greecheck.isVisible = true

        }
        retrieveUserData(androidId)
        return view
    }


    private fun updateUser(
        deviceImei: String,
        name: String,
        gender: String,
        age: String,
        weight: String,
        height: String,
    ) {
        val user = User(deviceImei, name, gender, age, weight, height)
        myRef.child(deviceImei).setValue(user)
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
            binding.name.setText(user.name)
            binding.age.setText(user.age)
            binding.weight.setText(user.weight)
            binding.height.setText(user.height)
            if(user.gender == "Male") {
                binding.radioButton1.isChecked = true
                binding.radioButton2.isChecked = false
                binding.image.setBackgroundResource(R.drawable.man_1)
            } else {
                binding.radioButton1.isChecked = false
                binding.radioButton2.isChecked = true
                binding.image.setBackgroundResource(R.drawable.woman_1)
            }
            // Update other UI elements as needed
        }
    }



    private fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID) ?: ""
    }





}
