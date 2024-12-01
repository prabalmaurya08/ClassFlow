package com.example.classflow.admin.adminFaculty

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.databinding.AdminFacutlyTimetableAndClassallotmentCardBinding
import com.example.classflow.mvvm.Faculty

class AdminFacultyListAdapter(
    private val onItemClick: (Faculty) -> Unit
) : ListAdapter<Faculty, AdminFacultyListAdapter.FacultyViewHolder>(FacultyDiffCallback()) {

    inner class FacultyViewHolder(val binding: AdminFacutlyTimetableAndClassallotmentCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(faculty: Faculty) {
            binding.facultyName.text = faculty.name
            binding.facultyId.text = faculty.facultyId
            binding.root.setOnClickListener {
                onItemClick(faculty)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FacultyViewHolder {
        val binding = AdminFacutlyTimetableAndClassallotmentCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FacultyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FacultyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FacultyDiffCallback : DiffUtil.ItemCallback<Faculty>() {
        override fun areItemsTheSame(oldItem: Faculty, newItem: Faculty): Boolean {
            return oldItem.facultyId == newItem.facultyId
        }

        override fun areContentsTheSame(oldItem: Faculty, newItem: Faculty): Boolean {
            return oldItem == newItem
        }
    }
}
