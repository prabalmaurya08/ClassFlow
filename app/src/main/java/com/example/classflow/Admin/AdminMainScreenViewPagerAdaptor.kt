package com.example.classflow.Admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.classflow.R


class AdminMainScreenViewPagerAdaptor (fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        // Number of fragments for ViewPager2
       return 2
    }

    override fun createFragment(position: Int): Fragment {
        // Return the appropriate fragment for each position
        return when (position){
            0 -> StudentuploadTimeTable()
            1 -> FacultyUploadTimeTableAndClassAllot()
            else -> throw IllegalStateException("Unexcepted position: $position")
        }
    }
}