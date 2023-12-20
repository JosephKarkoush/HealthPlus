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
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener



class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding get() = _binding!!

    val database = Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        val updateButton = binding.start
        updateButton.setOnClickListener {
        val telephonyManager =
            requireActivity().getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val deviceImei = telephonyManager.imei
        val name = binding.name.text.toString()
        val gender = "Male"
        val age = binding.age.text.toString().toDouble()
        val weight = binding.weight.text.toString().toDouble()
        val height = binding.height.text.toString().toDouble()
        updateUser(deviceImei, name, gender, age, weight, height)

    }
        myRef.addValueEventListener(userListener)

//dsdss
        return view
    }


    fun updateUser( deviceImei:String, name:String, gender:String,age:Double,weight:Double,height:Double,){
        val user = User(deviceImei, name, gender, age, weight, height)
        myRef.child(deviceImei).setValue(user)
    }


    val userListener = object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            val name = binding.name
            name.setText("")
            val age = binding.age
            age.setText("")
            val weight = binding.weight
            weight.setText("")
            val height = binding.height
            height.setText("")
            //-----
            var nbrOfChildren = snapshot.childrenCount
            //-----
            var nameQ = ""
            var ageQ = ""
            var weightQ = ""
            var heightQ = ""

            val children = snapshot.children
            children.forEach {
                var user = it.getValue(User::class.java)
                nameQ = (user?.name ?: "No Name")
                ageQ = (user?.age ?: "No Name").toString()
                weightQ = (user?.weight ?: "No Name").toString()
                heightQ = (user?.height ?: "No Name").toString()
            }
            name.setText(nameQ)
            age.setText(ageQ)
            weight.setText(weightQ)
            height.setText(heightQ)
        }




        override fun onCancelled(error: DatabaseError) {
            TODO("Not yet implemented")
        }
    }

}
