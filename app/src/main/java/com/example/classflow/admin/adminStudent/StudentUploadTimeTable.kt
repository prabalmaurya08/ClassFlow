package com.example.classflow.admin.adminStudent

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.databinding.FragmentStudentuploadTimeTableBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class StudentUploadTimeTable : Fragment() {
    private lateinit var binding: FragmentStudentuploadTimeTableBinding
 //   private lateinit var adaptorClass: AdminStudentRecyclerAdaptor



    private lateinit var sectionAdapter: SectionAdapter
    private lateinit var sectionList: List<Section>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentStudentuploadTimeTableBinding.inflate(layoutInflater)
        setUpCard()
        return binding.root
    }
    private fun setUpCard(){

       binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Example data
        sectionList = listOf(
            Section("CS41"),
            Section("CS42"),
            Section("CS43") ,
            Section("CS44"),
            // Add more sections as needed
        )

        sectionAdapter = SectionAdapter(sectionList) { section ->
            // Handle the "Upload Timetable" button click
            openFilePicker(section)
        }

        binding.recyclerView.adapter = sectionAdapter



    }

    private fun openFilePicker(section: Section) {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "application/pdf"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PDF_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PDF_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val pdfUri = data?.data // Get the selected file URI
            if (pdfUri != null) {
                // Upload the selected PDF to Firebase Storage and update Firestore
                uploadTimetableForSection(sectionList[0].sectionName, pdfUri) // Upload for the first section as example
            }
        }
    }

    private fun uploadTimetableForSection(sectionName: String, pdfUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference.child("timetables/$sectionName.pdf")

        // Upload the file to Firebase Storage
        storageRef.putFile(pdfUri)
            .addOnSuccessListener {
                // Get the download URL of the uploaded PDF
                storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val timetableUrl = downloadUrl.toString()

                    // Update the timetableUrl in the 'sections' collection
                    FirebaseFirestore.getInstance().collection("sections")
                        .document(sectionName)
                        .update("timetableUrl", timetableUrl)
                        .addOnSuccessListener {
                            // Timetable URL updated successfully
                            Toast.makeText(requireContext(), "Timetable uploaded successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(requireContext(), "Error updating timetable URL: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error uploading timetable: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }



    companion object {
        const val PDF_REQUEST_CODE = 101
    }

}