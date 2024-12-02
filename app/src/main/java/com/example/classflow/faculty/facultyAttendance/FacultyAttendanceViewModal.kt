package com.example.classflow.faculty.facultyAttendance

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.classflow.mvvm.AllottedClass
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FacultyAttendanceViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val _allottedClasses = MutableLiveData<List<AllottedClass>>()
    val allottedClasses: LiveData<List<AllottedClass>> = _allottedClasses

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    suspend fun fetchAllottedClasses(firebaseUid: String) {
        withContext(Dispatchers.IO) {
            try {
                // Fetch facultyId from users collection
                val userDoc = firestore.collection("users").document(firebaseUid).get().await()
                if (!userDoc.exists()) {
                    _errorMessage.postValue("User profile not found.")
                    return@withContext
                }

                val facultyId = userDoc.getString("facultyId") ?: ""
                if (facultyId.isEmpty()) {
                    _errorMessage.postValue("Faculty ID not found in user profile.")
                    return@withContext
                }

                // Fetch allotted classes from facultyProfiles
                val facultyDoc = firestore.collection("facultyProfiles").document(facultyId).get().await()
                if (facultyDoc.exists()) {
                    val allottedClasses = facultyDoc.get("allottedClasses") as? List<Map<String, String>>
                    val classesList = allottedClasses?.map {
                        AllottedClass(section = it["section"] ?: "", subject = it["subject"] ?: "")
                    } ?: emptyList()

                    _allottedClasses.postValue(classesList)
                } else {
                    _errorMessage.postValue("Faculty profile not found for ID: $facultyId")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching allotted classes: ${e.message}")
            }
        }
    }
}
