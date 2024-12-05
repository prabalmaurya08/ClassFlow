
package com.example.classflow.admin.adminFaculty
import android.content.Context
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer

import androidx.lifecycle.ViewModelProvider

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.admin.adminFaculty.AdminFacultyListAdapter


import com.example.classflow.databinding.FragmentFacultyUploadTimeTableAndClassAllotBinding
import com.example.classflow.faculty.facultyLogin.FacultyViewModel





class FacultyUploadTimeTableAndClassAllot : Fragment() {

    private var _binding: FragmentFacultyUploadTimeTableAndClassAllotBinding?=null

    private val binding get() = _binding!!
    private lateinit var viewModel: FacultyViewModel
    private lateinit var facultyAdapter: AdminFacultyListAdapter

    private var listener: OnFacultyClickListener? = null

    interface OnFacultyClickListener {
        fun onFacultyClicked(facultyId: String, facultyName: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (parentFragment is OnFacultyClickListener) {
            listener = parentFragment as OnFacultyClickListener
        } else {
            throw RuntimeException("Parent fragment must implement OnFacultyClickListener")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding =  FragmentFacultyUploadTimeTableAndClassAllotBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[FacultyViewModel::class.java]

        setupRecyclerView()
        observeFacultyList()
        return binding.root
    }

    private fun setupRecyclerView() {
        facultyAdapter = AdminFacultyListAdapter {

                faculty ->
            // Trigger listener when faculty is clicked
            listener?.onFacultyClicked(faculty.facultyId, faculty.name)


        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = facultyAdapter
    }



    private fun observeFacultyList() {
        viewModel.facultyList.observe(viewLifecycleOwner, Observer { facultyList ->

            if(facultyList.isEmpty()){
                binding.noFacultyFound.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }else{
                binding.noFacultyFound.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
            }
            facultyAdapter.submitList(facultyList)
        })
        // Observe action status for messages
        viewModel.actionStatus.observe(viewLifecycleOwner, Observer { statusMessage ->
            Toast.makeText(requireContext(), statusMessage, Toast.LENGTH_SHORT).show()
        })
    }
}