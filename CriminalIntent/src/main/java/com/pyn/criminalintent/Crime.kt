package com.pyn.criminalintent

import java.util.*

/**
 * Crime 陋习实体类
 *
 * @property id
 * @property title
 * @property date
 * @property isSolved
 * @constructor Create empty Crime
 */
data class Crime(
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false
)
