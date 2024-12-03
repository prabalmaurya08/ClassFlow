package com.example.classflow.faculty

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.classflow.faculty.facultyAssignment.FacultyAssignment
import com.example.classflow.faculty.facultyAttendance.FacultyAttendanceMainScreen

import com.example.classflow.faculty.facultyhome.FacultyHomeScreen
import com.example.classflow.faculty.facultytimetable.FacultyTimeTable

class FacultyMainScreenViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        // Number of fragments for ViewPager2
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        // Return the appropriate fragment for each position
        return when (position){
            0 -> FacultyHomeScreen()
            1 -> FacultyTimeTable()
            2-> FacultyAttendanceMainScreen()
         3-> FacultyAssignment()

            else -> throw IllegalStateException("Unexcepted position: $position")
        }
    }
}