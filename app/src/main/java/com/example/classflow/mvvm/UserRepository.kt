package com.example.classflow.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun signUpUser(user: User): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener {
                if (it.isSuccessful) {




                    //this will create a unique id for the user
                    val userId = auth.currentUser?.uid ?: ""
                    val userData = hashMapOf(
                        "email" to user.email,
                        "name" to user.name,
                        "role" to user.role,




                        )

                    //now store this data in the firestore so that we can easily retrieve it

                    firestore.collection("users").document(userId).set(userData)
                        .addOnSuccessListener {
                            when (user.role) {
                                "faculty" -> {
                                    saveFacultyProfile(userId,user)
                                }
                                "student" -> {
                                    saveStudentProfile(userId,user)

                                }
                                else -> {
                                    saveAdminProfile(userId,user)
                                }
                            }

                            result.postValue(true)
                        }
                        .addOnFailureListener {
                            result.postValue(false)

                        }
                }
                else{
                    result.postValue(false)
                }
            }
        return result

    }

    private fun saveStudentProfile(studentId: String,user: User){
        val studentData = hashMapOf(

            "name" to user.name,
            "studentRollNo" to user.studentRollNo,
            "email" to user.email,
            "studentSection" to user.studentSection



        )
        firestore.collection("studentProfiles").document(studentId).set(studentData)


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
    private fun saveFacultyProfile(facultyId: String,user: User){
        val facultyData = hashMapOf(

            "name" to user.name ,
            "email" to user.email,
          //  "facultyId" to user.facultyId




            )
        firestore.collection("facultyProfiles").document(facultyId).set(facultyData)
    }

    private fun saveAdminProfile(adminId: String,user: User){
        val adminData = hashMapOf(

            "name" to user.name,



            )
        firestore.collection("adminProfiles").document(adminId).set(adminData)

    }


}