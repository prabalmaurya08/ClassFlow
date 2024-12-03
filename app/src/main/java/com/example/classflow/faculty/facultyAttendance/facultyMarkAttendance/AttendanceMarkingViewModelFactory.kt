package com.example.classflow.faculty.facultyAttendance.facultyMarkAttendance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AttendanceMarkingViewModelFactory(
    private val repository: AttendanceRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AttendanceMarkingViewModel::class.java)) {
            return AttendanceMarkingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
