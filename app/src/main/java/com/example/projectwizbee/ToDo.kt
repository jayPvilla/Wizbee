package com.example.projectwizbee

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.LinearLayoutCompat
import com.example.projectwizbee.R
import com.example.projectwizbee.Science

class ToDo : Fragment(R.layout.fragment_to_do) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find your button in the layout
        val science_button: Button = view.findViewById(R.id.Science_Button)
        val math_button: Button = view.findViewById(R.id.Math_Button)
        val history_button: Button = view.findViewById(R.id.History_Button)
        val literature_button: Button = view.findViewById(R.id.Literature_Button)

        val science_pic = view.findViewById<LinearLayoutCompat>(R.id.Science)
        science_pic.animate().apply{
            duration = 1000
            translationX(0f)
        }.start()

        val math_pic = view.findViewById<LinearLayoutCompat>(R.id.Math)
        math_pic.animate().apply{
            duration = 2000
            translationX(0f)

        }.start()

        val history_pic = view.findViewById<LinearLayoutCompat>(R.id.History)
        history_pic.animate().apply{
            duration = 3000
            translationX(0f)
        }.start()

        val literature_pic = view.findViewById<LinearLayoutCompat>(R.id.Literature)
        literature_pic.animate().apply{
            duration = 4000
            translationX(0f)
        }.start()


        // Set a click listener for your button
        science_button.setOnClickListener {
            val next_activity: String = "Science"
            call_Loading(next_activity)
        }
        math_button.setOnClickListener {
            val next_activity: String = "Math"
            call_Loading(next_activity)
        }
        history_button.setOnClickListener {
            val next_activity: String = "History"
            call_Loading(next_activity)
        }
        literature_button.setOnClickListener {
            val next_activity: String = "Literature"
            call_Loading(next_activity)
        }
    }
    private fun call_Loading(activity_next: String){
        val intent = Intent(requireContext(), Loading::class.java).also{
            it.putExtra("Next Activity", activity_next)}
        startActivity(intent)
    }
}
