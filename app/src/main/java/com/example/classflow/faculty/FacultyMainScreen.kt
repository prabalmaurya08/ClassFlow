package com.example.classflow.faculty

import android.app.DownloadManager
import android.content.Context
import android.content.Context.*
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.classflow.R

import com.example.classflow.databinding.FragmentFacultyMainScreenBinding
import com.example.classflow.faculty.facultyAssignment.FacultyAssignment

import com.example.classflow.faculty.facultyAttendance.FacultyAttendanceMainScreen
import com.example.classflow.faculty.facultyAttendance.FacultyAttendanceMainScreenDirections
import com.example.classflow.faculty.facultytimetable.FacultyTimeTable
import com.google.android.material.bottomnavigation.BottomNavigationView


class FacultyMainScreen : Fragment() , FacultyAttendanceMainScreen.OnClassClickListener,FacultyAssignment.OnFaClassClickListener {

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

    override fun onClassClicked(section: String, subject: String) {
        try {
            // Navigate to Faculty Detail Screen
            val action = FacultyMainScreenDirections
                .actionFacultyMainScreenToFacultyAttendancescreenForAllotedclass(section, subject)
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e("AdminMainScreen", "Navigation failed: ${e.message}")
        }
    }

    override fun onFaClassClicked(section: String, subject: String) {
        try {
            // Navigate to Faculty Detail Screen
            val action = FacultyMainScreenDirections
                .actionFacultyMainScreenToFacultyAssignmentDetail(section, subject)
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e("AdminMainScreen", "Navigation failed: ${e.message}")
        }
    }


}