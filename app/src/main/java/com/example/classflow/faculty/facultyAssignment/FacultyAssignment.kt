package com.example.classflow.faculty.facultyAssignment

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
import com.example.classflow.databinding.FragmentFacultyAssigmentBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class FacultyAssignment : Fragment() {

    private var _binding: FragmentFacultyAssigmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FacultyAssignmentViewModel by viewModels()
    private lateinit var adapter: FacultyAssignmentAdapter

    private var listener: OnFaClassClickListener? = null

    interface OnFaClassClickListener {
        fun onFaClassClicked(section: String, subject: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnFaClassClickListener) {
            listener = parentFragment as OnFaClassClickListener
        } else {
            throw RuntimeException("Parent fragment must implement OnClassClickListener")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding=FragmentFacultyAssigmentBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        binding.FacultyAssignmentRecyclerview.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapter with an empty list
        adapter = FacultyAssignmentAdapter(emptyList()) { section, subject ->
            listener?.onFaClassClicked(section, subject)
        }
        binding.FacultyAssignmentRecyclerview.adapter = adapter

        // Observe allottedClasses LiveData for updates
        viewModel.allottedClasses.observe(viewLifecycleOwner) { classes ->
            Log.d("FacultyAssignment", "Fetched classes: $classes")
            if (classes.isNullOrEmpty()) {
                binding.FacultyAssignmentRecyclerview.visibility = View.GONE
               // binding.errorMessage.visibility = View.VISIBLE
            } else {
                binding.FacultyAssignmentRecyclerview.visibility = View.VISIBLE
              //  binding.errorMessage.visibility = View.GONE
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
                Log.d("FacultyAssignment", "Fetching data for user: ${firebaseUser.uid}")
                viewModel.fetchAllottedClasses(firebaseUser.uid)
            } else {
                Log.e("FacultyAssignment", "User is not logged in")
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}