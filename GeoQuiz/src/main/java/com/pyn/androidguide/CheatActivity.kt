package com.pyn.androidguide

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.pyn.androidguide.databinding.ActivityCheatBinding

private const val EXTRA_ANSWER_IS_TRUE = "answer_is_true";

class CheatActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityCheatBinding
    private var answer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityCheatBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        answer = intent.getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false)
        mBinding.btnShowAnswer.setOnClickListener {
            val answerText = when {
                answer -> R.string.true_button
                else -> R.string.false_button
            }
            mBinding.tvAnswer.setText(answerText)

            setAnswerShowResult(true)
        }
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
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }
}