package com.example.classflow.admin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.classflow.R
import com.example.classflow.databinding.FragmentAdminMainScreenBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class AdminMainScreen : Fragment() {
    private lateinit var binding: FragmentAdminMainScreenBinding

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentAdminMainScreenBinding.inflate(layoutInflater)
        setupViewPagerWithBottomNavigation()
        return binding.root
    }

    private fun setupViewPagerWithBottomNavigation() {
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_student-> viewPager.currentItem = 0
                R.id.bottom_nav_facutly -> viewPager.currentItem = 1
            }
            true
        }

    }
}