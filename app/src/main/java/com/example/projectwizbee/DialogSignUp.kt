package com.example.projectwizbee

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.projectwizbee.databinding.ActivityDialogSignUpBinding
import com.example.projectwizbee.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth

class DialogSignUp : AppCompatActivity() {

    private lateinit var binding: ActivityDialogSignUpBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDialogSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val option1Animator = ObjectAnimator.ofFloat(binding.lumilipad, "translationX", -550f, 900f)
        option1Animator.apply {
            duration = 10000
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            start()
        }

        firebaseAuth = FirebaseAuth.getInstance()

        binding.textView.setOnClickListener {
            val intent = Intent(this, DialogSignIn::class.java)
            startActivity(intent)
        }
        binding.login.setOnClickListener {
            val email = binding.email.text.toString()
            val pass = binding.password.text.toString()
            val confirmPass = binding.confirmPassword.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {

                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(this, "Account Successfully Created!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java).also{
                                it.putExtra("NAME", email)
                            }
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                        }
                    }
                } else {
                    Toast.makeText(this, "Password is not matching", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}