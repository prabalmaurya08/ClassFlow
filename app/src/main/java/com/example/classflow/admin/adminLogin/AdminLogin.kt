package com.example.classflow.admin.adminLogin

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.classflow.R

import com.example.classflow.databinding.FragmentAdminLoginBinding
import com.google.firebase.auth.FirebaseAuth



class AdminLogin : Fragment() {
    private lateinit var binding: FragmentAdminLoginBinding

    private lateinit var auth: FirebaseAuth
    private val adminEmail = "admin01@gmail.com" // Replace with your admin email
    private val adminPassword = "123456"


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAdminLoginBinding.inflate(layoutInflater)
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        binding.adminLoginButton.setOnClickListener {
            val email = binding.adminLoginEmailAddress.text.toString().trim()
            val password = binding.adminLoginPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                loginAdmin(email, password)
            } else {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }


        binding.AdminBackButton.setOnClickListener {
            findNavController().navigate(R.id.action_adminLogin2_to_mainLogin)
        }

        return binding.root
    }

    private fun loginAdmin(email: String, password: String) {



        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null && user.email == adminEmail) {
                        Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                        // Navigate to admin dashboard or perform admin actions

                        findNavController().navigate(R.id.action_adminLogin2_to_adminMainScreen)
                    } else {
                        Toast.makeText(requireContext(), "You are not authorized", Toast.LENGTH_SHORT).show()
                        auth.signOut() // Sign out non-admin users
                    }
                } else {
                    Toast.makeText(requireContext(), "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }

    }


}