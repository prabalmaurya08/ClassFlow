package com.example.classflow.faculty.facultyAssignment.assignmentDetails

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class FacultyAssignmentDetailViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    val assignmentsLiveData = MutableLiveData<List<Assignment>>()
    val uploadStatusLiveData = MutableLiveData<String>()

    fun fetchAssignments(section: String, subject: String) {
        firestore.collection("assignments")
            .document(section)
            .collection(subject)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("ViewModel", "Error fetching assignments: ${error.message}")
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val assignments = snapshot.documents.mapNotNull {
                        it.toObject(Assignment::class.java)?.copy(id = it.id)
                    }
                    Log.d("ViewModel", "Assignments fetched: $assignments")
                    assignmentsLiveData.postValue(assignments)
                }
            }
    }

    fun uploadAssignment(
        section: String,
        subject: String,
        assignmentName: String,
        fileUri: Uri
    ) {
        val fileRef = storage.reference.child("assignments/${System.currentTimeMillis()}_${fileUri.lastPathSegment}")
        fileRef.putFile(fileUri)
            .continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception("Unknown error during file upload")
                }
                fileRef.downloadUrl
            }
            .addOnSuccessListener { downloadUri ->
                val assignment = mapOf(
                    "name" to assignmentName,
                    "url" to downloadUri.toString(),
                    "timestamp" to System.currentTimeMillis()
                )

                // Ensure the section document exists
                val sectionRef = firestore.collection("assignments").document(section)
                sectionRef.get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        // If the section document exists, update the 'subjects' array
                        sectionRef.update("subjects", FieldValue.arrayUnion(subject))
                            .addOnFailureListener { e ->
                                Log.e("ViewModel", "Error adding subject to section: ${e.message}")
                            }
                    } else {
                        // If the section document doesn't exist, create it and set the 'subjects' field
                        val newSectionData = mapOf(
                            "subjects" to listOf(subject)
                        )
                        sectionRef.set(newSectionData)
                            .addOnFailureListener { e ->
                                Log.e("ViewModel", "Error creating section: ${e.message}")
                            }
                    }

                    // Add assignment to the subject's collection
                    sectionRef.collection(subject)
                        .add(assignment)
                        .addOnSuccessListener {
                            Log.d("ViewModel", "Assignment uploaded successfully: $assignment")
                            uploadStatusLiveData.postValue("Upload successful")
                        }
                        .addOnFailureListener { e ->
                            Log.e("ViewModel", "Error uploading assignment: ${e.message}")
                            uploadStatusLiveData.postValue("Firestore upload failed: ${e.message}")
                        }
                }
                    .addOnFailureListener { e ->
                        Log.e("ViewModel", "Error fetching section document: ${e.message}")
                    }
            }
            .addOnFailureListener { e ->
                Log.e("ViewModel", "Error uploading file to Storage: ${e.message}")
                uploadStatusLiveData.postValue("Storage upload failed: ${e.message}")
            }
    }



//    fun uploadAssignment(
//        section: String,
//        subject: String,
//        assignmentName: String,
//        fileUri: Uri
//    ) {
//        val fileRef = storage.reference.child("assignments/${System.currentTimeMillis()}_${fileUri.lastPathSegment}")
//        fileRef.putFile(fileUri)
//            .continueWithTask { task ->
//                if (!task.isSuccessful) {
//                    Log.e("ViewModel", "File upload failed", task.exception)
//                    throw task.exception ?: Exception("Unknown error during file upload")
//                }
//                fileRef.downloadUrl
//            }
//            .addOnSuccessListener { downloadUri ->
//                val assignment = mapOf(
//                    "name" to assignmentName,
//                    "url" to downloadUri.toString(),
//                    "timestamp" to System.currentTimeMillis()
//                )
//                firestore.collection("assignments")
//                    .document(section)
//                    .collection(subject)
//                    .add(assignment)
//                    .addOnSuccessListener {
//                        Log.d("ViewModel", "Assignment uploaded successfully: $assignment")
//                        uploadStatusLiveData.postValue("Upload successful")
//                    }
//                    .addOnFailureListener { e ->
//                        Log.e("ViewModel", "Error uploading to Firestore: ${e.message}")
//                        uploadStatusLiveData.postValue("Firestore upload failed: ${e.message}")
//                    }
//            }
//            .addOnFailureListener { e ->
//                Log.e("ViewModel", "Error uploading file to Firebase Storage: ${e.message}")
//                uploadStatusLiveData.postValue("Storage upload failed: ${e.message}")
//            }
//    }
}
