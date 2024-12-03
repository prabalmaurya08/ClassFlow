package com.example.classflow.faculty.facultyhome

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.databinding.FacultyClassAndSubjectCardBinding

import com.example.classflow.mvvm.AllottedClass

class AllottedClassAdapter(private val classes: List<AllottedClass>) :
    RecyclerView.Adapter<AllottedClassAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: FacultyClassAndSubjectCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AllottedClass) {
            binding.subjectName.text = item.subject
            binding.sectionName.text = item.section
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            FacultyClassAndSubjectCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(classes[position])
    }

    override fun getItemCount(): Int = classes.size
}