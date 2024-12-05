package com.example.classflow.faculty.facultytimetable

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.classflow.databinding.FragmentFacultytimetableBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File

class FacultyTimeTable : Fragment() {

    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()
    private lateinit var binding: FragmentFacultytimetableBinding

    // Declare the listener
    private var downloadListener: OnDownloadButtonClickListener? = null
    private var viewListener: OnViewPdfButtonClickListener? = null

    // Listener interfaces
    interface OnDownloadButtonClickListener {
        fun onDownloadButtonClicked(pdfUrl: String)
    }

    interface OnViewPdfButtonClickListener {
        fun onViewPdfButtonClicked(pdfFile: File)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnDownloadButtonClickListener) {
            downloadListener = context
        } else {
            throw RuntimeException("$context must implement OnDownloadButtonClickListener")
        }

        if (context is OnViewPdfButtonClickListener) {
            viewListener = context
        } else {
            throw RuntimeException("$context must implement OnViewPdfButtonClickListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        downloadListener = null
        viewListener = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFacultytimetableBinding.inflate(inflater, container, false)
        Log.d("FacultyTimetable", "onCreateView called")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("FacultyTimetable", "onViewCreated called")

        checkPermissions()

        // Download button functionality
        binding.downloadButton.setOnClickListener {
            checkPermissions()
            fetchAndLoadFacultyTimetable()
        }

        // View PDF button functionality
//        binding.viewButton.setOnClickListener {
//            val fileName = "faculty_timetable.pdf" // Ensure this matches your downloaded file name
//            val pdfFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName)
//
//            if (pdfFile.exists()) {
//                viewListener?.onViewPdfButtonClicked(pdfFile)
//            } else {
//                Toast.makeText(requireContext(), "PDF not found. Please download it first.", Toast.LENGTH_SHORT).show()
//            }
    //}
    }

    private fun fetchAndLoadFacultyTimetable() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val firebaseUid = firebaseAuth.currentUser?.uid
                if (firebaseUid.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Fetch facultyId from users collection
                val userDoc = firestore.collection("users").document(firebaseUid).get().await()
                if (!userDoc.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "User profile not found", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val facultyId = userDoc.getString("facultyId") ?: ""
                if (facultyId.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Faculty ID not found in user profile", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Fetch timetable URL from facultyProfiles collection
                val facultyDoc =
                    firestore.collection("facultyProfiles").document(facultyId).get().await()
                if (facultyDoc.exists()) {
                    val timetableUrl = facultyDoc.getString("timetableUrl")
                    if (!timetableUrl.isNullOrEmpty()) {
                        withContext(Dispatchers.Main) {
                            downloadListener?.onDownloadButtonClicked(timetableUrl)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(requireContext(), "Timetable URL not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "Faculty profile not found", Toast.LENGTH_SHORT).show()
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
                fetchAndLoadFacultyTimetable()
            } else {
                Toast.makeText(requireContext(), "Permissions required to download PDF", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
