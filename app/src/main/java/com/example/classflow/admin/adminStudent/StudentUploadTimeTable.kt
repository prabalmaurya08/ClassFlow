package com.example.classflow.admin.adminStudent

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.R
import com.example.classflow.databinding.FragmentStudentuploadTimeTableBinding

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class StudentUploadTimeTable : Fragment() {
    private lateinit var binding: FragmentStudentuploadTimeTableBinding
    private lateinit var sectionAdapter: SectionAdapter
    private lateinit var sectionList: List<Section>

    private var selectedSectionName: String? = null // Store the selected section name
    private var listener: OnAdminLogoutListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Attach the listener to the hosting activity or parent fragment
        if (context is OnAdminLogoutListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnLogoutListener")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentuploadTimeTableBinding.inflate(inflater, container, false)
        setupRecyclerView()

        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    Log.d("FacultyHomeScreen", "Logout menu clicked")
                    try {
                        // Sign out and navigate

                        Log.d("FacultyHomeScreen", "User signed out successfully")
                        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                        listener?.onAdminLogout()
                        findNavController().popBackStack(R.id.studentUploadTimeTable, false)

//                        // Use safe navigation
//                        if (isAdded && findNavController().currentDestination?.id == R.id.StudentuploadTimeTable) {
//                            Log.d("FacultyHomeScreen", "Navigating to main login screen")
//
//                        } else {
//                            Log.e("FacultyHomeScreen", "Navigation action is invalid or fragment is not attached")
//                        }
                    } catch (e: Exception) {
                        Log.e("FacultyHomeScreen", "Error during logout navigation", e)
                        Toast.makeText(requireContext(), "An error occurred during logout", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }


        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Example data
        sectionList = listOf(
            Section("CS41"),
            Section("CS42"),
            Section("CS43"),
            Section("CS44")
            // Add more sections as needed
        )

        sectionAdapter = SectionAdapter(sectionList) { section ->
            // Store the selected section and open the file picker
            selectedSectionName = section.sectionName
            openFilePicker()
        }

        binding.recyclerView.adapter = sectionAdapter
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val pdfUri = data?.data
            if (pdfUri != null && selectedSectionName != null) {
                // Upload the selected PDF for the selected section
                uploadTimetableForSection(selectedSectionName!!, pdfUri)
            } else {
                Toast.makeText(requireContext(), "Failed to select a file or section", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadTimetableForSection(sectionName: String, pdfUri: Uri) {
        val firestore = FirebaseFirestore.getInstance()
        val sectionDocRef = firestore.collection("sections").document(sectionName)
        val storageRef = FirebaseStorage.getInstance().reference.child("timetables/$sectionName.pdf")

        // Check if the section exists
        sectionDocRef.get().addOnSuccessListener { document ->
            if (document.exists()) {
                // Section exists; upload the timetable
                uploadFileToStorage(sectionName, pdfUri, storageRef, sectionDocRef)
            } else {
                // Section does not exist; create the section first
                val sectionData = mapOf(
                    "sectionName" to sectionName,
                    "students" to listOf<Map<String, String>>() // Initialize with an empty list of students
                )

                sectionDocRef.set(sectionData)
                    .addOnSuccessListener {
                        // Section created; upload the timetable
                        uploadFileToStorage(sectionName, pdfUri, storageRef, sectionDocRef)
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(
                            requireContext(),
                            "Failed to create section: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }.addOnFailureListener { e ->
            Toast.makeText(
                requireContext(),
                "Error checking section existence: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun uploadFileToStorage(
        sectionName: String,
        pdfUri: Uri,
        storageRef: StorageReference,
        sectionDocRef: DocumentReference
    ) {
        storageRef.putFile(pdfUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val timetableUrl = downloadUrl.toString()

                    // Update the timetable URL in Firestore
                    sectionDocRef.update("timetableUrl", timetableUrl)
                        .addOnSuccessListener {
                            Toast.makeText(
                                requireContext(),
                                "Timetable uploaded successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                requireContext(),
                                "Error updating timetable URL: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    requireContext(),
                    "Error uploading timetable: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    interface OnAdminLogoutListener {
        fun onAdminLogout()
    }
    companion object {
        const val PDF_REQUEST_CODE = 101
    }
}
