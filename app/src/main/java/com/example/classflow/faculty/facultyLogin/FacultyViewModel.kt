package com.example.classflow.faculty.facultyLogin

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.classflow.mvvm.Faculty
import com.example.classflow.mvvm.User
import com.example.classflow.mvvm.UserRepository
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class FacultyViewModel(application: Application) : AndroidViewModel(application) {

    // Repository for user-related tasks (sign up, etc.)
    private val facultyRepository = UserRepository()

    // LiveData to observe the result of sign up
    val signupResult: LiveData<Boolean> = MutableLiveData()

    // Firestore instance for faculty data management
    private val firestore = FirebaseFirestore.getInstance()

    // LiveData for the list of faculties
    private val _facultyList = MutableLiveData<List<Faculty>>()
    val facultyList: LiveData<List<Faculty>> get() = _facultyList

    // LiveData for action status messages (success or failure)
    private val _actionStatus = MutableLiveData<String>()
    val actionStatus: LiveData<String> get() = _actionStatus

    // Fetch the list of faculty profiles from Firestore
    init {
        fetchFacultyProfiles()
    }

    // Sign up the faculty user
    fun signUpFaculty(facultyUser: User) {
        facultyRepository.signUpUser(facultyUser).observeForever {
            (signupResult as MutableLiveData).postValue(it)
        }
    }

    // Fetch faculty list from Firestore
    private fun fetchFacultyProfiles() {
        firestore.collection("facultyProfiles")
            .get()
            .addOnSuccessListener { documents ->
                val faculties = documents.map { document ->
                    document.toObject(Faculty::class.java)
                }
                _facultyList.postValue(faculties) // Update the faculty list
            }
            .addOnFailureListener { exception ->
                _actionStatus.postValue("Failed to fetch faculty profiles: ${exception.message}")
            }
    }

    // Upload timetable URL for a specific faculty
    fun updateTimetableUrl(facultyId: String, timetableUrl: String) {
        firestore.collection("facultyProfiles")
            .document(facultyId)
            .update("timetableUrl", timetableUrl)
            .addOnSuccessListener {
                _actionStatus.postValue("Timetable uploaded successfully.") // Update status
            }
            .addOnFailureListener { exception ->
                _actionStatus.postValue("Failed to upload timetable: ${exception.message}")
            }
    }

    fun allotClasses(facultyId: String, section: String, subject: String) {
        val classData = hashMapOf("section" to section, "subject" to subject)

        firestore.collection("facultyProfiles").document(facultyId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Fetch the allottedClasses list, ensuring it's a List type
                    val allottedClasses = document.get("allottedClasses") as? List<Map<String, String>> // Assuming it's a list of maps

                    // If allottedClasses exists, add the new classData to it
                    firestore.collection("facultyProfiles")
                        .document(facultyId)
                        .update("allottedClasses", FieldValue.arrayUnion(classData))
                        .addOnSuccessListener {
                            _actionStatus.postValue("Class allotted successfully.")
                            Log.d("FacultyViewModel", "Class allotted successfully for facultyId: $facultyId")
                        }
                        .addOnFailureListener { exception ->
                            _actionStatus.postValue("Failed to allot class: ${exception.message}")
                            Log.e("FacultyViewModel", "Error allotting class: ${exception.message}")
                        }
                } else {
                    // If the document doesn't exist, notify the user
                    _actionStatus.postValue("Faculty not found.")
                    Log.e("FacultyViewModel", "Faculty not found for facultyId: $facultyId")
                }
            }
            .addOnFailureListener { exception ->
                _actionStatus.postValue("Error fetching faculty data: ${exception.message}")
                Log.e("FacultyViewModel", "Error fetching faculty data: ${exception.message}")
            }
    }


}
