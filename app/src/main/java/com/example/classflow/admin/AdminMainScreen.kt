package com.example.classflow.admin

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.classflow.R
import com.example.classflow.admin.adminFaculty.FacultyUploadTimeTableAndClassAllot
import com.example.classflow.databinding.FragmentAdminMainScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class AdminMainScreen : Fragment(), FacultyUploadTimeTableAndClassAllot.OnFacultyClickListener {

    private var _binding: FragmentAdminMainScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAdminMainScreenBinding.inflate(inflater, container, false)
        viewPager = binding.viewPager
        bottomNavigationView = binding.adminBottomNav
        setupViewPagerWithBottomNavigation()
        return binding.root
    }

    private fun setupViewPagerWithBottomNavigation() {
        // Set up the ViewPager adapter
        val adapter = AdminMainScreenViewPagerAdaptor(childFragmentManager, lifecycle) // Define your own adapter
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
                R.id.bottom_nav_student -> viewPager.currentItem = 0
                R.id.bottom_nav_facutly -> viewPager.currentItem = 1

            }
            true
        }
    }

    override fun onFacultyClicked(facultyId: String, facultyName: String) {
        try {
            // Navigate to Faculty Detail Screen
            val action = AdminMainScreenDirections
                .actionAdminMainScreenToAdminFacultyDetailScreen(facultyId, facultyName)
            findNavController().navigate(action)
        } catch (e: Exception) {
            Log.e("AdminMainScreen", "Navigation failed: ${e.message}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
