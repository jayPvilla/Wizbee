//dialog_form

package com.example.projectwizbee

import android.app.Dialog
import android.content.Intent
import android.media.Image
import androidx.fragment.app.DialogFragment
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.view.ViewCompat.animate
import com.bumptech.glide.Glide

class dialog_form : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_dialog_form, container, false)
        val close = view.findViewById<ImageButton>(R.id.backbtn)

        val sticker = view.findViewById<ImageView>(R.id.intro_gif)
        Glide.with(this)
            .load(R.drawable.broken_bee)
            .into(sticker)
        sticker.animate().apply {
            duration = 1200
            translationY(-950f)
        }

        Handler().postDelayed({
            sticker.animate().apply {
                duration = 3000
                translationY(-2500f)
            }
            val imageView = view.findViewById<ImageView>(R.id.dialog_logo)
            Glide.with(this)
                .load(R.drawable.pagod_bee)
                .into(imageView)

            val mj_pic = view.findViewById<LinearLayoutCompat>(R.id.mj_layout)
            mj_pic.animate().apply {
                duration = 2000
                rotationXBy(360f)
                translationY(0f)
            }.start()

            val dc_pic = view.findViewById<LinearLayoutCompat>(R.id.dc_layout)
                dc_pic.animate().apply{
                    duration = 2000
                    rotationXBy(360f)
                    translationY(0f)
                }.start()

            val cj_pic = view.findViewById<LinearLayoutCompat>(R.id.cj_layout)
                cj_pic.animate().apply{
                    duration = 2000
                    rotationXBy(360f)
                    translationY(0f)
                }.start()

            val jp_pic = view.findViewById<LinearLayoutCompat>(R.id.jp_layout)
                jp_pic.animate().apply{
                    duration = 2000
                    rotationXBy(360f)
                    translationY(0f)
                }.start()
        }, 3500)

        close.setOnClickListener{
            dismiss()
        }
        return view
    }
}








