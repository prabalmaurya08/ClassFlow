package com.example.classflow.student.studentAttendance

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.classflow.R
import com.example.classflow.databinding.FragmentStudentAttendanceBinding
import com.example.classflow.student.studentAssignment.StudentAssignment
import com.google.firebase.auth.FirebaseAuth


class StudentAttendance : Fragment() {
    private lateinit var binding:FragmentStudentAttendanceBinding

    private val viewModel: StudentAttendanceViewModel by activityViewModels()
    private lateinit var adapter: StudentAttendanceAdapter

    private var listener: OnSectionClickListener? = null

    // Attach listener to the parent fragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is StudentAssignment.OnSubjectClickListener) {
            listener = parentFragment as OnSectionClickListener
        } else {
            throw RuntimeException("Parent fragment must implement OnSubjectClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentStudentAttendanceBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        adapter = StudentAttendanceAdapter() { subject ->
            // Handle click on subject (item) here
            listener?.onSectionClick(subject)
        }
        binding.recyclerView.adapter = adapter

        // Observe subject names LiveData from the ViewModel
        viewModel.subjectNames.observe(viewLifecycleOwner) { subjects ->
            if (subjects.isEmpty()) {
                Toast.makeText(context, "No subjects found.", Toast.LENGTH_SHORT).show()
            } else {
                Log.d("StudentAttendance", "Subject: $subjects")
                adapter.submitList(subjects)
            }
        }

        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        // Get the student UID and fetch subjects
        val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid
        if (!firebaseUid.isNullOrEmpty()) {
            viewModel.fetchSubjectsForStudent(firebaseUid)
        }
    }
    interface OnSectionClickListener {
        fun onSectionClick(subjectName: String)
    }


}