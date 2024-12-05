package com.example.classflow.student.studentAttendance

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StudentAttendanceViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    // LiveData for subject names
    private val _subjectNames = MutableLiveData<List<String>>()
    val subjectNames: LiveData<List<String>> get() = _subjectNames

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // Fetch the subjects for a student
    fun fetchSubjectsForStudent(firebaseUid: String) {
        viewModelScope.launch {
            try {
                Log.d("StudentAttendance", "Fetching data for UID: $firebaseUid")

                // Fetch studentRollNo from the 'users' collection
                val userDoc = firestore.collection("users").document(firebaseUid).get().await()
                if (!userDoc.exists()) {
                    _errorMessage.postValue("User profile not found.")
                    return@launch
                }

                val studentRollNo = userDoc.getString("studentRollNo") ?: ""
                if (studentRollNo.isEmpty()) {
                    _errorMessage.postValue("Student Roll Number not found in user profile.")
                    return@launch
                }

                // Fetch section name from the 'studentProfiles' collection
                val studentProfileDoc = firestore.collection("studentProfiles").document(studentRollNo).get().await()
                if (!studentProfileDoc.exists()) {
                    _errorMessage.postValue("Student profile not found.")
                    return@launch
                }

                val sectionName = studentProfileDoc.getString("studentSection") ?: ""
                if (sectionName.isEmpty()) {
                    _errorMessage.postValue("Student section not found in profile.")
                    return@launch
                }

                // Fetch subjects directly by checking the subcollections under the sectionName document in 'attendanceRecords'
                val sectionRef = firestore.collection("attendanceRecords").document(sectionName)
                val subjectSnapshot = sectionRef.collection("subjects").get().await()

                // Check if subjects are available under the section document
                if (!subjectSnapshot.isEmpty) {
                    val subjectNamesList = mutableListOf<String>()
                    subjectSnapshot.forEach { document ->
                        val subjectName = document.id // Document ID is used as subject name
                        subjectNamesList.add(subjectName)
                        Log.d("StudentAttendance", "Subject: $subjectName")
                    }

                    // Post the subject names to LiveData
                    _subjectNames.postValue(subjectNamesList)
                    Log.d("StudentAttendance", "Updated subject names: $subjectNamesList")
                } else {
                    _errorMessage.postValue("No subjects found for this student in the section.")
                    Log.d("StudentAttendance", "No subjects found for section: $sectionName")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching subjects: ${e.message}")
                Log.e("StudentAttendance", "Error: ${e.message}")
            }
        }
    }

}
