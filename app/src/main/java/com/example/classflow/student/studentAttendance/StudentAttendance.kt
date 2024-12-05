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
        Log.d("StudentAttendance", "onAttach called")
        if (parentFragment is OnSectionClickListener) {
            listener = parentFragment as OnSectionClickListener
        } else {
            throw RuntimeException("Parent fragment must implement OnSubjectClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("StudentAttendance", "onCreateView called")
        binding = FragmentStudentAttendanceBinding.inflate(layoutInflater)
        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Log.d("StudentAttendance", "onViewCreated called")
//
//        // Set up RecyclerView
//        adapter = StudentAttendanceAdapter { subject ->
//            Log.d("StudentAttendance", "Subject clicked: $subject")
//            listener?.onSectionClick(subject)
//        }
//
//
//
//
//        binding.recyclerView.adapter = adapter
//
//        Log.d("StudentAttendance", "RecyclerView visibility: ${binding.recyclerView.visibility}")
//
//        // Observe subject names LiveData from the ViewModel
//        viewModel.subjectNames.observe(viewLifecycleOwner) { subjects ->
//            Log.d("StudentAttendance", "Subjects observed: $subjects")
//            if (subjects.isEmpty()) {
//                Toast.makeText(context, "No subjects found.", Toast.LENGTH_SHORT).show()
//                binding.error.visibility = View.VISIBLE
//            } else {
//                adapter.submitList(subjects)
//                Log.d("StudentAttendance", "Adapter list size in main screen: ${adapter.itemCount}")
//
//            }
//        }
//
//        // Observe error messages
//        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
//            Log.e("StudentAttendance", "Error observed: $errorMessage")
//            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
//        }
//
//        // Get the student UID and fetch subjects
//        val firebaseUid = FirebaseAuth.getInstance().currentUser?.uid
//        if (!firebaseUid.isNullOrEmpty()) {
//            Log.d("StudentAttendance", "Fetching subjects for student UID: $firebaseUid")
//            viewModel.fetchSubjectsForStudent(firebaseUid)
//        } else {
//            Log.e("StudentAttendance", "Firebase UID is null or empty")
//        }
//    }

    interface OnSectionClickListener {
        fun onSectionClick(subjectName: String)
    }


}