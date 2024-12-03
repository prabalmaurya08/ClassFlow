package com.example.classflow.faculty.facultyAttendance.facultyMarkAttendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch



class AttendanceMarkingViewModel(
    private val repository: AttendanceRepository
) : ViewModel() {

    private val _studentsLiveData = MutableLiveData<List<Student>>()
    val studentsLiveData: LiveData<List<Student>> get() = _studentsLiveData

    private val _errorLiveData = MutableLiveData<String>()
    val errorLiveData: LiveData<String> get() = _errorLiveData

    fun loadStudents(sectionName: String) {
        viewModelScope.launch {
            try {
                val students = repository.getStudentsForSection(sectionName)
                _studentsLiveData.value = students
            } catch (e: Exception) {
                _errorLiveData.value = "Error loading students: ${e.message}"
            }
        }
    }

    fun saveAttendance(
        sectionName: String,
        subjectName: String,
        date: String,
        facultyId: String,
        students: List<Student>
    ) {
        viewModelScope.launch {
            try {
                val attendanceMap = students.associate { it.rollNo to it.attendanceStatus }
                repository.saveAttendance(sectionName, subjectName, date, facultyId, attendanceMap)
            } catch (e: Exception) {
                _errorLiveData.value = "Error saving attendance: ${e.message}"
            }
        }
    }
}
