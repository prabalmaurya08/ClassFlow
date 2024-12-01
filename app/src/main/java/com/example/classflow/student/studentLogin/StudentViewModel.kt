package com.example.classflow.student.studentLogin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.classflow.mvvm.User
import com.example.classflow.mvvm.UserRepository

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val studentRepository = UserRepository()
    val signupResult: LiveData<Boolean> = MutableLiveData()

    fun signUpStudent(studentUser: User) {
        studentRepository.signUpUser(studentUser).observeForever {
            (signupResult as MutableLiveData).postValue(it)

        }

    }
}