package com.example.classflow.splashScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.classflow.R
import com.example.classflow.databinding.FragmentSplashScreenBinding

class SplashScreen : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Load the animation
        val splashAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.splash_animation)

        // Start animation on the ImageView
        binding.splashImage.startAnimation(splashAnimation)

        // Set up a listener for when the animation ends
        splashAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                // Optional: Add logic if needed when animation starts
            }

            override fun onAnimationEnd(animation: Animation?) {
                // Navigate to the next screen when animation finishes
                findNavController().navigate(R.id.action_splashScreen_to_mainLogin)
            }

            override fun onAnimationRepeat(animation: Animation?) {
                // Not needed for splash animations
            }
        })
    }
}
