package com.example.classflow.student.studentAssignment.detailScreen

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.R
import com.example.classflow.databinding.FragmentStudentAssignmentDetailScreenBinding
import com.example.classflow.faculty.facultyAssignment.assignmentDetails.Assignment


class StudentAssignmentDetailScreen : Fragment() {
    private lateinit var binding: FragmentStudentAssignmentDetailScreenBinding
    private val args: StudentAssignmentDetailScreenArgs by navArgs()
    private lateinit var viewModel: StudentViewModel
    private lateinit var assignmentAdapter: StudentAssignmentDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStudentAssignmentDetailScreenBinding.inflate(layoutInflater)
        val subject = args.subject

        binding.StuAssignmentSubject.text = subject

       binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[StudentViewModel::class.java]

        // Initialize RecyclerView
        assignmentAdapter = StudentAssignmentDetailAdapter(emptyList()) { assignment ->
            downloadAssignment(assignment)
        }

        binding.AssignmentDetailRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.AssignmentDetailRecyclerView.adapter = assignmentAdapter

        // Observe assignments data
        viewModel.assignments.observe(viewLifecycleOwner, Observer { assignments ->
            assignmentAdapter.updateAssignments(assignments) // Update adapter with new data
        })

        // Observe error message
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        })

        // Fetch assignments using the subject from SafeArgs
        val subjectName = args.subject // Get the subject passed via SafeArgs
        viewModel.fetchAssignments(subjectName)
    }

    private fun downloadAssignment(assignment: Assignment) {
        val url = assignment.url
        if (url.isNotEmpty() && (url.startsWith("http://") || url.startsWith("https://"))) {
            try {
                // Prepare download request
                val request = DownloadManager.Request(Uri.parse(url)).apply {
                    setTitle(assignment.name)
                    setDescription("Downloading ${assignment.name}")
                    setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, assignment.name + ".pdf") // Adjust file extension if needed
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                }

                // Get system's DownloadManager service
                val downloadManager = requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                downloadManager.enqueue(request)

                // Inform the user that the download has started
                Toast.makeText(requireContext(), "Download started for ${assignment.name}", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                // Handle download errors
                Toast.makeText(requireContext(), "Download failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Handle invalid URL
            Toast.makeText(requireContext(), "Invalid URL for assignment", Toast.LENGTH_SHORT).show()
        }
    }
}
