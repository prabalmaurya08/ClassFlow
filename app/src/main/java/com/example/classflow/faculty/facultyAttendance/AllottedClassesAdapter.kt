package com.example.classflow.faculty.facultyAttendance

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.databinding.FacultyClassAndSubjectCardBinding

import com.example.classflow.mvvm.AllottedClass

class AllottedClassesAdapter(
    private val classes: List<AllottedClass>,
    private val listener: OnClassClickListener
) : RecyclerView.Adapter<AllottedClassesAdapter.ClassViewHolder>() {

    interface OnClassClickListener {
        fun onClassClicked(section: String, subject: String)
    }

    inner class ClassViewHolder(private val binding: FacultyClassAndSubjectCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(allottedClass: AllottedClass) {
            binding.sectionName.text = allottedClass.section
            binding.subjectName.text = allottedClass.subject

            // Set click listener for each card
            binding.root.setOnClickListener {
                listener.onClassClicked(allottedClass.section, allottedClass.subject)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding =FacultyClassAndSubjectCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(classes[position])
    }

    override fun getItemCount(): Int = classes.size
}
