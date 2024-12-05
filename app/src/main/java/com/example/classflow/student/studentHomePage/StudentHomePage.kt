package com.example.classflow.student.studentHomePage

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.classflow.R
import com.example.classflow.databinding.FragmentStudentHomePageBinding
import com.example.classflow.faculty.facultyhome.FacultyHomeScreen.OnLogoutListener
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.auth.FirebaseAuth


class StudentHomePage : Fragment() {


    // Firebase Authentication instance
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var _binding: FragmentStudentHomePageBinding? = null
    private val binding get() = _binding!!



    // Firebase Authentication instance

    private var listener: OnStdLogoutListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Attach the listener to the hosting activity or parent fragment
        if (context is OnStdLogoutListener) {
            listener = context
        } else {
            throw ClassCastException("$context must implement OnLogoutListener")
        }
    }
            override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStudentHomePageBinding.inflate(inflater, container, false)


        setUpPieChart()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_logout -> {
                    Log.d("FacultyHomeScreen", "Logout menu clicked")
                    try {
                        // Sign out and navigate
                        auth.signOut()
                        Log.d("FacultyHomeScreen", "User signed out successfully")
                        Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                        listener?.onStdLogout()
                        findNavController().popBackStack(R.id.studentHomePage, false)

                        // Use safe navigation
                        if (isAdded && findNavController().currentDestination?.id == R.id.studentHomePage) {
                            Log.d("FacultyHomeScreen", "Navigating to main login screen")

                        } else {
                            Log.e("FacultyHomeScreen", "Navigation action is invalid or fragment is not attached")
                        }
                    } catch (e: Exception) {
                        Log.e("FacultyHomeScreen", "Error during logout navigation", e)
                        Toast.makeText(requireContext(), "An error occurred during logout", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun setUpPieChart(){

        val pieChart=binding.pieChart
        //Make array of PieEntry
        val list:ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(10f))
        list.add(PieEntry(20f))
        list.add(PieEntry(15f))


        val pieDataset=PieDataSet(list,"List")

        // Define a custom color list for each label
        val colors: ArrayList<Int> = ArrayList()
        context?.let { ContextCompat.getColor(it, R.color.blue) }?.let { colors.add(it) }       // Emergency
        context?.let { ContextCompat.getColor(it, R.color.green_bright) }?.let { colors.add(it) }       //pending
        context?.let { ContextCompat.getColor(it, R.color.grey) }?.let { colors.add(it) }  //confirmed
            // Cancelled


        // Set the custom colors to the dataset
        pieDataset.colors = colors
        pieDataset.valueTextSize=15f



        val pieData=PieData(pieDataset)
        pieChart.data=pieData

        pieChart.centerText="Today Stats"
        pieChart.animateY(2000)
        pieChart.legend.isEnabled = false

        pieChart.invalidate()
        setUpCustomLegend()


    }
    private fun setUpCustomLegend() {
        val labels = listOf( "Present %", "Present %", "Present %",)
        val colors = listOf(
            R.color.blue,
            R.color.green_bright,
            R.color.grey,

        )

        // Clear the layout before adding new views (in case of re-inflation)
        binding.customLegendLayout.removeAllViews()

        // Loop through labels and colors to create custom legend
        for (i in labels.indices) {
            // Create a horizontal LinearLayout for each legend item
            val legendItemLayout = LinearLayout(requireContext())
            legendItemLayout.orientation = LinearLayout.HORIZONTAL
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 20, 0, 20) // Margin for spacing
            legendItemLayout.layoutParams = params

            // Create a View for the color circle
            val colorCircle = View(requireContext())
            val circleParams = LinearLayout.LayoutParams(40, 40)
            circleParams.setMargins(0, 0, 25, 0) // Margin between circle and label
            colorCircle.layoutParams = circleParams

            // Create a circular drawable programmatically
            val drawable = GradientDrawable()
            drawable.shape = GradientDrawable.OVAL // Set shape to oval (circle)
            drawable.setColor(ContextCompat.getColor(requireContext(), colors[i])) // Set the color

            // Set the drawable as the background of the colorCircle view
            colorCircle.background = drawable

            // Create a TextView for the label
            val labelText = TextView(requireContext())
            labelText.text = labels[i]
            labelText.textSize = 14f
            labelText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            // Add the color circle and label to the legend item layout
            legendItemLayout.addView(colorCircle)
            legendItemLayout.addView(labelText)

            // Add the legend item layout to the custom legend layout
            binding.customLegendLayout.addView(legendItemLayout)
        }
    }
    interface OnStdLogoutListener {
        fun onStdLogout()
    }


}