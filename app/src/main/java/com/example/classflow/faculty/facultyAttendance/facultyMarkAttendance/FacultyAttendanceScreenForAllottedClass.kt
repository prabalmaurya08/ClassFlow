package com.example.classflow.faculty.facultyAttendance.facultyMarkAttendance

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.databinding.FragmentFacultyAttendancescreenForAllotedclassBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.ViewModelProvider
import java.util.Calendar

class FacultyAttendanceScreenForAllottedClass : Fragment() {

    private lateinit var binding: FragmentFacultyAttendancescreenForAllotedclassBinding

    private lateinit var sectionName: String
    private lateinit var subjectName: String
    private var facultyId: String? = null

    private lateinit var adapter: StudentAttendanceAdapter

    // Using ViewModelProvider with factory
    private val viewModel: AttendanceMarkingViewModel by lazy {
        ViewModelProvider(
            this,
            AttendanceMarkingViewModelFactory(AttendanceRepository())
        )[AttendanceMarkingViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFacultyAttendancescreenForAllotedclassBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get Safe Args
        val args = FacultyAttendanceScreenForAllottedClassArgs.fromBundle(requireArguments())
        sectionName = args.section
        subjectName = args.subject

        // Fetch facultyId
        fetchFacultyId()
        setupRecyclerView()
        setupObservers()
        setupDatePicker()

        // Load students
        viewModel.loadStudents(sectionName)

        binding.submitBtn.setOnClickListener {
            val currentDate = binding.showDate.text.toString()
            if (currentDate.isEmpty()) {
                Toast.makeText(requireContext(), "Please select a date", Toast.LENGTH_SHORT).show()
            } else {
                facultyId?.let { id ->
                    viewModel.saveAttendance(
                        sectionName,
                        subjectName,
                        currentDate,
                        id,
                        adapter.getStudents()
                    )
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = StudentAttendanceAdapter()
        binding.recylerview.adapter = adapter
        binding.recylerview.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        viewModel.studentsLiveData.observe(viewLifecycleOwner) { students ->
            adapter.submitList(students)
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDatePicker() {
        binding.datePicker.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { _, year, month, day ->
                    binding.showDate.text = "$day-${month + 1}-$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.show()
        }
    }

    private fun fetchFacultyId() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = FirebaseFirestore.getInstance()
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    facultyId = document.getString("facultyId")
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error fetching faculty ID", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
