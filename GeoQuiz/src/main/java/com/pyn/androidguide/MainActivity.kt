package com.pyn.androidguide

import android.app.Activity
import android.os.Build
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
const val DEFAULT_CAN_CHEAT_NUM:Int = 3
const val KEY_CHEAT_NUM = "cheat_num"

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding

    private val quizViewModel by lazy { ViewModelProvider(this)[QuizViewModel::class.java] }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                quizViewModel.isCheater =
                    it.data?.getBooleanExtra(EXTRA_ANSWER_SHOW, false) ?: false
                if (quizViewModel.isCheater){
                    // 如果偷看了答案
                    quizViewModel.cheatNum ++
                    updateCheatNum()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        if (savedInstanceState != null) {
            quizViewModel.currentIndex = savedInstanceState.getInt(KEY_INDEX, 0)
            quizViewModel.cheatNum = savedInstanceState.getInt(KEY_CHEAT_NUM, 0)
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

        updateCheatNum()

        mBinding.tvCompileVersion.text = "API LEVER = ${Build.VERSION.SDK_INT}"
    }


    /**
     * 更新问题
     */
    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        mBinding.questionTextView.setText(questionTextResId)

        setBtnEnabled(!quizViewModel.mQuestionsAnswered?.get(quizViewModel.currentIndex)!!)
    }

    private fun updateCheatNum(){
        var canCheatNum = DEFAULT_CAN_CHEAT_NUM - quizViewModel.cheatNum
        mBinding.tvCheatNum.text = "还可以偷看答案 $canCheatNum 次"
        if (canCheatNum == 0){
            mBinding.btnCheat.isEnabled = false
        }
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
        // 重置一下是否偷看了答案，此题回答过了，一来不可重复回答，二来解决回答下个问题时此参数还是原来的
        quizViewModel.isCheater = false
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
        savedInstanceState.putInt(KEY_CHEAT_NUM, quizViewModel.cheatNum)
    }

    // 禁止一题多答，设置button状态
    private fun setBtnEnabled(enabled: Boolean) {
        mBinding.trueButton.isEnabled = enabled
        mBinding.falseButton.isEnabled = enabled
    }
}