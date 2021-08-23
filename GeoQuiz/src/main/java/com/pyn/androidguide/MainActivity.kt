package com.pyn.androidguide

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pyn.androidguide.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val quizViewModel by lazy { ViewModelProvider(this)[QuizViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        if (savedInstanceState != null) {
            quizViewModel.currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
        }
        Log.i(TAG, "onCreate(savedInstanceState: Bundle?) called")

        mBinding.trueButton.setOnClickListener { checkAnswer(true) }
        mBinding.falseButton.setOnClickListener { checkAnswer(false) }

        mBinding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        mBinding.preButton.setOnClickListener {
            quizViewModel.moveToPre()
            updateQuestion()
        }

        mBinding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        updateQuestion()
    }

    /**
     * 更新问题
     */
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        mBinding.questionTextView.setText(questionTextResId)

        setBtnEnabled(!quizViewModel.mQuestionsAnswered?.get(quizViewModel.currentIndex)!!)
    }

    /**
     * Check answer 检测选的答案  里面还需要更新回答正确的题目数，以及已经回答过的题目index
     *
     * @param userAnswer
     */
    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer
        val messageResId = if (userAnswer == correctAnswer) {
            quizViewModel.mTrueAnswerCount++
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        setBtnEnabled(false)
        quizViewModel.mQuestionsAnswered?.set(quizViewModel.currentIndex, true)
        getScoreResult()
    }

    private fun getScoreResult() {
        var isAllAnswered = true
        for (i in 0 until quizViewModel.questionSize) {
            if (!quizViewModel.mQuestionsAnswered?.get(i)!!) {
                isAllAnswered = false
                return
            }
        }
        if (isAllAnswered) {
            Toast.makeText(
                this,
                "${quizViewModel.mTrueAnswerCount * 100 / quizViewModel.questionSize} %",
                Toast.LENGTH_LONG
            ).show()
            mBinding.tvResult.text =
                "评分：${quizViewModel.mTrueAnswerCount * 100 / quizViewModel.questionSize} %"
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "onDestroy() called")
    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")
        savedInstanceState.putInt(KEY_INDEX, quizViewModel.currentIndex)
    }

    // 禁止一题多答，设置button状态
    private fun setBtnEnabled(enabled: Boolean) {
        mBinding.trueButton.isEnabled = enabled
        mBinding.falseButton.isEnabled = enabled
    }
}