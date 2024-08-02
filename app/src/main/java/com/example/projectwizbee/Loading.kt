package com.example.projectwizbee

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.bumptech.glide.Glide
import com.example.projectwizbee.databinding.ActivityLoadingBinding

class Loading : AppCompatActivity() {
    private lateinit var binding: ActivityLoadingBinding
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val activity = intent.getStringExtra("Next Activity")

        Glide.with(this)
            .load(R.drawable.loading_talbog_bee)
            .into(binding.loadingLogo)

        binding.loadingText.alpha = 0f
        binding.loadingText.animate().apply {
            duration = 7000
            alpha(1f)
        }
        binding.loadingLogo.animate().apply {
            duration = 4000
            translationX(0f)
        }
        Handler().postDelayed({
            binding.loadingLogo.animate().apply{
                duration = 2000
                translationX(550f)
            }
        }, 5500)
        mediaPlayer = MediaPlayer.create(this, R.raw.loading_bg)
        mediaPlayer.isLooping = false
        mediaPlayer.start()

            Handler().postDelayed({
                mediaPlayer.release()
                if (activity == "Science") {
                    val intent = Intent(this@Loading, Science::class.java)
                    startActivity(intent)
                    finish()
                } else if (activity == "Math") {
                    val intent = Intent(this@Loading, Math::class.java)
                    startActivity(intent)
                    finish()
                } else if (activity == "History") {
                    val intent = Intent(this@Loading, History::class.java)
                    startActivity(intent)
                    finish()
                } else if (activity == "Literature") {
                    val intent = Intent(this@Loading, Literature::class.java)
                    startActivity(intent)
                    finish()
                } else if (activity == "Botany") {
                    val intent = Intent(this@Loading, Botany::class.java)
                    startActivity(intent)
                    finish()
                } else if (activity == "Music") {
                    val intent = Intent(this@Loading, Music::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    val intent = Intent(this@Loading, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } }, 7000)
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
}