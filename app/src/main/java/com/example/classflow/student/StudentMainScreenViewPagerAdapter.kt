package com.example.classflow.student


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.classflow.admin.adminStudent.StudentUploadTimeTable
import com.example.classflow.databinding.AdminStudentTimeTableCardBinding
import com.example.classflow.student.studentAssignment.StudentAssignment
import com.example.classflow.student.studentAttendance.StudentAttendance
import com.example.classflow.student.studentHomePage.StudentHomePage

import com.example.classflow.student.studentTimeTable.StudentTimeTable

class StudentMainScreenViewPagerAdapter(fragmentmanager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentmanager ,lifecycle) {

    override fun getItemCount(): Int {
        // Number of fragments for ViewPager2
        return 4
    }




    override fun createFragment(position: Int): Fragment {
        // Return the appropriate fragment for each position
        return when (position) {
            0 -> StudentHomePage()
            1 -> StudentTimeTable()
            2 -> StudentAttendance()
            3 -> StudentAssignment()
            else -> throw IllegalStateException("Unexpected position: $position")
        }
    }
}
