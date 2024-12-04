package com.example.classflow.faculty.facultyAttendance.facultyMarkAttendance

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

    // Suspend function to save attendance
    suspend fun saveAttendance(
        sectionName: String,
        subjectName: String,
        date: String,
        facultyId: String,
        attendance: Map<String, String>
    ) {
        val attendanceData = mapOf(
            "markedBy" to facultyId,
            "attendance" to attendance
        )
        try {
            db.collection("attendanceRecords")
                .document(sectionName)
                .collection(subjectName)
                .document(date)
                .set(attendanceData).await()
        } catch (e: Exception) {
            throw e // Rethrow for ViewModel to handle
        }
    }
}
