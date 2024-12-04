package com.example.classflow.student.studentAssignment.detailScreen

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.databinding.AssignmentListBinding

import com.example.classflow.faculty.facultyAssignment.assignmentDetails.Assignment
import java.text.DateFormat
import java.util.*

class StudentAssignmentDetailAdapter(
    private var assignments: List<Assignment>,
    private val onDownloadClick: (Assignment) -> Unit // Pass the full Assignment object instead of just URL
) : RecyclerView.Adapter<StudentAssignmentDetailAdapter.AssignmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val binding = AssignmentListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AssignmentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        val assignment = assignments[position]
        holder.bind(assignment)
    }

    override fun getItemCount(): Int = assignments.size

    // Method to update assignments list dynamically
    fun updateAssignments(newAssignments: List<Assignment>) {
        assignments = newAssignments
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    inner class AssignmentViewHolder(private val binding: AssignmentListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(assignment: Assignment) {
            binding.studentAssignmentName.text = assignment.url
            binding.timestampTextView.text = DateFormat.getDateInstance().format(Date(assignment.timestamp))

            // Set click listener to handle download action
            binding.studentDownloadButton.setOnClickListener {
                onDownloadClick(assignment) // Pass the full assignment object
            }
        }
    }
}
