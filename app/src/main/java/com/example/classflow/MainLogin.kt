package com.example.classflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.classflow.databinding.FragmentMainLoginBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class MainLogin : Fragment() {
    private lateinit var binding: FragmentMainLoginBinding
    private val tabTitles = arrayOf("Student", "Faculty")
    private lateinit var imageView: ImageView
    private lateinit var loginTab: TabLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentMainLoginBinding.inflate(inflater,container,false)

        loginTab = binding.loginTabLayout


        viewPagerWithTabLayout()
        return binding.root
    }

    private fun viewPagerWithTabLayout() {
        binding.loginViewPager.adapter = MainLoginViewPagerAdapter(this)
        val tabLayout = binding.loginTabLayout
        val viewPager = binding.loginViewPager
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]


        }.attach()

        for (i in 0..2) {
            val textView = LayoutInflater.from(requireContext())
                .inflate(R.layout.login_tab_textview, null) as TextView
            binding.loginTabLayout.getTabAt(i)?.customView = textView
        }


        // Set a listener to change the background when a tab is selected
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    val position = it.position
                    // Update background of selected tab
                    (it.customView as TextView).background = when (position) {
                        0 -> ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.login_student_select


                        )


                        else -> ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.login_faculty_select
                        )
                    }
                    updateTabBg(position)
                   // updateImageViewForTab(position)
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                tab?.let {
                    val position = it.position
                    // Update background of unselected tab
                    (it.customView as TextView).background = when (position) {
                        0 -> ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.login_tablayout_bg
                        )

                        else -> ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.login_tablayout_bg
                        )
                    }


                    //update image according to tab change

                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })
    }
    private fun updateTabBg(position: Int){
        when(position){
            0 -> loginTab.setBackgroundResource(R.drawable.login_tab_stroke_bg)
            else -> loginTab.setBackgroundResource(R.drawable.login_faculty_stroke)
        }
    }


}