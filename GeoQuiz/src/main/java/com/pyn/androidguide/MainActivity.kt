package com.pyn.androidguide

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
private const val KEY_QUESTION_ANSWERED = "answered"
private const val KEY_TRUE_ANSWER_COUNT = "true_answer_count"

class MainActivity : AppCompatActivity() {

    private lateinit var trueButton: Button
    private lateinit var falseButton: Button
    private lateinit var nextButton: ImageButton
    private lateinit var preButton: ImageButton
    private lateinit var questionTextView: TextView

    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )
    private var currentIndex = 0

    private var mQuestionsAnswered: BooleanArray? = BooleanArray(questionBank.size)

    private var mTrueAnswerCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate(savedInstanceState: Bundle?) called")
        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            mQuestionsAnswered = savedInstanceState.getBooleanArray(KEY_QUESTION_ANSWERED)
            mTrueAnswerCount = savedInstanceState.getInt(KEY_TRUE_ANSWER_COUNT)
        }

        trueButton = findViewById(R.id.true_button)
        falseButton = findViewById(R.id.false_button)
        nextButton = findViewById(R.id.next_button)
        preButton = findViewById(R.id.pre_button)
        questionTextView = findViewById(R.id.question_text_view)

        trueButton.setOnClickListener { checkAnswer(true) }
        falseButton.setOnClickListener { checkAnswer(false) }
        nextButton.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        preButton.setOnClickListener {
            currentIndex = (currentIndex + questionBank.size - 1) % questionBank.size
            updateQuestion()
        }

        questionTextView.setOnClickListener {
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()
    }

    /**
     * 更新问题
     */
    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        questionTextView.setText(questionTextResId)

        setBtnEnabled(!mQuestionsAnswered?.get(currentIndex)!!)
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer
        val messageResId = if (userAnswer == correctAnswer) {
            mTrueAnswerCount++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        setBtnEnabled(false)
        mQuestionsAnswered?.set(currentIndex, true)
        getScoreResult()
    }

    private fun getScoreResult() {
        var isAllAnswered = true
        for (i in questionBank.indices) {
            if (!mQuestionsAnswered?.get(i)!!) {
                isAllAnswered = false
                return
            }
        }
        if (isAllAnswered) {
            Toast.makeText(
                this,
                "${mTrueAnswerCount * 100 / questionBank.size} %",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, currentIndex)
        savedInstanceState.putBooleanArray(KEY_QUESTION_ANSWERED, mQuestionsAnswered)
        savedInstanceState.putInt(KEY_TRUE_ANSWER_COUNT, mTrueAnswerCount)
    }

    private fun setBtnEnabled(enabled: Boolean) {
        trueButton.isEnabled = enabled
        falseButton.isEnabled = enabled
    }
}