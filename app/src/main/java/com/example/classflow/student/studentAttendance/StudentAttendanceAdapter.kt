package com.example.classflow.student.studentAttendance

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.databinding.StudentSubjectnameCardBinding // Make sure to import your generated binding class

class StudentAttendanceAdapter(
    private val onItemClicked: (String) -> Unit
) : ListAdapter<String, StudentAttendanceAdapter.SubjectViewHolder>(SubjectDiffCallback()) {

    // ViewHolder with ViewBinding
    class SubjectViewHolder(private val binding: StudentSubjectnameCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subject: String, onItemClicked: (String) -> Unit) {
            binding.sectionName.text = subject
            binding.root.setOnClickListener {
                onItemClicked(subject)
            }
        }
    }

    // Create ViewHolder using ViewBinding
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val binding = StudentSubjectnameCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubjectViewHolder(binding)
    }



    // Bind data to ViewHolder
    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = getItem(position) // This will get the item from the current list
        Log.d("StudentAttendance", "Binding subject: $subject")
        holder.bind(subject, onItemClicked)
    }

    // DiffUtil Callback for comparing old and new list
    class SubjectDiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            // Assuming subject names are unique. If not, use a more appropriate check.
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            // Check if content is the same (e.g., subject name)
            return oldItem == newItem
        }
    }
}
