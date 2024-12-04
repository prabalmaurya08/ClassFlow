package com.example.classflow.student.studentTimeTable

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.classflow.databinding.FragmentStudentTimeTableBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File


class StudentTimeTable : Fragment() {
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: FragmentStudentTimeTableBinding


    // Declare the listener
    private var downloadListener: OnStdDownloadButtonClickListener? = null
    private var viewListener: OnStdViewPdfButtonClickListener? = null

    // Listener interfaces
    interface OnStdDownloadButtonClickListener {
        fun onStdDownloadButtonClicked(pdfUrl: String)
    }

    interface OnStdViewPdfButtonClickListener {
        fun onStdViewPdfButtonClicked(pdfFile: File)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnStdDownloadButtonClickListener) {
            downloadListener = context
        } else {
            throw RuntimeException("$context must implement OnDownloadButtonClickListener")
        }

        if (context is OnStdViewPdfButtonClickListener) {
            viewListener = context
        } else {
            throw RuntimeException("$context must implement OnViewPdfButtonClickListener")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentStudentTimeTableBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FacultyTimetable", "onViewCreated called")

        checkPermissions()

        // Download button functionality
        binding.downloadButton.setOnClickListener {
            checkPermissions()
            fetchAndLoadStudentTimetable()
        }

        // View PDF button functionality
        binding.viewButton.setOnClickListener {
            val fileName = "student_timetable.pdf" // Ensure this matches your downloaded file name
            val pdfFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)

            if (pdfFile.exists()) {
                viewListener?.onStdViewPdfButtonClicked(pdfFile)
            } else {
                Toast.makeText(requireContext(), "PDF not found. Please download it first.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun fetchAndLoadStudentTimetable() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val firebaseUid = firebaseAuth.currentUser?.uid
                if (firebaseUid.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Fetch studentRollNo from the 'users' collection
                val userDoc = firestore.collection("users").document(firebaseUid).get().await()
                if (!userDoc.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "User profile not found", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val studentRollNo = userDoc.getString("studentRollNo") ?: ""
                if (studentRollNo.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Student Roll Number not found in user profile", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Fetch section name from the 'studentProfiles' collection
                val studentProfileDoc = firestore.collection("studentProfiles").document(studentRollNo).get().await()
                if (!studentProfileDoc.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Student profile not found", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val sectionName = studentProfileDoc.getString("studentSection") ?: ""
                if (sectionName.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Student section not found in profile", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Fetch timetable URL from the 'sections' collection
                val sectionDoc = firestore.collection("sections").document(sectionName).get().await()
                if (sectionDoc.exists()) {
                    val timetableUrl = sectionDoc.getString("timetableUrl")
                    if (!timetableUrl.isNullOrEmpty()) {
                        withContext(Dispatchers.Main) {
                            downloadListener?.onStdDownloadButtonClicked(timetableUrl)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Timetable URL not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Section not found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun checkPermissions(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            true
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
            false
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchAndLoadStudentTimetable()
            } else {
                Toast.makeText(requireContext(), "Permissions required to download PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }
}