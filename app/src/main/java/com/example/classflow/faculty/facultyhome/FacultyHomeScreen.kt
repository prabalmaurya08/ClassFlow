package com.example.classflow.faculty.facultyhome

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.R
import com.example.classflow.databinding.FragmentFacultyhomescreenBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch


class FacultyHomeScreen : Fragment() {
    private lateinit var binding: FragmentFacultyhomescreenBinding
  //  private  val viewModel: FacultyHomeViewModel by viewModel()
    private val viewModel: FacultyHomeViewModel by viewModels()
    private lateinit var adapter: AllottedClassAdapter

    // Firebase Authentication instance
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFacultyhomescreenBinding.inflate(inflater, container, false)



        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    // Call the logout function
                    auth.signOut()
                    findNavController().navigate(R.id.action_facultyhomescreen_to_mainLogin)

                    true
                }

                else -> false
            }
        }

        // Initialize RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize adapter with an empty list
        adapter = AllottedClassAdapter(emptyList())
        binding.recyclerView.adapter = adapter

        // Observe allottedClasses LiveData for updates
        viewModel.allottedClasses.observe(viewLifecycleOwner) { classes ->
            Log.d("FacultyHomePage", "Fetched classes: $classes")
            if (classes.isNullOrEmpty()) {
                binding.recyclerView.visibility = View.GONE
                // binding.errorMessage.visibility = View.VISIBLE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                //  binding.errorMessage.visibility = View.GONE
                adapter.updateData(classes)
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
                Log.d("FacultyHomePage", "Fetching data for user: ${firebaseUser.uid}")
                viewModel.fetchAllottedClasses(firebaseUser.uid)
            } else {
                Log.e("FacultyHomePage", "User is not logged in")
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }



}