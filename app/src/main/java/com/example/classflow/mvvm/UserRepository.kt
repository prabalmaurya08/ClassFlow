package com.example.classflow.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun signUpUser(user: User): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""

                    // Validate that the FacultyId is unique if the role is 'faculty'
                    if (user.role == "faculty") {
                        validateFacultyId(user.facultyId) { isValid ->
                            if (isValid) {
                                val userData = hashMapOf(
                                    "email" to user.email,
                                    "name" to user.name,
                                    "role" to user.role,
                                    "facultyId" to user.facultyId // Save FacultyId
                                )

                                // Save user data in the 'users' collection
                                firestore.collection("users").document(userId).set(userData)
                                    .addOnSuccessListener {
                                        saveFacultyProfile(user.facultyId, user)
                                        result.postValue(true)
                                    }
                                    .addOnFailureListener {
                                        result.postValue(false)
                                    }
                            } else {
                                Log.e("UserRepository", "FacultyId already exists or is invalid.")
                                result.postValue(false)
                            }
                        }
                    } else {
                        // For students and admins, save directly
                        val userData = hashMapOf(
                            "email" to user.email,
                            "name" to user.name,
                            "role" to user.role,
                            "studentRollNo" to user.studentRollNo // Save studentRollNo
                        )
                        firestore.collection("users").document(userId).set(userData)
                            .addOnSuccessListener {
                                when (user.role) {
                                    "student" -> saveStudentProfile(user.studentRollNo, user) // Use studentRollNo as the document ID
                                    else -> saveAdminProfile(userId, user)
                                }
                                result.postValue(true)
                            }
                            .addOnFailureListener {
                                result.postValue(false)
                            }
                    }
                } else {
                    result.postValue(false)
                }
            }
        return result
    }

    private fun saveStudentProfile(studentRollNo: String, user: User) {
        val studentData = hashMapOf(
            "name" to user.name,
            "studentRollNo" to user.studentRollNo,
            "email" to user.email,
            "studentSection" to user.studentSection
        )

        // Save student data in the 'studentProfiles' collection using studentRollNo as the document ID
        firestore.collection("studentProfiles").document(studentRollNo).set(studentData)

        // Data to add to the section
        val studentDataForSection = mapOf(
            "rollNo" to user.studentRollNo,
            "name" to user.name
        )

        // Add the student's data to the section in 'sections' collection
        firestore.collection("sections")
            .document(user.studentSection)
            .update("students", FieldValue.arrayUnion(studentDataForSection))
            .addOnFailureListener {
                // If the section document doesn't exist, create it
                val sectionData = hashMapOf(
                    "sectionName" to user.studentSection,
                    "students" to listOf(studentDataForSection) // Add the student data as a new list
                )
                firestore.collection("sections").document(user.studentSection).set(sectionData)
            }
    }

    private fun saveFacultyProfile(facultyId: String, user: User) {
        val facultyData = hashMapOf(
            "name" to user.name,
            "email" to user.email,
            "facultyId" to facultyId // Save FacultyId
        )
        firestore.collection("facultyProfiles")
            .document(facultyId) // Using facultyId as the document ID
            .set(facultyData)
            .addOnSuccessListener {
                Log.d("FacultyViewModel", "Faculty profile saved successfully.")
            }
            .addOnFailureListener { exception ->
                Log.e("FacultyViewModel", "Failed to save faculty profile: ${exception.message}")
            }
    }

    private fun saveAdminProfile(adminId: String, user: User) {
        val adminData = hashMapOf(
            "name" to user.name
        )
        firestore.collection("adminProfiles").document(adminId).set(adminData)
    }

    private fun validateFacultyId(facultyId: String, callback: (Boolean) -> Unit) {
        firestore.collection("facultyProfiles").document(facultyId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // FacultyId already exists
                    callback(false)
                } else {
                    // FacultyId is valid
                    callback(true)
                }
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    // FOR LOGIN

    suspend fun login(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: ""

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun facultyLogin(email: String, password: String): Result<String> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val userId = authResult.user?.uid ?: ""

            Result.success(userId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserRole(userId: String): LiveData<String> {
        val userRole = MutableLiveData<String>()
        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val role = documentSnapshot.getString("role") ?: ""
                    userRole.value = role
                } else {
                    userRole.postValue("User not found")
                }
            }.addOnFailureListener {
                userRole.value = ""
            }
        return userRole
    }
}



















//    fun signUpUser(user: User): LiveData<Boolean> {
//        val result = MutableLiveData<Boolean>()
//        auth.createUserWithEmailAndPassword(user.email, user.password)
//            .addOnCompleteListener {
//                if (it.isSuccessful) {
//
//
//
//
//                    //this will create a unique id for the user
//                    val userId = auth.currentUser?.uid ?: ""
//                    val userData = hashMapOf(
//                        "email" to user.email,
//                        "name" to user.name,
//                        "role" to user.role,
//
//
//
//
//                        )
//
//                    //now store this data in the firestore so that we can easily retrieve it
//
//                    firestore.collection("users").document(userId).set(userData)
//                        .addOnSuccessListener {
//                            when (user.role) {
//                                "faculty" -> {
//                                    saveFacultyProfile(userId,user)
//                                }
//                                "student" -> {
//                                    saveStudentProfile(userId,user)
//
//                                }
//                                else -> {
//                                    saveAdminProfile(userId,user)
//                                }
//                            }
//
//                            result.postValue(true)
//                        }
//                        .addOnFailureListener {
//                            result.postValue(false)
//
//                        }
//                }
//                else{
//                    result.postValue(false)
//                }
//            }
//        return result
//
//    }