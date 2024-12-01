package com.example.classflow.faculty.facultyLogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.classflow.R
import com.example.classflow.databinding.FragmentFacultySignUpBinding


class FacultySignUp : Fragment() {
    private lateinit var binding: FragmentFacultySignUpBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentFacultySignUpBinding.inflate(layoutInflater)
        //go back to login

        binding.FacultyBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_facultySignUp_to_mainLogin)
        }
        return binding.root
    }

}