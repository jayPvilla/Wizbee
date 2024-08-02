

package com.example.projectwizbee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

@Suppress("DEPRECATION")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val bee = findViewById<ImageView>(R.id.bee)
        bee.alpha = 0f

        bee.animate().apply {
            duration = 5000
            scaleXBy(2f)
            scaleYBy(2f)
            alpha(1f)
            withEndAction {
                val intent = Intent(this@SplashActivity, DialogSignIn::class.java)
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            }
        }
    }
}

