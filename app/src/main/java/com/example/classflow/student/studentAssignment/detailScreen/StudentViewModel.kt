package com.example.classflow.student.studentAssignment.detailScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.classflow.faculty.facultyAssignment.assignmentDetails.Assignment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class StudentViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _assignments = MutableLiveData<List<Assignment>>()
    val assignments: LiveData<List<Assignment>> get() = _assignments

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchAssignments(subject: String) {
        viewModelScope.launch {
            try {
                val firebaseUid = auth.currentUser?.uid
                if (firebaseUid.isNullOrEmpty()) {
                    _errorMessage.postValue("User is not logged in.")
                    return@launch
                }

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

                // Fetch assignments based on section and subject
                val assignmentsRef = firestore.collection("assignments")
                    .document(sectionName)
                    .collection(subject)

                assignmentsRef.get().addOnSuccessListener { snapshot ->
                    if (!snapshot.isEmpty) {
                        val assignmentsList = snapshot.documents.mapNotNull { doc ->
                            val name = doc.getString("name") ?: return@mapNotNull null
                            val url = doc.getString("url") ?: return@mapNotNull null
                            val timestamp = doc.getLong("timestamp") ?: return@mapNotNull null
                            Assignment(name, url, timestamp.toString())
                        }
                        _assignments.postValue(assignmentsList)
                    } else {
                        _errorMessage.postValue("No assignments found.")
                    }
                }.addOnFailureListener { exception ->
                    _errorMessage.postValue("Error fetching assignments: ${exception.message}")
                }

            } catch (e: Exception) {
                _errorMessage.postValue("Error: ${e.message}")
            }
        }
    }
}