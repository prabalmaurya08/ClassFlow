package com.example.classflow.student.studentLogin

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.classflow.mvvm.User
import com.example.classflow.mvvm.UserRepository
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val studentRepository = UserRepository()
   val signupResult: LiveData<Boolean> = MutableLiveData()



    //Login for student and faculty
    private val repository=UserRepository()


    fun signUpStudent(studentUser: User) {
        studentRepository.signUpUser(studentUser).observeForever {
            (signupResult as MutableLiveData).postValue(it)

        }

    }

    private val _loginResult = MutableLiveData<Result<String>>()
    val loginResult: LiveData<Result<String>> get() = _loginResult

    fun login(email:String,password:String){
        viewModelScope.launch {
            val result=repository.login(email,password)
            _loginResult.value=result

        }
    }

    fun getUserRole(userId: String): LiveData<String> {
        return repository.getUserRole(userId)

    }
}