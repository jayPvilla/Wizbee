package com.example.projectwizbee

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectwizbee.databinding.ActivityDialogSignInBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DialogSignIn : AppCompatActivity() {

    private lateinit var binding: ActivityDialogSignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialogSignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val option1Animator = ObjectAnimator.ofFloat(binding.lumilipad, "translationX", -550f, 900f)
        option1Animator.apply {
            duration = 10000
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            start()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        binding.textView.setOnClickListener {
            val intent = Intent(this, DialogSignUp::class.java)
            startActivity(intent)
        }

        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        storeUserData(email, pass)
                        Toast.makeText(this, "Successfully Logged In!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java).also {
                            it.putExtra("NAME", email)
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun storeUserData(email: String, password: String) {
        val user = User(email, password)
        val userId = firebaseAuth.currentUser?.uid
        userId?.let {
            database.child("users").child(it).setValue(user).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(this, "User data stored successfully.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Failed to store user data: ${it.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
