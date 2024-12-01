package com.example.classflow.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore

class FacultyRepository {

    private val firestore = FirebaseFirestore.getInstance()

    fun getFacultyList(): LiveData<List<Faculty>> {
        val facultyLiveData = MutableLiveData<List<Faculty>>()
        firestore.collection("facultyProfiles")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val facultyList = querySnapshot.documents.mapNotNull { it.toObject(Faculty::class.java) }
                facultyLiveData.value = facultyList
            }
            .addOnFailureListener { exception ->
                Log.e("FacultyRepository", "Error fetching faculty list", exception)
                facultyLiveData.value = emptyList()
            }
        return facultyLiveData
    }
}
