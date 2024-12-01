package com.example.classflow.student.studentLogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.classflow.R
import com.example.classflow.databinding.FragmentStudentSignUpBinding
import com.example.classflow.mvvm.User


class StudentSignUp : Fragment() {
    private lateinit var binding:FragmentStudentSignUpBinding
    private lateinit var viewModel: StudentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentStudentSignUpBinding.inflate(layoutInflater)

        binding.StuBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_studentSignUp_to_mainLogin)
        }
        sectionSpinner()

        //set up Signup View Model
        setUpSignUpViewModel()
        return binding.root
    }
    private fun setUpSignUpViewModel(){
        viewModel= ViewModelProvider(this)[StudentViewModel::class.java]
        binding.StuSignUpButton.setOnClickListener {
            val name=binding.studentName.text.toString()
            val email=binding.StuSignUpEmailAddress.text.toString()
            val password=binding.studentPassword.text.toString()
            val section=binding.studentSection.selectedItem.toString()
            val studentRollNo=binding.StuEnterRollNo.text.toString()
            val studentUser=User(name,email,password,"student",section, studentRollNo)
            viewModel.signUpStudent(studentUser)

        }

        viewModel.signupResult.observe(viewLifecycleOwner){
            if (it){
                findNavController().navigate(R.id.action_studentSignUp_to_mainLogin)
            }
            else{
                binding.StuSignUpEmailAddress.error="Email Already Exists"
            }
        }


    }

    private fun sectionSpinner() {
        val genders = arrayOf("CS41", "CS42", "CS43", "CS44")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, genders)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.studentSection.adapter = adapter
    }


}