package com.example.classflow.faculty.facultyAssignment.assignmentDetails

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.databinding.FragmentFacultyAssignmentDetailBinding

class FacultyAssignmentDetail : Fragment() {

    private lateinit var binding: FragmentFacultyAssignmentDetailBinding
    private val viewModel: FacultyAssignmentDetailViewModel by viewModels()
    private val args: FacultyAssignmentDetailArgs by navArgs()
    private var selectedFileUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFacultyAssignmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
        val section = args.section
        val subject = args.subject

        Log.d("FacultyAssignmentDetail", "Section: $section, Subject: $subject")

        binding.FacultyAssignSubject.text = subject
        binding.FacultyAssignClass.text = section

        val adapter = AssignmentAdapter(mutableListOf()) { assignment ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(assignment.url))
            startActivity(intent)
        }
        binding.assignmentRecyclerView.adapter = adapter
        binding.assignmentRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe the assignments list
        viewModel.assignmentsLiveData.observe(viewLifecycleOwner) { assignments ->
            Log.d("FacultyAssignmentDetail", "Assignments received: $assignments")
            adapter.updateAssignments(assignments)
        }

        // Observe the upload status
        viewModel.uploadStatusLiveData.observe(viewLifecycleOwner) { status ->
            Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
        }

        // Fetch assignments
        viewModel.fetchAssignments(section, subject)

        // Handle file selection
        binding.chooseFileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                type = "*/*"
            }
            startActivityForResult(intent, 100)
        }

        // Handle file upload
        binding.uploadButton.setOnClickListener {
            val assignmentName = binding.assignmentNameInput.text.toString()
            if (assignmentName.isBlank() || selectedFileUri == null) {
                Toast.makeText(requireContext(), "Fill all details", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            Log.d("FacultyAssignmentDetail", "Uploading assignment: $assignmentName, File: $selectedFileUri")
            viewModel.uploadAssignment(section, subject, assignmentName, selectedFileUri!!)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedFileUri = data?.data
            binding.filePathText.text = selectedFileUri?.lastPathSegment ?: "No file selected"
            Log.d("FacultyAssignmentDetail", "File selected: $selectedFileUri")
        }
    }
}
