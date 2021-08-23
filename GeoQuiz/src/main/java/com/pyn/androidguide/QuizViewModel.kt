package com.pyn.androidguide

import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"

class QuizViewModel : ViewModel() {

    // 当前显示的题目的index
    var currentIndex = 0
    // 回答正确的题目数量
    var mTrueAnswerCount = 0

    // 题目库
    private val questionBank = listOf(
        Question(R.string.question_australia, true),
        Question(R.string.question_oceans, true),
        Question(R.string.question_mideast, false),
        Question(R.string.question_africa, false),
        Question(R.string.question_americas, true),
        Question(R.string.question_asia, true)
    )

    // 已经回答过的问题
    var mQuestionsAnswered: BooleanArray? = BooleanArray(questionBank.size)

    // 得到当前题目的答案
    val currentQuestionAnswer: Boolean get() = questionBank[currentIndex].answer

    // 得到当前题目文本
    val currentQuestionText: Int get() = questionBank[currentIndex].textResId

    // 得到当前总题目数量
    val questionSize: Int get() = questionBank.size

    // 移动下一个题目
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    // 上一个题目
    fun moveToPre(){
        currentIndex = (currentIndex + questionBank.size - 1) % questionBank.size
    }

    // test
    init {
        Log.i(TAG, "ViewModel instance created")
    }

    /**
     * On cleared
     * onCleared()函数的调用恰好在ViewModel被销毁之前。适合做一些善后清理工作，比如解绑某个数据源。
     */
    override fun onCleared() {
        super.onCleared()
        Log.i(TAG, "ViewModel instance about to destroyed")
    }
}