package com.example.classflow.admin.adminFaculty

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.databinding.AdminFacutlyTimetableAndClassallotmentCardBinding

class AdminFacultyRecyclerAdaptor (private val datalist: List<AdminFacultyDataClass>):RecyclerView.Adapter<AdminFacultyRecyclerAdaptor.ViewHolder>(){
   class ViewHolder(private val binding: AdminFacutlyTimetableAndClassallotmentCardBinding):RecyclerView.ViewHolder(binding.root){
       fun bind(data:AdminFacultyDataClass){
           binding.facultyName.text=data.Faculty_name
           binding.facultyId.text=data.Faculty_id

//           binding.uploadTimeTableBtn.setOnClickListener {
//               data.onUploadTimeTable()
//           }
//           binding.allotClassBtn.setOnClickListener {
//               data.onAllotClass()
//           }
       }
   }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):ViewHolder {
        val binding=AdminFacutlyTimetableAndClassallotmentCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder:ViewHolder, position: Int) {
        holder.bind(datalist[position])
    }

    override fun getItemCount(): Int {
        return datalist.size
    }
}