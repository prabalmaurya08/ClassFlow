package com.example.classflow.faculty.facultyAttendance.facultyMarkAttendance

data class Student(val rollNo: String = "",
                   val name: String = "",
                   var attendanceStatus: String = "absent" // Default to "absent"
)
