package com.example.classflow.admin.adminFaculty

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.classflow.R
import com.example.classflow.databinding.FragmentAdminFacultyDetailScreenBinding
import com.example.classflow.faculty.facultyLogin.FacultyViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.storage.FirebaseStorage

class AdminFacultyDetailScreen : Fragment() {
    private lateinit var binding: FragmentAdminFacultyDetailScreenBinding

    private lateinit var viewModel: FacultyViewModel
    private lateinit var facultyId: String
    private lateinit var facultyName: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentAdminFacultyDetailScreenBinding.inflate(layoutInflater)

        facultyId = arguments?.getString("facultyId") ?: ""
        facultyName = arguments?.getString("facultyName") ?: ""
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[FacultyViewModel::class.java]

        setupUI()

        return binding.root
    }
    private fun setupUI() {
        binding.facultyName.text = facultyName
        binding.facultyId.text = facultyId

        setupSpinners()

        binding.uploadTimetableButton.setOnClickListener {
            openFilePicker()
        }

        binding.allotClassesButton.setOnClickListener {
            allotClass()
        }
    }

    private fun setupSpinners() {
        // Example data
        val sections = listOf("Select Section","CS41", "CS42", "CS43","CS44")//add select section
        val subjects = listOf("Select Subject","Math", "Science", "English")//add select subject

        val sectionAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            sections
        )
        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.sectionSpinner.adapter = sectionAdapter

        val subjectAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            subjects
        )
        subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.subjectSpinner.adapter = subjectAdapter
    }

    private fun allotClass() {
        val selectedSection = binding.sectionSpinner.selectedItem.toString()
        val selectedSubject = binding.subjectSpinner.selectedItem.toString()

        //shristi

        if (selectedSection == "Select Section" || selectedSubject == "Select Subject") {
            Snackbar.make(binding.root, "Please select valid Section and Subject", Snackbar.LENGTH_SHORT).show()
            return
        }

//end

        viewModel.allotClasses(facultyId, selectedSection, selectedSubject)
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/pdf"
        }
        startActivityForResult(intent, PDF_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val pdfUri = data?.data
            if (pdfUri != null) {
                uploadTimetable(pdfUri)
            }
        }
    }

    private fun uploadTimetable(pdfUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference
            .child("facultyTimetables/$facultyId.pdf")

        storageRef.putFile(pdfUri)
            .addOnSuccessListener {
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    viewModel.updateTimetableUrl(facultyId, downloadUrl.toString())
                }
            }
            .addOnFailureListener {
                Snackbar.make(binding.root, "Failed to upload timetable", Snackbar.LENGTH_SHORT).show()
            }
    }

    companion object {
        const val PDF_REQUEST_CODE = 101
    }

}