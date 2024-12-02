package com.example.classflow.faculty.facultyLogin

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.classflow.R
import com.example.classflow.databinding.FragmentFacultyLoginBinding
import com.example.classflow.databinding.FragmentStudentLoginBinding
import com.example.classflow.student.studentLogin.StudentLogin.OnStudent
import com.example.classflow.student.studentLogin.StudentViewModel
import com.google.firebase.auth.FirebaseAuth


class FacultyLogin : Fragment() {
    private  var listener: OnFaculty ?=null
    private lateinit var binding: FragmentFacultyLoginBinding

    private lateinit var auth: FirebaseAuth
    //using Login viewmodal which is on StudentViewModel
    private lateinit var loginViewModel:StudentViewModel


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

        facultyLogin()
        return binding.root
    }


    private fun facultyLogin() {

        loginViewModel= ViewModelProvider(this)[StudentViewModel::class.java]

        binding.facultyLoginButton.setOnClickListener {


            val email =   binding.facultyLoginEmailAddress.text.toString().trim()
            val password = binding.facultyLoginpassword.text.toString().trim()

            if (validateInput(email, password)) {


                loginViewModel.login(email, password)
                observeLoginResult()
            } else {
                Toast.makeText(requireContext(), "Please enter valid email and password", Toast.LENGTH_SHORT).show()
           }
        }
    }

    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->


            result.onSuccess { userId ->
                loginViewModel.getUserRole(userId).observe(viewLifecycleOwner) { userRole ->
                    if (userRole == "faculty") {

                        listener?.onFacultyLoginSuccess()
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        // Block login if the role is not doctor
                        auth.signOut()  // Sign out the user
                        Toast.makeText(requireContext(), "This account does not belong to a Doctor", Toast.LENGTH_SHORT).show()
                    }
                }
            }.onFailure {
                // Login failed, show an error message
                Toast.makeText(requireContext(), "Login Failed: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun validateInput(email: String, password: String): Boolean {
        return when {
            email.isEmpty() || password.isEmpty() -> false
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                Toast.makeText(requireContext(), "Invalid Email format", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    interface OnFaculty{
        fun onFacultySignupClicked()
        fun onFacultyLoginSuccess()
    }
}