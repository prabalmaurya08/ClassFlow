package com.example.classflow.admin

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.classflow.admin.adminFaculty.FacultyUploadTimeTableAndClassAllot
import com.example.classflow.admin.adminStudent.StudentUploadTimeTable
import com.example.classflow.faculty.facultyLogin.FacultyLogin


class AdminMainScreenViewPagerAdaptor (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        // Number of fragments for ViewPager2
       return 2
    }

    override fun createFragment(position: Int): Fragment {
        // Return the appropriate fragment for each position
        return when (position){
            0 -> FacultyLogin()
            1 -> FacultyUploadTimeTableAndClassAllot()
            else -> throw IllegalStateException("Unexcepted position: $position")
        }
    }
}