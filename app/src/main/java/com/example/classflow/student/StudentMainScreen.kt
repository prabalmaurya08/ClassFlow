package com.example.classflow.student

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.classflow.R
import com.example.classflow.databinding.FragmentStudentMainScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class StudentMainScreen : Fragment() {
    private lateinit var binding: FragmentStudentMainScreenBinding

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentStudentMainScreenBinding.inflate(layoutInflater)

        viewPager=binding.viewPager
        bottomNavigationView=binding.studentBottomNav

        setupViewPagerWithBottomNavigation()
        return binding.root
    }

    private fun setupViewPagerWithBottomNavigation() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_dashboard -> viewPager.currentItem = 0
                R.id.bottom_nav_timeTable -> viewPager.currentItem = 1
                R.id.bottom_nav_attendance -> viewPager.currentItem = 2
                R.id.bottom_nav_assignment -> viewPager.currentItem = 3
            }
            true
        }
    }


}