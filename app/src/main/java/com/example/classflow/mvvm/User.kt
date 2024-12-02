package com.example.classflow.mvvm

data class User(
    val name: String="",
    val email: String="",
    val password: String="",
    val role:String="",
    val studentSection:String="",
    val studentRollNo:String="",
    val facultyId:String=""


)

data class Faculty(
    val facultyId: String = "",
    val name: String = "",
    val email: String = "",
    val timetableUrl: String? = null,
    val allottedClasses: List<AllottedClass>? = null
)

data class AllottedClass(
    val section: String="",
    val subject: String=""
)