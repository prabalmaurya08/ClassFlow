package com.example.classflow.student

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.classflow.R
import com.example.classflow.admin.AdminMainScreenViewPagerAdaptor
import com.example.classflow.databinding.FragmentStudentMainScreenBinding
import com.example.classflow.faculty.FacultyMainScreenDirections
import com.example.classflow.student.studentAssignment.StudentAssignment
import com.example.classflow.student.studentAttendance.StudentAttendance
import com.google.android.material.bottomnavigation.BottomNavigationView


class StudentMainScreen : Fragment(),StudentAssignment.OnSubjectClickListener, StudentAttendance.OnSectionClickListener {
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
        // Set up the ViewPager adapter
        val adapter = StudentMainScreenViewPagerAdapter(childFragmentManager, lifecycle) // Define your own adapter
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_homePage -> viewPager.currentItem = 0
                R.id.bottom_nav_timeTable -> viewPager.currentItem = 1
                R.id.bottom_nav_attendance -> viewPager.currentItem = 2
                R.id.bottom_nav_assignment -> viewPager.currentItem = 3
            }
            true
        }
    }

    override fun onSubjectClicked(subject: String) {
        try {
            // Navigate to Faculty Detail Screen
            val action = StudentMainScreenDirections
                .actionStudentMainScreenToStudentAssignmentDetailScreen(subject)
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e("StudentNav", "Navigation failed: ${e.message}")
        }
    }

    override fun onSectionClick(subjectName: String) {
        try {
            // Navigate to Faculty Detail Screen
            val action = StudentMainScreenDirections
                .actionStudentMainScreenToStudentSubjectAttendance(subjectName)
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e("StudentNav", "Navigation failed: ${e.message}")
        }
    }


}