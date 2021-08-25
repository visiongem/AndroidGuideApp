package com.pyn.androidguide

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pyn.androidguide.databinding.ActivityMainBinding

private const val TAG = "MainActivity"
private const val KEY_INDEX = "index"
const val EXTRA_ANSWER_SHOW = "extra_answer_show"

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val quizViewModel by lazy { ViewModelProvider(this)[QuizViewModel::class.java] }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                quizViewModel.isCheater =
                    it.data?.getBooleanExtra(EXTRA_ANSWER_SHOW, false) ?: false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        if (savedInstanceState != null) {
            quizViewModel.currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
        }
        Log.i(TAG, "onCreate(savedInstanceState: Bundle?) called")

        // 回答问题
        mBinding.trueButton.setOnClickListener { checkAnswer(true) }
        mBinding.falseButton.setOnClickListener { checkAnswer(false) }

        // 下个问题
        mBinding.nextButton.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        // 上个问题
        mBinding.preButton.setOnClickListener {
            quizViewModel.moveToPre()
            updateQuestion()
        }

        // 点文字也是下个问题
        mBinding.questionTextView.setOnClickListener {
            quizViewModel.moveToNext()
            updateQuestion()
        }

        // 去偷看答案
        mBinding.btnCheat.setOnClickListener {
            val answer = quizViewModel.currentQuestionAnswer
            startForResult.launch(CheatActivity.newIntent(this, answer))
        }

        // 更新问题
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
        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgment_toast
            userAnswer == correctAnswer -> {
                quizViewModel.mTrueAnswerCount++
                R.string.correct_toast
            }
            else ->
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