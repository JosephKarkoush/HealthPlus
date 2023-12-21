package com.example.healthplus

import android.content.Context.TELEPHONY_SERVICE
import android.os.Bundle
import android.telephony.TelephonyManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getSystemService
import com.example.healthplus.databinding.FragmentProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.database.database
import android.provider.Settings
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    val database =
        Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        val updateButton = binding.start
        val telephonyManager =
            requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val deviceImei = "telephonyManager.imei"


        updateButton.setOnClickListener {
            val name = binding.name.text.toString()
            var gender = ""
            if(binding.radioButton1.isChecked)
                gender = "Male"
            else gender = "Female"
            val age = binding.age.text.toString()
            val weight = binding.weight.text.toString()
            val height = binding.height.text.toString()
            updateUser(deviceImei, name, gender, age, weight, height)

        }

//dsdss
        return view
    }
//TO DOOOO !!!!
    private fun checkExist(str: String) {
        myRef.child(str).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // User exists in the database
                    // Handle the case where the user exists
                    // For example, update the user's information
                    val user = snapshot.getValue(User::class.java)
                    if (user != null) {
                        binding.name.setText(user.name.toString())
                        binding.age.setText(user.age.toString())
                        binding.height.setText(user.height.toString())
                        binding.weight.setText(user.weight.toString())
                        if(user.gender == "Male")
                            binding.radioButton1.isChecked = true
                        else if(user.gender == "Female")
                            binding.radioButton2.isChecked = true

                    }
                } else {
                    // User does not exist in the database
                    // Handle the case where the user does not exist
                    // e.g., show a message, prompt the user to register, etc.
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    fun updateUser(
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



}
