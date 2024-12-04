package com.example.classflow.faculty.facultyAssignment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.classflow.mvvm.AllottedClass
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FacultyAssignmentViewModel:ViewModel() {
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

                // Log the facultyId for debugging
                Log.d("FacultyAttendance", "Faculty ID: $facultyId")

                // Fetch allotted classes from facultyProfiles
                val facultyDoc = firestore.collection("facultyProfiles").document(facultyId).get().await()
                if (facultyDoc.exists()) {
                    val allottedClasses = facultyDoc.get("allottedClasses") as? List<Map<String, String>>
                    Log.d("FacultyAttendance", "Allotted Classes: $allottedClasses")

                    val classesList = allottedClasses?.map {
                        AllottedClass(section = it["section"] ?: "", subject = it["subject"] ?: "")
                    } ?: emptyList()

                    _allottedClasses.postValue(classesList)
                } else {
                    _errorMessage.postValue("Faculty profile not found for ID: $facultyId")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error fetching allotted classes: ${e.message}")
                Log.e("FacultyAttendance", "Error fetching data", e)
            }
        }
    }
}