package com.example.classflow.faculty.facultyAttendance.facultyMarkAttendance

import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.tasks.await

class AttendanceRepository {

    private val db = FirebaseFirestore.getInstance()

    // Suspend function to get students for a specific section
    suspend fun getStudentsForSection(sectionName: String): List<Student> {
        return try {
            val document = db.collection("sections").document(sectionName).get().await()
            val studentsList = document.get("students") as List<Map<String, String>>?
            studentsList?.map {
                Student(
                    rollNo = it["rollNo"] ?: "",
                    name = it["name"] ?: ""
                )
            } ?: emptyList()
        } catch (e: Exception) {
            emptyList() // Handle error case
        }
    }
    suspend fun saveAttendance(
        sectionName: String,
        subjectName: String,
        date: String,
        facultyId: String,
        attendance: Map<String, String>
    ) {
        val attendanceData = mapOf(
            "markedBy" to facultyId,
            "attendance" to attendance,
            "subjectName" to subjectName // Adding the subjectName field
        )

        try {
            // Save the attendance data to the subjectName subcollection under sectionName
            db.collection("attendanceRecords")
                .document(sectionName) // sectionName document
                .collection(subjectName) // subjectName subcollection
                .document(date) // Date document
                .set(attendanceData).await()

            // Save the subjectInfo under the sectionName document
            val sectionRef = db.collection("attendanceRecords").document(sectionName)

            // Get the document for the sectionName
            sectionRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // If the sectionName document exists, update the subjectInfo array
                    sectionRef.update("subjectInfo", FieldValue.arrayUnion(subjectName))
                        .addOnFailureListener { e ->
                            Log.e("ViewModel", "Error updating subjectInfo: ${e.message}")
                        }
                } else {
                    // If the sectionName document doesn't exist, create it with the subjectInfo field
                    val newSubjectInfo = mapOf("subjectInfo" to listOf(subjectName))

                    sectionRef.set(newSubjectInfo)
                        .addOnFailureListener { e ->
                            Log.e("ViewModel", "Error creating sectionName document: ${e.message}")
                        }
                }
            }
        } catch (e: Exception) {
            throw e // Rethrow for ViewModel to handle
        }


    }


    // Suspend function to save attendance
//    suspend fun saveAttendance(
//        sectionName: String,
//        subjectName: String,
//        date: String,
//        facultyId: String,
//        attendance: Map<String, String>
//    ) {
//        val attendanceData = mapOf(
//            "markedBy" to facultyId,
//            "attendance" to attendance
//        )
//        try {
//            db.collection("attendanceRecords")
//                .document(sectionName)
//                .collection(subjectName)
//                .document(date)
//                .set(attendanceData).await()
//        } catch (e: Exception) {
//            throw e // Rethrow for ViewModel to handle
//        }
//    }
}
