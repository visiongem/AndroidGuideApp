package com.pyn.androidguide

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.pyn.androidguide.databinding.ActivityCheatBinding

private const val EXTRA_ANSWER = "extra_answer"
private const val IS_SHOW_ANSWER = "is_show_answer"

class CheatActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityCheatBinding
    private var answer = false

    private val cheatViewModel by lazy { ViewModelProvider(this)[CheatViewModel::class.java] }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        if (savedInstanceState != null) {
            cheatViewModel.isShowAnswer = savedInstanceState.getBoolean(IS_SHOW_ANSWER, false)
        }

        answer = intent.getBooleanExtra(EXTRA_ANSWER, false)
        mBinding.btnShowAnswer.setOnClickListener {
            cheatViewModel.isShowAnswer = true
            val answerText = when {
                answer -> R.string.true_button
                else -> R.string.false_button
            }
            mBinding.tvAnswer.setText(answerText)
        }
    }

    // 每次返回的时候，把结果带回去
    override fun onBackPressed() {
        setAnswerShowResult(cheatViewModel.isShowAnswer)
        super.onBackPressed()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(IS_SHOW_ANSWER, cheatViewModel.isShowAnswer)
    }

    /**
     * Set answer show result
     * 给第一个activity返回是否偷看了答案
     *
     * @param isAnswerShown
     */
    fun setAnswerShowResult(isAnswerShown: Boolean) {
        val data = Intent().apply { putExtra(EXTRA_ANSWER_SHOW, isAnswerShown) }
        setResult(Activity.RESULT_OK, data)
    }

    companion object {
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER, answerIsTrue)
            }
        }
    }
}