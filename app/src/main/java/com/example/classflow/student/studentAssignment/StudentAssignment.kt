package com.example.classflow.student.studentAssignment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.R
import com.example.classflow.databinding.FragmentStudentAssignmentBinding
import kotlinx.coroutines.launch

class StudentAssignment : Fragment() {

    private var _binding: FragmentStudentAssignmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SubjectViewModel by viewModels()
    private lateinit var adapter: SubjectAdapter


    private var listener: OnSubjectClickListener? = null

    // Attach listener to the parent fragment
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnSubjectClickListener) {
            listener = parentFragment as OnSubjectClickListener
        } else {
            throw RuntimeException("Parent fragment must implement OnSubjectClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentAssignmentBinding.inflate(inflater, container, false)

        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up RecyclerView
        binding.studentAssignmentRecyclerview.layoutManager = LinearLayoutManager(requireContext())
        adapter = SubjectAdapter(emptyList()) { subject ->
            // Handle subject click
            Log.d("StudentAssignment", "Clicked subject: $subject")
            listener?.onSubjectClicked(subject)
        }
        binding.studentAssignmentRecyclerview.adapter = adapter

        // Observe subjects LiveData
        viewModel.subjects.observe(viewLifecycleOwner) { subjects ->
            if (subjects.isNullOrEmpty()) {
                Toast.makeText(requireContext(), "No subjects found", Toast.LENGTH_SHORT).show()
            } else {
                adapter.submitList(subjects)
            }
        }

        // Observe errors
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        }

        // Fetch subjects
        viewModel.fetchSubjectsForStudent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface OnSubjectClickListener {
        fun onSubjectClicked(subject: String)
    }


}


