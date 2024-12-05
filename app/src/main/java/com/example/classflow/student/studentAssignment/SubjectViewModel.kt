package com.example.classflow.student.studentAssignment



import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.launch


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class SubjectViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val _subjects = MutableLiveData<List<String>?>()
    val subjects: LiveData<List<String>?> get() = _subjects

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchSubjectsForStudent() {
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

                // Log the sectionName to debug
                Log.d("SubjectViewModel", "Section Name: $sectionName")

                // Fetch subjects from the 'assignments' collection
                val sectionDoc = firestore.collection("assignments").document(sectionName).get().await()

                // Check if the section document exists
                if (sectionDoc.exists()) {
                    val subjectList = sectionDoc.get("subjects") as? List<String>
                    if (subjectList.isNullOrEmpty()) {
                        _errorMessage.postValue("No subjects found.")
                    } else {
                        _subjects.postValue(subjectList)
                    }
                } else {
                    _errorMessage.postValue("Assignment Not Uploaded Yet.......")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching subjects: ${e.message}")
            }
        }
    }
}


