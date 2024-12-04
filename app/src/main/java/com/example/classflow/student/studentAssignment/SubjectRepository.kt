package com.example.classflow.student.studentAssignment



import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SubjectRepository(private val firestore: FirebaseFirestore) {

    suspend fun fetchSubjects(section: String): List<String> {
        return try {
            val snapshot = firestore.collection("assignments")
                .document(section)
                .collection("subjects") // This assumes 'subjects' are documents under the section
                .get()
                .await()

            snapshot.documents.map { it.id } // Return document IDs as subject names
        } catch (e: Exception) {
            emptyList()
        }
    }
}
