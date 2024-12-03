package com.example.classflow.faculty.facultyhome

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.databinding.FragmentFacultyhomescreenBinding


class FacultyHomeScreen : Fragment() {
    private lateinit var binding: FragmentFacultyhomescreenBinding
    private lateinit var viewModel: FacultyHomeViewModel
    private lateinit var adapter: AllottedClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFacultyhomescreenBinding.inflate(inflater, container, false)

        setupViewModel()
        setupRecyclerView()
        observeData()

        return binding.root
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        )[FacultyHomeViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = AllottedClassAdapter(emptyList()) // Initialize with an empty list
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun observeData() {
        viewModel.allottedClasses.observe(viewLifecycleOwner) { classList ->

            Log.d("FacultyHome", "Fetched Classes: $classList")

            // Update the adapter data
            adapter = AllottedClassAdapter(classList)
            binding.recyclerView.adapter = adapter
            adapter.notifyDataSetChanged() // Notify adapter of data changes
        }

        // Fetch faculty data (includes fetching facultyId and classes)
        viewModel.fetchFacultyData()
    }
}