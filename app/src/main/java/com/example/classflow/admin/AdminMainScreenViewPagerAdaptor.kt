package com.example.classflow.admin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.classflow.admin.adminFaculty.FacultyUploadTimeTableAndClassAllot
import com.example.classflow.admin.adminStudent.StudentUploadTimeTable
import com.example.classflow.faculty.facultyLogin.FacultyLogin


class AdminMainScreenViewPagerAdaptor (fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        // Number of fragments for ViewPager2
       return 2
    }

    override fun createFragment(position: Int): Fragment {
        // Return the appropriate fragment for each position
        return when (position){
            0 -> StudentUploadTimeTable()
            1 -> FacultyUploadTimeTableAndClassAllot()
            else -> throw IllegalStateException("Unexcepted position: $position")
        }
    }
}