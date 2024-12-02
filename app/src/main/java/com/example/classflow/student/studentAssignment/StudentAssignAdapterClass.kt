package com.example.classflow.student.studentAssignment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.databinding.StudentAssignmentCardBinding

class StudentAssignAdapterClass(
    private val datalist: List<StudentAssignmentDataClass>
) : RecyclerView.Adapter<StudentAssignAdapterClass.ViewHolder>() {

    // ViewHolder Class
    class ViewHolder(private val binding: StudentAssignmentCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: StudentAssignmentDataClass) {
            // Binding data to the views
            binding.StuAssignmentSubject.text = data.stuAssignSub
            binding.StuAssignSubjectTeacher.text = data.stuAssignSubTeacher

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each item
        val binding = StudentAssignmentCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }
}
