package com.example.classflow.faculty.facultyAttendance

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.example.classflow.databinding.FacultyClassAndSubjectCardBinding
import com.example.classflow.mvvm.AllottedClass

class AllottedClassesAdapter(
    private var classes: List<AllottedClass>,
    private val onItemClick: (String, String) -> Unit
) : RecyclerView.Adapter<AllottedClassesAdapter.ClassViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FacultyClassAndSubjectCardBinding.inflate(inflater, parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val allottedClass = classes[position]
        holder.bind(allottedClass)
    }

    override fun getItemCount(): Int = classes.size

    fun submitList(newClasses: List<AllottedClass>) {
        classes = newClasses
        notifyDataSetChanged()
    }

    inner class ClassViewHolder(private val binding: FacultyClassAndSubjectCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(allottedClass: AllottedClass) {
            binding.sectionName.text = allottedClass.section
            binding.subjectName.text = allottedClass.subject
            binding.root.setOnClickListener {
                onItemClick(allottedClass.section, allottedClass.subject)
            }
        }
    }
}
