package com.example.classflow.admin.adminFaculty

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.databinding.FragmentFacultyUploadTimeTableAndClassAllotBinding


class FacultyUploadTimeTableAndClassAllot : Fragment() {

    private lateinit var binding: FragmentFacultyUploadTimeTableAndClassAllotBinding
    private lateinit var adaptorClass: AdminFacultyRecyclerAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentFacultyUploadTimeTableAndClassAllotBinding.inflate(layoutInflater)
//        setupcard()
        return binding.root
    }

    fun setupcard(){
        val cardItems= listOf(
                AdminFacultyDataClass(
                        "Anurag", "#31234"
                    ),
                AdminFacultyDataClass(
                        "Anurag", "#31234"
                    ),
                AdminFacultyDataClass(
                        "Anurag", "#31234"
                    ),
                AdminFacultyDataClass(
                        "Anurag", "#31234"
                    ),
                AdminFacultyDataClass(
                        "Anurag", "#31234"
                    )
                )
        adaptorClass = AdminFacultyRecyclerAdaptor(cardItems)
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adaptorClass

        }

        }

}



