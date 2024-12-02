package com.example.classflow.faculty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.classflow.R
import com.example.classflow.admin.AdminMainScreenViewPagerAdaptor
import com.example.classflow.databinding.FragmentAdminMainScreenBinding
import com.example.classflow.databinding.FragmentFacultyMainScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class FacultyMainScreen : Fragment() {

    private var _binding: FragmentFacultyMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFacultyMainScreenBinding.inflate(inflater, container, false)
        viewPager = binding.viewPager
        bottomNavigationView = binding.facultyBottomNav
        setupViewPagerWithBottomNavigation()
        return binding.root
    }
    private fun setupViewPagerWithBottomNavigation() {
        // Set up the ViewPager adapter
        val adapter = FacultyMainScreenViewPagerAdapter(childFragmentManager, lifecycle) // Define your own adapter
        viewPager.adapter = adapter

        // Sync ViewPager with BottomNavigationView
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_homePage-> viewPager.currentItem = 0
                R.id.bottom_nav_timeTable -> viewPager.currentItem = 1
                R.id.bottom_nav_attendance -> viewPager.currentItem = 2
                R.id.bottom_nav_assignment -> viewPager.currentItem = 3

            }
            true
        }
    }


}