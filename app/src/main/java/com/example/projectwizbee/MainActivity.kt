package com.example.projectwizbee

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.findNavController
import com.example.projectwizbee.databinding.ActivityMainBinding
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import de.hdodenhof.circleimageview.CircleImageView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit


class MainActivity : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    lateinit var binding : ActivityMainBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val fab: FloatingActionButton = findViewById(R.id.floatingbutton)
        fab.setOnClickListener {
            showDialog()
        }
        binding.imageView8.animate().apply{
            duration = 5000
            rotationYBy(360f)
        }.start()

        val user_name = intent.getStringExtra("NAME")
        binding.username.text = user_name

        binding.openSideBar.setOnClickListener{
            val option1Animator = ObjectAnimator.ofFloat(binding.sideBar, "translationX", -450f, 0f)
            option1Animator.apply {
                duration = 2000
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }
        binding.backbtn.setOnClickListener{
            val option1Animator = ObjectAnimator.ofFloat(binding.sideBar, "translationX", 0f, -450f)
            option1Animator.apply {
                duration = 2000
                interpolator = AccelerateDecelerateInterpolator()
                start()
            }
        }
        val floating = findViewById<FloatingActionButton>(R.id.floatingbutton)
        val animationScaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        val animationScaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down)

        animationScaleUp.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                floating.startAnimation(animationScaleDown)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

        binding.ToDoBtn.setOnClickListener {
            navigateTo(R.id.ToDoFragment)

            binding.ToDoBtn.setBackgroundResource(R.color.yellow)
            binding.RecentStudiesBtn.setBackgroundResource(R.color.purple_500)
        }
        binding.RecentStudiesBtn.setOnClickListener {
            navigateTo(R.id.action_home_to_recent_studies)
            binding.ToDoBtn.setBackgroundResource(R.color.purple_500)
            binding.RecentStudiesBtn.setBackgroundResource(R.color.yellow)
        }

        mediaPlayer = MediaPlayer.create(this, R.raw.wand)
        mediaPlayer.isLooping = false
        mediaPlayer.start()

        val option1Animator = ObjectAnimator.ofFloat(binding.imageView9, "translationX", -500f, 60f)
        option1Animator.apply {
            duration = 10000
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            start()
        }

        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 500,
            colors = listOf(0xfce18a, 0xffc0cb, 0x800080, 0xffa500),
            emitter = Emitter(duration = 1, TimeUnit.SECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )

        binding.konfettiView.start(party)

        binding.sideBarScience.setOnClickListener {
            val next_activity: String = "Science"
            call_Loading(next_activity)
        }
        binding.sideBarMath.setOnClickListener {
            val next_activity: String = "Math"
            call_Loading(next_activity)
        }
        binding.sideBarHistory.setOnClickListener {
            val next_activity: String = "History"
            call_Loading(next_activity)
        }
        binding.sideBarLiterature.setOnClickListener {
            val next_activity: String = "Literature"
            call_Loading(next_activity)
        }
        binding.sideBarBotany.setOnClickListener {
            val next_activity: String = "Botany"
            call_Loading(next_activity)
        }
        auth = FirebaseAuth.getInstance()

        val logoutButton: Button = findViewById(R.id.logout)
        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun navigateTo(actionId: Int) {
        findNavController(R.id.nav_host_fragment).navigate(actionId)
    }
    private fun showDialog() {
        val fragmentManager: FragmentManager = supportFragmentManager
        val dialogFragment = dialog_form()
        dialogFragment.show(fragmentManager, "MyDialog")
    }
    private fun call_Loading(activity_next: String){
        val intent = Intent(this, Loading::class.java).also{
            it.putExtra("Next Activity", activity_next)}
        startActivity(intent)
    }
    private fun logout(){
        auth.signOut()
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
    override fun onDestroy(){
        super.onDestroy()
        mediaPlayer.release()

    }


}

