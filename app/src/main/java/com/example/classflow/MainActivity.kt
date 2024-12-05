package com.example.classflow

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.classflow.admin.adminStudent.StudentUploadTimeTable
import com.example.classflow.faculty.facultyLogin.FacultyLogin
import com.example.classflow.faculty.facultyhome.FacultyHomeScreen
import com.example.classflow.faculty.facultytimetable.FacultyTimeTable
import com.example.classflow.student.studentHomePage.StudentHomePage
import com.example.classflow.student.studentLogin.StudentLogin
import com.example.classflow.student.studentTimeTable.StudentTimeTable
import java.io.File

class MainActivity : AppCompatActivity(), StudentLogin.OnStudent, FacultyLogin.OnFaculty,FacultyTimeTable.OnDownloadButtonClickListener,FacultyTimeTable.OnViewPdfButtonClickListener,StudentTimeTable.OnStdDownloadButtonClickListener ,StudentTimeTable.OnStdViewPdfButtonClickListener,FacultyHomeScreen.OnLogoutListener
,StudentUploadTimeTable.OnAdminLogoutListener,StudentHomePage.OnStdLogoutListener{

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

    override fun onAdminLoginClicked() {
       findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_adminLogin2)
    }

    override fun onFacultySignupClicked() {
        findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_facultySignUp)
    }

    override fun onFacultyLoginSuccess() {
        findNavController(R.id.fragment).navigate(R.id.action_mainLogin_to_facultyMainScreen)

    }
    // Implement the listener method
    override fun onDownloadButtonClicked(pdfUrl: String) {
        // Call the download function from here
        downloadPdf(pdfUrl)
    }

    private fun downloadPdf(pdfUrl: String) {
        // Ensure that you're getting the DownloadManager from the correct context
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
            .setTitle("Downloading Faculty Timetable")
            .setDescription("Downloading PDF from Firebase Storage")
            .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "faculty_timetable.pdf")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setMimeType("application/pdf")

        // Correct usage of getSystemService(Context.DOWNLOAD_SERVICE)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show()
    }

    override fun onViewPdfButtonClicked(pdfFile: File) {
        if (!pdfFile.exists()) {
            Toast.makeText(this, "PDF file not found. Please download it first.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val pdfUri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider", // Ensure this matches your FileProvider authority
                pdfFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(pdfUri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            // Check if any app can handle this intent
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No PDF viewer found. Please install one.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to open PDF. Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStdDownloadButtonClicked(pdfUrl: String) {
        stdDownloadPdf(pdfUrl)
    }
    private fun stdDownloadPdf(pdfUrl: String) {
        // Ensure that you're getting the DownloadManager from the correct context
        val request = DownloadManager.Request(Uri.parse(pdfUrl))
            .setTitle("Downloading Student Timetable")
            .setDescription("Downloading PDF from Firebase Storage")
            .setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, "student_timetable.pdf")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setMimeType("application/pdf")

        // Correct usage of getSystemService(Context.DOWNLOAD_SERVICE)
        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)

        Toast.makeText(this, "Download started", Toast.LENGTH_SHORT).show()
    }

    override fun onStdViewPdfButtonClicked(pdfFile: File) {
        if (!pdfFile.exists()) {
            Toast.makeText(this, "PDF file not found. Please download it first.", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val pdfUri = FileProvider.getUriForFile(
                this,
                "${packageName}.fileprovider", // Ensure this matches your FileProvider authority
                pdfFile
            )

            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(pdfUri, "application/pdf")
                flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            }

            // Check if any app can handle this intent
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                Toast.makeText(this, "No PDF viewer found. Please install one.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Unable to open PDF. Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onLogout() {
        findNavController(R.id.fragment).navigate(R.id.action_facultyMainScreen_to_mainLogin)
    }

    override fun onAdminLogout() {
        findNavController(R.id.fragment).navigate(R.id.action_adminMainScreen_to_mainLogin)
    }

    override fun onStdLogout() {
        findNavController(R.id.fragment).navigate(R.id.action_studentMainScreen_to_mainLogin)
    }

}