package com.example.classflow

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.classflow.faculty.FacultyLogin
import com.example.classflow.student.StudentLogin


class MainLoginViewPagerAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {


    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {

        return when(position){
            0-> StudentLogin()
            else-> FacultyLogin()

        }
    }
}