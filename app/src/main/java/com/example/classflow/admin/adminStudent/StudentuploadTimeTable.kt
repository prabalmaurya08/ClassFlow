package com.example.classflow.admin.adminStudent

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.classflow.R
import com.example.classflow.databinding.AdminStudentTimeTableCardBinding
import com.example.classflow.databinding.FragmentStudentTimeTableBinding
import com.example.classflow.databinding.FragmentStudentuploadTimeTableBinding


class StudentUploadTimeTable : Fragment() {
    private lateinit var binding: FragmentStudentuploadTimeTableBinding
    private lateinit var adaptorClass: AdminStudentRecyclerAdaptor

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentStudentuploadTimeTableBinding.inflate(layoutInflater)
        setUpCard()
        return binding.root
    }
    fun setUpCard(){
        val cardItems= listOf(
            AdminStudentDataClass("CS-44")
        )
        adaptorClass= AdminStudentRecyclerAdaptor(cardItems)
        binding.recyclerView.apply {
            layoutManager=LinearLayoutManager(context)
            adapter=adaptorClass
        }


    }

}