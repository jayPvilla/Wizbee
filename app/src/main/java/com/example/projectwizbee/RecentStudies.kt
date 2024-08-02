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

class RecentStudies : Fragment(R.layout.fragment_recent_studies) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find your button in the layout
        val botany_button: Button = view.findViewById(R.id.Botany_Button)

        val botany_pic = view.findViewById<LinearLayoutCompat>(R.id.Botany)
        botany_pic.animate().apply{
            duration = 1000
            translationX(0f)

        }.start()


        // Set a click listener for your button
        botany_button.setOnClickListener {
            val next_activity: String = "Botany"
            call_Loading(next_activity)
        }
        val music_button: Button = view.findViewById(R.id.Music_Button)

        val music_pic = view.findViewById<LinearLayoutCompat>(R.id.Music)
        music_pic.animate().apply{
            duration = 2000
            translationX(0f)

        }.start()


        // Set a click listener for your button
        music_button.setOnClickListener {
            val next_activity: String = "Music"
            call_Loading(next_activity)
        }
    }
    private fun call_Loading(activity_next: String){
        val intent = Intent(requireContext(), Loading::class.java).also{
            it.putExtra("Next Activity", activity_next)}
        startActivity(intent)
    }
}
