package com.example.healthplus

import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import com.example.healthplus.databinding.FragmentHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    val database =
        Firebase.database("https://healthplus-25c48-default-rtdb.europe-west1.firebasedatabase.app/")
    val myRef = database.getReference("users")

    private lateinit var numberOfUserText: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        numberOfUserText = binding.numberOfUsers
        retrieveUserData()
        startGlowingAnimation()






        return view
    }


    private fun retrieveUserData() {
        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var counter = dataSnapshot.getChildrenCount()
                numberOfUserText.setText(counter.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error
            }
        })
    }



    private fun startGlowingAnimation() {
        val animator = ObjectAnimator.ofFloat(numberOfUserText, "alpha", 0.5f, 1f)
        animator.duration = 1000 // Set the duration of the animation
        animator.repeatCount = ObjectAnimator.INFINITE // Repeat the animation infinitely
        animator.repeatMode = ObjectAnimator.REVERSE // Reverse the animation when it repeats
        animator.interpolator = AccelerateDecelerateInterpolator() // Use AccelerateDecelerateInterpolator for smooth animation

        animator.start()
    }


}