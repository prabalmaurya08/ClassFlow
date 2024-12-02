package com.example.classflow.student.studentAssignment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.R
import com.example.classflow.databinding.FragmentStudentAssignmentBinding

class StudentAssignment : Fragment() {
    private lateinit var binding: FragmentStudentAssignmentBinding
    private lateinit var adapterClass: StudentAssignAdapterClass

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStudentAssignmentBinding.inflate(layoutInflater)
        setupCard()
        return binding.root
    }

    private fun setupCard() {
        val cardItem = listOf(
            StudentAssignmentDataClass(stuAssignSub ="Automata Assignment", stuAssignDate = "Assigned On: 1 December 2024", stuAssignDeadline = "Submit By: 5 December 2024"),
            StudentAssignmentDataClass(stuAssignSub ="Automata Assignment", stuAssignDate = "Assigned On: 1 December 2024", stuAssignDeadline = "Submit By: 5 December 2024"),
            StudentAssignmentDataClass(stuAssignSub ="Automata Assignment", stuAssignDate = "Assigned On: 1 December 2024", stuAssignDeadline = "Submit By: 5 December 2024"),
            StudentAssignmentDataClass(stuAssignSub ="Automata Assignment", stuAssignDate = "Assigned On: 1 December 2024", stuAssignDeadline = "Submit By: 5 December 2024"),
            StudentAssignmentDataClass(stuAssignSub ="Automata Assignment", stuAssignDate = "Assigned On: 1 December 2024", stuAssignDeadline = "Submit By: 5 December 2024"),


            )
        adapterClass = StudentAssignAdapterClass(cardItem)
        binding.StudentAssignmentRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterClass
        }
    }
}


