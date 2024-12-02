package com.example.classflow.admin.adminStudent

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.databinding.AdminStudentTimeTableCardBinding

class SectionAdapter(
    private val sectionList: List<Section>,
    private val onUploadClick: (Section) -> Unit
) : RecyclerView.Adapter<SectionAdapter.SectionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        val binding = AdminStudentTimeTableCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SectionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        val section = sectionList[position]
        holder.bind(section)
    }

    override fun getItemCount(): Int = sectionList.size

    inner class SectionViewHolder(private val binding: AdminStudentTimeTableCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(section: Section) {
            binding.sectionName.text = section.sectionName
         //   binding.timetableUrl.text = section.timetableUrl.ifEmpty { "No timetable uploaded" }

            // Set click listener for the upload button
            binding.uploadTimeTableBtn.setOnClickListener {
                onUploadClick(section)
            }
        }
    }
}
