package com.example.classflow

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.classflow.faculty.facultyLogin.FacultyLogin
import com.example.classflow.student.studentLogin.StudentLogin

class MainActivity : AppCompatActivity(), StudentLogin.OnStudent, FacultyLogin.OnFaculty {

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

    }

    override fun onStudentSignupClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_studentSignUp)
    }

    override fun onStudentLoginSuccess() {
        findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_studentMainScreen)

    }

    override fun onFacultySignupClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_facultySignUp)
    }

    override fun onFacultyLoginSuccess() {
        findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_facultyMainScreen)

    }
}