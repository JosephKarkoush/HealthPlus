package com.example.healthplus

import ViewPagerAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.healthplus.databinding.FragmentTabsBinding

class TabsFragment : Fragment() {
    private var _binding : FragmentTabsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTabsBinding.inflate(inflater, container, false)
        val view = binding.root

        setUpTabs()

        return view
    }


    private fun setUpTabs() {
        val adapter = ViewPagerAdapter(supportFragmentManager = requireFragmentManager())

        // Instantiate your fragments
        val bmiFragment = BmiFragment()
        val calCalculatorFragment = CalCalculator() // Assuming CalCalculatorFragment is your fragment

        // Add fragments to the adapter
        adapter.addFragment(bmiFragment, "BMI")
        adapter.addFragment(calCalculatorFragment, "Calorie")

        // Set up ViewPager and TabLayout
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)

        // Set icons for tabs
        binding.tabs.getTabAt(0)?.setIcon(R.drawable.bmi)
        binding.tabs.getTabAt(1)?.setIcon(R.drawable.calorie)
    }
}