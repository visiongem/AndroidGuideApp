package com.pyn.criminalintent.bean

import java.util.*

/**
 * Crime 陋习实体类
 *
 * @property id
 * @property title
 * @property date 日期
 * @property isSolved 是否解决
 * @property requiresPolice 是否需警方介入
 * @constructor Create empty Crime
 */
data class Crime(
    val id: UUID = UUID.randomUUID(),
    var title: String = "",
    var date: Date = Date(),
    var isSolved: Boolean = false,
    var requiresPolice: Boolean = false
)
