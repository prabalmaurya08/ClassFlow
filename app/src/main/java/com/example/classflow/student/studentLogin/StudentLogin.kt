package com.example.classflow.student.studentLogin

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider

import com.example.classflow.databinding.FragmentStudentLoginBinding
import com.google.firebase.auth.FirebaseAuth


class StudentLogin : Fragment() {
    private var listener: OnStudent?=null
    private lateinit var binding: FragmentStudentLoginBinding

    private lateinit var loginViewModel: StudentViewModel


    private val auth = FirebaseAuth.getInstance()


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

        binding.adminLogin.setOnClickListener {
            listener?.onAdminLoginClicked()
        }

        studentLogin()
        return binding.root
    }



    private fun studentLogin() {
        loginViewModel = ViewModelProvider(this)[StudentViewModel::class.java]

        binding.StuLoginButton.setOnClickListener {
            val email = binding.StuLoginEmailAddress.text.toString().trim()
            val password = binding.StuLoginpassword.text.toString().trim()

            if (validateInput(email, password)) {
                // Hide the button and show the progress bar
                binding.StuLoginButton.visibility = View.GONE
                binding.img.visibility = View.GONE
                binding.loginProgressBar.visibility = View.VISIBLE

                loginViewModel.login(email, password)
                observeLoginResult()
            } else {
                Toast.makeText(requireContext(), "Please enter valid email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(viewLifecycleOwner) { result ->
            // Restore the button and hide the progress bar when login completes
            binding.StuLoginButton.visibility = View.VISIBLE
            binding.img.visibility = View.VISIBLE
            binding.loginProgressBar.visibility = View.GONE

            result.onSuccess { userId ->
                loginViewModel.getUserRole(userId).observe(viewLifecycleOwner) { userRole ->
                    if (userRole == "student") {
                        listener?.onStudentLoginSuccess()
                        Toast.makeText(requireContext(), "Login Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        auth.signOut()
                        Toast.makeText(requireContext(), "This account does not belong to a Student", Toast.LENGTH_SHORT).show()
                    }
                }
            }.onFailure {
                Toast.makeText(requireContext(), "Login Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                binding.StuLoginButton.visibility = View.VISIBLE
                binding.img.visibility = View.VISIBLE
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
    interface OnStudent{
        fun onStudentSignupClicked()
        fun onStudentLoginSuccess()
        fun onAdminLoginClicked()
    }


}