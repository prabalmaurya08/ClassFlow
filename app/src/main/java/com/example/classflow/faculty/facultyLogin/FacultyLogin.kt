package com.example.classflow.faculty.facultyLogin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.classflow.R
import com.example.classflow.databinding.FragmentFacultyLoginBinding
import com.example.classflow.databinding.FragmentStudentLoginBinding
import com.example.classflow.student.studentLogin.StudentLogin.OnStudent


class FacultyLogin : Fragment() {
    private  var listener: OnFaculty ?=null
    private lateinit var binding: FragmentFacultyLoginBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {

            is OnFaculty -> {
                listener=context


            }



            else -> {
                throw ClassCastException("$context must implement OnSignupClickListener")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentFacultyLoginBinding.inflate(layoutInflater)

        //go to sign up Page
        binding.goToSignUp.setOnClickListener {
            listener?.onFacultySignupClicked()
        }
        return binding.root
    }

    interface OnFaculty{
        fun onFacultySignupClicked()
        fun onFacultyLoginSuccess()
    }
}