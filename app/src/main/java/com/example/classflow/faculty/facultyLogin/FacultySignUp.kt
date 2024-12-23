package com.example.classflow.faculty.facultyLogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.classflow.R
import com.example.classflow.databinding.FragmentFacultySignUpBinding
import com.example.classflow.mvvm.User



class FacultySignUp : Fragment() {
    private lateinit var binding: FragmentFacultySignUpBinding
    private lateinit var viewModel: FacultyLoginViewModel

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

        setUpSignUpViewModel()
        return binding.root
    }
    private fun setUpSignUpViewModel(){
        viewModel= ViewModelProvider(this)[FacultyLoginViewModel::class.java]
        binding.FacultySignUpButton.setOnClickListener {
            val name=binding.FacultyEnterName.text.toString()
            val email=binding.FacultySignUpEmailAddress.text.toString().trim()
            val password=binding.FacultySignUppassword.text.toString().trim()

            val facultyId=binding.FacultyId.text.toString()
            val facultyUser= User(name,email,password,"faculty", facultyId = facultyId)
            viewModel.signUpFaculty(facultyUser)

        }

        viewModel.fSignupResult.observe(viewLifecycleOwner){
            if (it){
                findNavController().navigate(R.id.action_facultySignUp_to_mainLogin)
            }
            else{

            }
        }


    }

}