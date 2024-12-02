package com.example.classflow.faculty.facultyAttendance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.databinding.FragmentFacultyAttendanceMainScreenBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class FacultyAttendanceMainScreen : Fragment(), AllottedClassesAdapter.OnClassClickListener {

    private var _binding: FragmentFacultyAttendanceMainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FacultyAttendanceViewModel by viewModels()
    private lateinit var adapter: AllottedClassesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFacultyAttendanceMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val firebaseUser = FirebaseAuth.getInstance().currentUser

        // Initialize RecyclerView
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext()) // Set a LayoutManager
        }

        // Observe ViewModel LiveData
        viewModel.allottedClasses.observe(viewLifecycleOwner) { classes ->
            if (classes.isNotEmpty()) {
                adapter = AllottedClassesAdapter(classes, this@FacultyAttendanceMainScreen)
                binding.recyclerView.adapter = adapter
                binding.errorMessage.visibility = View.GONE
            } else {
                // Show error message if no data
                binding.errorMessage.apply {
                    text = "No classes allotted."
                    visibility = View.VISIBLE
                }
            }
        }

        // Fetch data
        lifecycleScope.launch {
            firebaseUser?.let {
                viewModel.fetchAllottedClasses(it.uid)
            } ?: run {
                binding.errorMessage.apply {
                    text = "User not logged in."
                    visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onClassClicked(section: String, subject: String) {
        // Handle card click (navigate with Safe Args or other logic)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
