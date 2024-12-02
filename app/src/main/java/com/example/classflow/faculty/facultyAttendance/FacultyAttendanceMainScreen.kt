package com.example.classflow.faculty.facultyAttendance

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.databinding.FragmentFacultyAttendanceMainScreenBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class FacultyAttendanceMainScreen : Fragment() {

    private var _binding: FragmentFacultyAttendanceMainScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FacultyAttendanceViewModel by viewModels()
    private lateinit var adapter: AllottedClassesAdapter

    private var listener: OnClassClickListener? = null

    interface OnClassClickListener {
        fun onClassClicked(section: String, subject: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnClassClickListener) {
            listener = parentFragment as OnClassClickListener
        } else {
            throw RuntimeException("Parent fragment must implement OnClassClickListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFacultyAttendanceMainScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapter with an empty list
        adapter = AllottedClassesAdapter(emptyList()) { section, subject ->
            listener?.onClassClicked(section, subject)
        }
        binding.recyclerView.adapter = adapter

        // Observe allottedClasses LiveData for updates
        viewModel.allottedClasses.observe(viewLifecycleOwner) { classes ->
            Log.d("FacultyAttendance", "Fetched classes: $classes")
            if (classes.isNullOrEmpty()) {
                binding.recyclerView.visibility = View.GONE
                binding.errorMessage.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.errorMessage.visibility = View.GONE
                adapter.submitList(classes)
            }
        }

        // Observe errorMessage LiveData for errors
        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_LONG).show()
            }
        }

        // Fetch data using lifecycleScope
        lifecycleScope.launch {
            val firebaseUser = FirebaseAuth.getInstance().currentUser
            if (firebaseUser != null) {
                Log.d("FacultyAttendance", "Fetching data for user: ${firebaseUser.uid}")
                viewModel.fetchAllottedClasses(firebaseUser.uid)
            } else {
                Log.e("FacultyAttendance", "User is not logged in")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
