package com.example.classflow.student.studentAssignment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.R

class SubjectAdapter(
    private var subjects: List<String>,
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    class SubjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val subjectName: TextView = view.findViewById(R.id.StuAssignmentSubject)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.student_assignment_card, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = subjects[position]
        holder.subjectName.text = subject

        holder.itemView.setOnClickListener {
            onItemClicked(subject)
        }
    }

    override fun getItemCount() = subjects.size

    fun submitList(newSubjects: List<String>) {
        subjects = newSubjects
        notifyDataSetChanged()
    }
}
