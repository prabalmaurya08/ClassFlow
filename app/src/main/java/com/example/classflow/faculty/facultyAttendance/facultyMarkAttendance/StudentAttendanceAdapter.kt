package com.example.classflow.faculty.facultyAttendance.facultyMarkAttendance

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.R
import com.example.classflow.databinding.FacultyAttendanceStudentlistCardBinding

class StudentAttendanceAdapter : RecyclerView.Adapter<StudentAttendanceAdapter.ViewHolder>() {

    private val students = mutableListOf<Student>()

    fun submitList(studentList: List<Student>) {
        students.clear()
        students.addAll(studentList)
        notifyDataSetChanged()
    }

    fun getStudents(): List<Student> = students

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = FacultyAttendanceStudentlistCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(students[position])
    }

    override fun getItemCount(): Int = students.size

    inner class ViewHolder(private val binding: FacultyAttendanceStudentlistCardBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Student) {
            binding.StudentName.text = student.name
            binding.StudentRollno.text = student.rollNo
            binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
                student.attendanceStatus = if (checkedId == R.id.rbPresent) "present" else "absent"
            }
        }
    }
}
