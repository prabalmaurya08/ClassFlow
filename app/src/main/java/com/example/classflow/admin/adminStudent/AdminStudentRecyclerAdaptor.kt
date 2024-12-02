package com.example.classflow.admin.adminStudent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.databinding.AdminStudentTimeTableCardBinding

class AdminStudentRecyclerAdaptor(private val datalist: List<AdminStudentDataClass>) :
    RecyclerView.Adapter<AdminStudentRecyclerAdaptor.ViewHolder>() {

    class ViewHolder(private val binding: AdminStudentTimeTableCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Bind data to the UI elements
        fun bind(data: AdminStudentDataClass) {
            binding.sectionName.text=data.sectionName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdminStudentRecyclerAdaptor.ViewHolder {
        val binding = AdminStudentTimeTableCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AdminStudentRecyclerAdaptor.ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}
