package com.example.classflow.faculty.facultyLogin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.classflow.mvvm.User
import com.example.classflow.mvvm.UserRepository

class FacultyViewModel(application: Application) : AndroidViewModel(application) {
    private val facultyRepository = UserRepository()
    val signupResult: LiveData<Boolean> = MutableLiveData()

    fun signUpFaculty(facultyUser: User) {
        facultyRepository.signUpUser(facultyUser).observeForever {
            (signupResult as MutableLiveData).postValue(it)

        }
    }


}