package com.example.projectwizbee

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Color.red
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.projectwizbee.databinding.ActivityMathBinding
import com.bumptech.glide.Glide
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit


class Math : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var binding: ActivityMathBinding
    private val questions = arrayOf(
        "Addition: What is the sum of 34 and 58?",
        "Subtraction: What is the difference when 75 is subtracted from 112?",
        "Multiplication: What is the product of 7 and 9?",
        "Division: What is 144 divided by 12?",
        "Fractions: What is the simplified form of the fraction 8/24?",
        "Decimals: What is the decimal form of the fraction 3/8?",
        "Percentages: What is 15% of 200?",
        "Powers: What is 2 raised to the power of 4?",
        "Square Roots: What is the square root of 64?",
        "Algebra: Solve for x in the equation: 3x+5=20.")

    private val options = arrayOf(arrayOf("90", "82", "92"),
        arrayOf("27", "37", "47"),
        arrayOf("63", "56", "72"),
        arrayOf("14","12", "13"),
        arrayOf("1/3", "2/6", "1/4"),
        arrayOf("0.375", "3.75", "37.5"),
        arrayOf("3", "30", "300"),
        arrayOf("8", "32", "16"),
        arrayOf("7", "8", "16"),
        arrayOf("x=6", "x=4", "x=5"),

        )

    private val correctAnswers = arrayOf(2, 1, 0, 1, 0, 0, 1, 2, 1, 2)

    private var currentQuestionIndex = 0
    private var score = 0

    private var initialProgress: Int = 0
    private var currentQuestion: Int = 0
    private var totalQuestions: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMathBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val backgroundView = findViewById<ImageView>(R.id.backgroundView)
        val animationScaleUp = AnimationUtils.loadAnimation(this, R.anim.scale_up)
        val animationScaleDown = AnimationUtils.loadAnimation(this, R.anim.scale_down)

        animationScaleUp.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                backgroundView.startAnimation(animationScaleDown)
            }
            override fun onAnimationRepeat(animation: Animation?) {}
        })

        backgroundView.startAnimation(animationScaleUp)

        Glide.with(this)
            .load(R.drawable.loading_talbog_bee)
            .into(binding.lumilipad)

        // Assuming you have a boolean variable isAlternateMusic set based on some condition

        val option1Animator = ObjectAnimator.ofFloat(binding.lumilipad, "translationX", -550f, 600f)
        option1Animator.apply {
            duration = 10000
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            start()
        }

        binding.mathQuestionText.visibility= View.GONE
        binding.option1Button.visibility= View.GONE
        binding.option2Button.visibility= View.GONE
        binding.option3Button.visibility= View.GONE
        binding.lumilipad.visibility =View.GONE
        binding.backbtn.visibility =View.GONE
        binding.scoreTextView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE

        binding.progressBar.progress = 0

        showInstructionsDialog()

        binding.option1Button.setOnClickListener {
            checkAnswer(0)
        }
        binding.option2Button.setOnClickListener {
            checkAnswer(1)
        }
        binding.option3Button.setOnClickListener {
            checkAnswer(2)
        }

        binding.backbtn.setOnClickListener{
            mediaPlayer.release()
            val intent = Intent(this@Math, MainActivity::class.java)
            startActivity(intent)
        }
        totalQuestions = questions.size
        // Calculate initial progress based on total questions
        initialProgress = 100 / totalQuestions
        // Initialize progress bar with initial progress
        binding.progressBar.progress = initialProgress

    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    private fun correctButtonColors(math_buttonIndex: Int){
        when(math_buttonIndex){
            0 -> binding.option1Button.setBackgroundColor(Color.GREEN)
            1 -> binding.option2Button.setBackgroundColor(Color.GREEN)
            2 -> binding.option3Button.setBackgroundColor(Color.GREEN)
        }
    }
    private fun wrongButtonColors(math_buttonIndex: Int){
        when(math_buttonIndex){
            0 -> binding.option1Button.setBackgroundColor(Color.RED)
            1 -> binding.option2Button.setBackgroundColor(Color.RED)
            2 -> binding.option3Button.setBackgroundColor(Color.RED)
        }
    }
    private fun resetButtonColors(){
        binding.option1Button.setBackgroundColor(Color.WHITE)
        binding.option2Button.setBackgroundColor(Color.WHITE)
        binding.option3Button.setBackgroundColor(Color.WHITE)
    }
    private fun displayQuestion(){
        binding.option1Button.isEnabled = true
        binding.option2Button.isEnabled = true
        binding.option3Button.isEnabled = true
        binding.mathQuestionText.text = questions[currentQuestionIndex]
        binding.option1Button.text = options[currentQuestionIndex][0]
        binding.option2Button.text = options[currentQuestionIndex][1]
        binding.option3Button.text = options[currentQuestionIndex][2]

        // Slide animation for question text
        val questionAnimator = ObjectAnimator.ofFloat(binding.mathQuestionText, "translationX", -1000f, 0f)
        questionAnimator.apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        // Slide animation for option buttons
        val option1Animator = ObjectAnimator.ofFloat(binding.option1Button, "translationX", -1000f, 0f)
        option1Animator.apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        val option2Animator = ObjectAnimator.ofFloat(binding.option2Button, "translationX", -1000f, 0f)
        option2Animator.apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        val option3Animator = ObjectAnimator.ofFloat(binding.option3Button, "translationX", -1000f, 0f)
        option3Animator.apply {
            duration = 1000
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }
        if (currentQuestionIndex == 0){
            binding.progressBar.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.VISIBLE
        }

        resetButtonColors()
    }
    private fun checkAnswer(selectedAnswerIndex: Int){
        binding.option1Button.isEnabled = false
        binding.option2Button.isEnabled = false
        binding.option3Button.isEnabled = false
        val correctAnswerIndex = correctAnswers[currentQuestionIndex]
        updateProgressBar()

        if (selectedAnswerIndex == correctAnswerIndex){
            score++
            correctButtonColors(selectedAnswerIndex)
            binding.scoreTextView.text = "Score: $score/${questions.size.toString()}"
        } else {
            wrongButtonColors(selectedAnswerIndex)
            correctButtonColors(correctAnswerIndex)
        }
        if (currentQuestionIndex < questions.size - 1){
            currentQuestionIndex++
            Handler().postDelayed({
                val option0Animator = ObjectAnimator.ofFloat(binding.mathQuestionText, "translationX", 0f, 1000f)
                option0Animator.apply {
                    duration = 1000
                    interpolator = AccelerateDecelerateInterpolator()
                    start()
                }
                val option1Animator = ObjectAnimator.ofFloat(binding.option1Button, "translationX", 0f, 1000f)
                option1Animator.apply {
                    duration = 1000
                    interpolator = AccelerateDecelerateInterpolator()
                    start()
                }
                val option2Animator = ObjectAnimator.ofFloat(binding.option2Button, "translationX", 0f, 1000f)
                option2Animator.apply {
                    duration = 1000
                    interpolator = AccelerateDecelerateInterpolator()
                    start()
                }
                val option3Animator = ObjectAnimator.ofFloat(binding.option3Button, "translationX", 0f, 1000f)
                option3Animator.apply {
                    duration = 1000
                    interpolator = AccelerateDecelerateInterpolator()
                    start()
                }
            }, 1000)
            binding.mathQuestionText.postDelayed({displayQuestion()}, 1500)
        } else {
            showResults()
        }
    }
    private fun restartQuiz() {
        mediaPlayer.release()
        currentQuestionIndex = 0
        score = 0
        binding.countdown.visibility = View.VISIBLE
        binding.mathQuestionText.visibility = View.GONE
        binding.option1Button.visibility = View.GONE
        binding.option2Button.visibility = View.GONE
        binding.option3Button.visibility = View.GONE
        binding.lumilipad.visibility = View.GONE
        binding.backbtn.visibility = View.GONE
        binding.scoreTextView.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        binding.scoreTextView.text = "Score: $score/${questions.size.toString()}"
        resetProgressBar()

        countDownAndStartGame()

        // Reset ProgressBee and green_bar
    }



    private fun showResults() {
        mediaPlayer.release()
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_score)
        val dialogLayout = dialog.findViewById<View>(R.id.dialogLayout)
        val imageView: ImageView = dialog.findViewById(R.id.bee)
        val scoreText = dialog.findViewById<TextView>(R.id.textViewScoreValue)
        val restartButton = dialog.findViewById<Button>(R.id.buttonRestart)
        val closeButton = dialog.findViewById<Button>(R.id.buttonClose)
        val konfetti = dialog.findViewById<KonfettiView>(R.id.konfettiView)

        val party = Party(
            speed = 0f,
            maxSpeed = 20f,
            damping = 0.9f,
            spread = 500,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def, 0x8ae8fc, 0x8dfcb4),
            emitter = Emitter(duration = 10, TimeUnit.SECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )

        val musicResId: Int
        val gifResId: Int

        if (score > questions.size){
            score = questions.size
        }

        scoreText.text = "$score out of ${questions.size}"

        val totalQuestions = questions.size
        val percentage = (score.toDouble() / totalQuestions.toDouble()) * 100

        when {
            percentage <=  10 -> {
                gifResId = R.drawable.ulan_bee
                dialogLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                musicResId = R.raw.failed_bg
            }
            percentage <= 20 -> {
                gifResId = R.drawable.iyak_bee
                dialogLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                musicResId = R.raw.failed_bg
            }
            percentage <= 30 -> {
                gifResId = R.drawable.scared
                dialogLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                musicResId = R.raw.failed_bg
            }
            percentage <= 40 -> {
                gifResId = R.drawable.stress_bee
                dialogLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                musicResId = R.raw.failed_bg
            }
            percentage <= 50-> {
                gifResId = R.drawable.oa_bee
                dialogLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                musicResId = R.raw.failed_bg
            }
            percentage <= 60 -> {
                gifResId = R.drawable.galit_bee
                dialogLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                musicResId = R.raw.failed_bg
            }
            percentage < 70 -> {
                gifResId = R.drawable.dissapointed_bee
                dialogLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                musicResId = R.raw.failed_bg
            }
            percentage == 100.0 -> {
                gifResId = R.drawable.rainbow_bee
                dialogLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                musicResId = R.raw.passed_bg
                konfetti.start(party)
            }
            else -> {
                // Handle percentage 70% or higher
                gifResId = R.drawable.hehe_bee
                dialogLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow))
                musicResId = R.raw.passed_bg
                konfetti.start(party)
            }
        }


        Glide.with(this)
            .load(gifResId)
            .into(imageView)

        mediaPlayer = MediaPlayer.create(this, musicResId)
        mediaPlayer.isLooping = false
        mediaPlayer.start()

        scoreText.text = "$score out of ${questions.size}"

        restartButton.setOnClickListener {
            mediaPlayer.release()
            dialog.dismiss()
            binding.countdown.text = "..."
            restartQuiz()
        }

        closeButton.setOnClickListener {
            dialog.dismiss()
            mediaPlayer.release()
            val intent = Intent(this@Math, Loading::class.java)
            intent.putExtra("Next Activity", "Main Activity")
            startActivity(intent)
        }

        dialog.show()
    }
    private fun showInstructionsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_instructions)
        val okButton = dialog.findViewById<RadioButton>(R.id.continue_to_game)
        val cancelButton = dialog.findViewById<RadioButton>(R.id.cancel_game)
        val imageView: ImageView = dialog.findViewById(R.id.instruction_logo)

        val instruct = dialog.findViewById<TextView>(R.id.instruction)
        instruct.text = "Answer all the ${questions.size} Questions corectly. \n You must get atleast 70% passing score \n in order to pass this subject. \n Good Luck and enjoy the game!"


        Glide.with(this)
            .load(R.drawable.instruction_bee)
            .into(imageView)

        okButton.setOnClickListener {
            // Add a delay of 1 second before dismissing the dialog
            Handler(Looper.getMainLooper()).postDelayed({
                binding.countdown.visibility = View.VISIBLE
                binding.mathQuestionText.visibility= View.GONE
                binding.option1Button.visibility= View.GONE
                binding.option2Button.visibility= View.GONE
                binding.option3Button.visibility= View.GONE
                binding.progressBar.visibility = View.GONE
                dialog.dismiss()
                countDownAndStartGame()
                // Add any other functionality you need here
            }, 1000) // 1000 milliseconds = 1 second
        }
        cancelButton.setOnClickListener {
            val intent = Intent(this@Math, MainActivity::class.java)
            startActivity(intent)
        }

        dialog.show()
    }
    private fun countDown(){
        mediaPlayer = MediaPlayer.create(this, R.raw.clock)
        mediaPlayer.isLooping = false
        val volume = 2.0f
        mediaPlayer.setVolume(volume, volume)
        mediaPlayer.start()
        val defaultTextSize = binding.countdown.textSize
        Handler().postDelayed({
            binding.countdown.text = "5"
            binding.countdown.animate().apply {
                duration = 1000
                rotationYBy(360f)
                scaleX(1.1f)
                scaleY(1.1f)
            }
        }, 1000)

        Handler().postDelayed({
            binding.countdown.text = "4"
            binding.countdown.animate().apply {
                duration = 1000
                rotationYBy(360f)
                scaleX(1.2f)
                scaleY(1.2f)
            }
        }, 2000)

        Handler().postDelayed({
            binding.countdown.text = "3"
            binding.countdown.animate().apply {
                duration = 1000
                rotationYBy(360f)
                scaleX(1.3f)
                scaleY(1.3f)
            }
        }, 3000)

        Handler().postDelayed({
            binding.countdown.text = "2"
            binding.countdown.animate().apply {
                duration = 1000
                rotationYBy(360f)
                scaleX(1.4f)
                scaleY(1.4f)
            }
        }, 4000)
        Handler().postDelayed({
            binding.countdown.text = "1"
            binding.countdown.animate().apply {
                duration = 1000
                rotationYBy(360f)
                scaleX(1.5f)
                scaleY(1.5f)
            }
        }, 5000)
        Handler().postDelayed({
            binding.countdown.text = "0"
            binding.countdown.animate().apply {
                duration = 1000
                rotationYBy(360f)
                scaleX(1.6f)
                scaleY(1.6f)
            }
        }, 6000)
        Handler().postDelayed({
            binding.countdown.text = "GO!"
            binding.countdown.textSize = 150f
            binding.countdown.animate().apply {
                duration = 1000
                scaleX(1.0f)
                scaleY(1.0f)
            }
        }, 7000)
    }
    private fun countDownAndStartGame() {
        // Show countdown and start game after countdown
        countDown()
        Handler(Looper.getMainLooper()).postDelayed({
            mediaPlayer.release()
            binding.countdown.visibility = View.GONE
            binding.mathQuestionText.visibility = View.VISIBLE
            binding.option1Button.visibility = View.VISIBLE // Show option buttons
            binding.option2Button.visibility = View.VISIBLE
            binding.option3Button.visibility = View.VISIBLE
            binding.lumilipad.visibility = View.VISIBLE
            binding.backbtn.visibility =View.VISIBLE
            binding.scoreTextView.visibility = View.VISIBLE
            binding.progressBar.visibility = View.VISIBLE
            displayQuestion()
            mediaPlayer = MediaPlayer.create(this, R.raw.math_bg)
            mediaPlayer.isLooping = true
            mediaPlayer.start()
            // Additional code to start the game after countdown
        }, 8000) // Adjust the delay as needed
    }
    private fun resetProgressBar() {
        // Reset progress bar to initial progress value
        binding.progressBar.progress = initialProgress
        currentQuestion = 0
        score = 0
    }
    private fun updateProgressBar() {
        // Increment progress for each answered question
        currentQuestion++
        val progress = initialProgress * currentQuestion
        binding.progressBar.progress = progress
    }
}
