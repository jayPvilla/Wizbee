package com.example.projectwizbee

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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.example.projectwizbee.databinding.ActivityBotanyBinding
import com.bumptech.glide.Glide
import com.example.projectwizbee.R.color.red
import com.example.projectwizbee.R.color.yellow
import kotlinx.coroutines.delay
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit


class Botany : AppCompatActivity() {
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var binding: ActivityBotanyBinding
    private val questions = arrayOf(
        "Which of the Following is a native Philippine tree commonly known as Pili or Canarium ovatum?",
        "Which of the following is the National Flower, sampaguita also known as Jasminum sambac?",
        "Select the picture of Averrhoa bilimbi commonly known as kamias.",
        "The following are pictures of native trees/flowers in the philippines EXCEPT:",
        "It is known as Refflesia samiosa, the largest flower in the world.",
        "It is an endemic plant in the Philippines known as Moringa Oleifera and growns in almost every place where it is planted.",
        "The leaves of this tree is like guava leaves, it can be used to wash wounds or applied to open cuts commonly known as Alagao",
        "This vegetable is known as Water Spinach in english and common vegetable ingredients used in filipino dishes..",
        "It is widely known as the National Tree of the Philippines with a scientific name of Pterocarpus indicus.",
        "Which of the following plants is mentioned in the song 'Bahay Kubo?'")

    private val optionImages = arrayOf(
        arrayOf(R.drawable.pili, R.drawable.banaba, R.drawable.narra),
        arrayOf(R.drawable.katuray, R.drawable.sampaguita, R.drawable.alagao),
        arrayOf(R.drawable.ipilipil, R.drawable.malunggay, R.drawable.camias),
        arrayOf(R.drawable.chinese_bamboo, R.drawable.pili, R.drawable.refflesia),
        arrayOf(R.drawable.gumamela, R.drawable.refflesia, R.drawable.kalachuchi),
        arrayOf(R.drawable.santan2, R.drawable.dao, R.drawable.malunggay),
        arrayOf(R.drawable.alagao, R.drawable.narra, R.drawable.mahogany),
        arrayOf(R.drawable.camias, R.drawable.kangkong, R.drawable.ipilipil),
        arrayOf(R.drawable.mahogany, R.drawable.dao, R.drawable.narra),
        arrayOf(R.drawable.linga, R.drawable.malunggay, R.drawable.kangkong))

    private val correctAnswers = arrayOf(0, 1, 2, 0, 1, 2, 0, 1, 2, 1)

    private var currentQuestionIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBotanyBinding.inflate(layoutInflater)
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



        // Assuming you have a boolean variable isAlternateMusic set based on some condition

        mediaPlayer = MediaPlayer.create(this, R.raw.botany_bg)
        mediaPlayer.isLooping = true
        mediaPlayer.start()

        showInstructionsDialog()

        displayQuestion()

        binding.option1Button.setOnClickListener {
            checkAnswer(0)
        }
        binding.option2Button.setOnClickListener {
            checkAnswer(1)
        }
        binding.option3Button.setOnClickListener {
            checkAnswer(2)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }
    private fun correctButtonColors(botany_buttonIndex: Int){
        when(botany_buttonIndex){
            0 -> binding.option1Button.setImageResource(R.drawable.green_square)
            1 -> binding.option2Button.setImageResource(R.drawable.green_square)
            2 -> binding.option3Button.setImageResource(R.drawable.green_square)
        }
    }
    private fun wrongButtonColors(botany_buttonIndex: Int){
        when(botany_buttonIndex){
            0 -> binding.option1Button.setImageResource(R.drawable.red_square)
            1 -> binding.option2Button.setImageResource(R.drawable.red_square)
            2 -> binding.option3Button.setImageResource(R.drawable.red_square)
        }
    }
    private fun displayQuestion() {
        binding.option1Button.isEnabled = true
        binding.option2Button.isEnabled = true
        binding.option3Button.isEnabled = true
        binding.botanyQuestionText.text = questions[currentQuestionIndex]
        val currentOptions = optionImages[currentQuestionIndex]

        binding.option1Button.setImageResource(currentOptions[0])
        binding.option2Button.setImageResource(currentOptions[1])
        binding.option3Button.setImageResource(currentOptions[2])

    }
    private fun checkAnswer(selectedAnswerIndex: Int){
        binding.option1Button.isEnabled = false
        binding.option2Button.isEnabled = false
        binding.option3Button.isEnabled = false
        val correctAnswerIndex = correctAnswers[currentQuestionIndex]

        if (selectedAnswerIndex == correctAnswerIndex){
            score++
            correctButtonColors(selectedAnswerIndex)
        } else {
            wrongButtonColors(selectedAnswerIndex)
            correctButtonColors(correctAnswerIndex)
        }
        if (currentQuestionIndex < questions.size - 1){
            currentQuestionIndex++
            binding.botanyQuestionText.postDelayed({displayQuestion()}, 1000)
        } else {
            showResults()
        }
    }



    private fun restartQuiz(){
        mediaPlayer = MediaPlayer.create(this, R.raw.botany_bg)
        mediaPlayer.isLooping = false
        mediaPlayer.start()
        currentQuestionIndex = 0
        score = 0
        displayQuestion()
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

        val musicResId: Int
        val gifResId: Int

        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 3, TimeUnit.SECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )

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
            percentage <= 70 -> {
                gifResId = R.drawable.dissapointed_bee
                dialogLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
                musicResId = R.raw.failed_bg
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
            restartQuiz()
        }

        closeButton.setOnClickListener {
            dialog.dismiss()
            mediaPlayer.release()
            val intent = Intent(this@Botany, MainActivity::class.java)
            intent.putExtra("SCORE", score.toString())
            startActivity(intent)
        }

        dialog.show()
    }
    private fun showInstructionsDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_instructions)
        val okButton = dialog.findViewById<RadioButton>(R.id.continue_to_game)
        val imageView: ImageView = dialog.findViewById(R.id.instruction_logo)
        val cancelButton = dialog.findViewById<RadioButton>(R.id.cancel_game)

        val instruct = dialog.findViewById<TextView>(R.id.instruction)
        instruct.text = "Answer all the ${questions.size} Questions corectly. \n You must get atleast 70% passing score \n in order to pass this subject. \n Good Luck and enjoy the game!"

        Glide.with(this)
            .load(R.drawable.instruction_bee)
            .into(imageView)

        okButton.setOnClickListener {
            // Add a delay of 1 second before dismissing the dialog
            Handler(Looper.getMainLooper()).postDelayed({
                dialog.dismiss()
                // Add any other functionality you need here
            }, 1000) // 1000 milliseconds = 1 second
        }
        cancelButton.setOnClickListener {
            mediaPlayer.release()
            val intent = Intent(this@Botany, MainActivity::class.java)
            startActivity(intent)
        }

        dialog.show()
    }


}
