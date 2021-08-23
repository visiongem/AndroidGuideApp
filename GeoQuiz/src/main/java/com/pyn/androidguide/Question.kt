package com.pyn.androidguide

import androidx.annotation.StringRes

/**
 * Question 实例类  包括问题文本资源id，以及答案
 *
 * @property textResId
 * @property answer
 * @constructor Create empty Question
 */
data class Question(@StringRes val textResId: Int, val answer: Boolean)
