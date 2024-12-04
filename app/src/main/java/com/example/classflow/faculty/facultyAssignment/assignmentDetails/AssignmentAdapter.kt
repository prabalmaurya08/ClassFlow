package com.example.classflow.faculty.facultyAssignment.assignmentDetails

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.classflow.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AssignmentAdapter(
    private var assignments: MutableList<Assignment>,
    private val onDownloadClicked: (Assignment) -> Unit
) : RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder>() {

    class AssignmentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val assignmentName: TextView = view.findViewById(R.id.assignmentName)
        val downloadButton: Button = view.findViewById(R.id.downloadButton)
        val timestampTextView: TextView = view.findViewById(R.id.timestampTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssignmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_assignment, parent, false)
        return AssignmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssignmentViewHolder, position: Int) {
        val assignment = assignments[position]
        holder.assignmentName.text = assignment.name
        // Convert timestamp to readable date format
        val date = Date(assignment.timestamp) // Convert the timestamp to a Date object
        val format = SimpleDateFormat("dd MMM yyyy HH:mm", Locale.getDefault()) // Define your desired format
        val formattedDate = format.format(date) // Format the Date object

        // Display the formatted date in the card (you can create a new TextView for this in the layout)
        holder.timestampTextView.text = formattedDate
        holder.downloadButton.setOnClickListener {
            onDownloadClicked(assignment)
        }
    }

    override fun getItemCount(): Int = assignments.size

    /**
     * Updates the data in the adapter and refreshes the RecyclerView.
     * @param newAssignments The new list of assignments to be displayed.
     */
    @SuppressLint("NotifyDataSetChanged")
    fun updateAssignments(newAssignments: List<Assignment>) {
        assignments.clear()
        assignments.addAll(newAssignments)
        notifyDataSetChanged() // Refresh the entire RecyclerView (can be optimized further)
    }
}

data class Assignment(
    val id: String = "",
    val name: String = "",
    val url: String = "",
    val timestamp: Long = 0L
)
