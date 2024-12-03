package com.example.classflow.faculty.facultyhome

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.classflow.mvvm.AllottedClass
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class FacultyHomeViewModel(application: Application) : AndroidViewModel(application) {
    private val firestore = FirebaseFirestore.getInstance()
    private val firebaseAuth = FirebaseAuth.getInstance()

    private val _allottedClasses = MutableLiveData<List<AllottedClass>>()
    val allottedClasses: LiveData<List<AllottedClass>> get() = _allottedClasses

    fun fetchFacultyData() {
        viewModelScope.launch(Dispatchers.IO) {
            val context = getApplication<Application>().applicationContext

            try {
                val firebaseUid = firebaseAuth.currentUser?.uid
                if (firebaseUid.isNullOrEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "User not logged in", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                // Fetch facultyId
                val userDoc = firestore.collection("users").document(firebaseUid).get().await()
                if (!userDoc.exists()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "User profile not found", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                val facultyId = userDoc.getString("facultyId") ?: ""
                if (facultyId.isEmpty()) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Faculty ID not found in user profile", Toast.LENGTH_SHORT).show()
                    }
                    return@launch
                }

                fetchAllottedClasses(facultyId)
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private suspend fun fetchAllottedClasses(facultyId: String) {
        try {
            val facultyDoc = firestore.collection("facultyProfiles").document(facultyId).get().await()
            if (facultyDoc.exists()) {
                val allottedClassesData = facultyDoc["allottedClasses"] as? List<Map<String, String>> ?: emptyList()
                val classes = allottedClassesData.map {
                    AllottedClass(
                        subject = it["subjectName"] ?: "",
                        section = it["section"] ?: ""
                    )
                }
                _allottedClasses.postValue(classes)
            } else {
                _allottedClasses.postValue(emptyList())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _allottedClasses.postValue(emptyList())
        }
    }
}