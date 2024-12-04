package com.example.classflow.faculty.facultyAssignment

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.databinding.FacultyAssignmentCardBinding

import com.example.classflow.mvvm.AllottedClass

class FacultyAssignmentAdapter (  private var classes: List<AllottedClass>,
private val onItemClick: (String, String) -> Unit
) : RecyclerView.Adapter<FacultyAssignmentAdapter.ClassViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = FacultyAssignmentCardBinding.inflate(inflater, parent, false)
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val allottedClass = classes[position]
        holder.bind(allottedClass)
    }

    override fun getItemCount(): Int = classes.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newClasses: List<AllottedClass>) {
        classes = newClasses
        notifyDataSetChanged()
    }

    inner class ClassViewHolder(private val binding: FacultyAssignmentCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(allottedClass: AllottedClass) {
            binding.FacultyAssignedClass.text = allottedClass.section
            binding.facultyAssignSubject.text = allottedClass.subject
            binding.root.setOnClickListener {
                onItemClick(allottedClass.section, allottedClass.subject)
            }
        }
    }
}

