package com.example.classflow.student.studentLogin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.classflow.databinding.FragmentStudentLoginBinding


class StudentLogin : Fragment() {
    private var listener: OnStudent?=null
    private lateinit var binding: FragmentStudentLoginBinding


    override fun onAttach(context: Context) {
        super.onAttach(context)
        when (context) {

            is OnStudent -> {
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
        binding=FragmentStudentLoginBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment


        //go to signup
        binding.goToSignUp.setOnClickListener {
            listener?.onStudentSignupClicked()
        }
        return binding.root
    }
    interface OnStudent{
        fun onStudentSignupClicked()
        fun onStudentLoginSuccess()
    }


}