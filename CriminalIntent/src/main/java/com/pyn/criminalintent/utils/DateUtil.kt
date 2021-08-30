package com.pyn.criminalintent.utils

import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    /**
     * 自定义返回日期 星期，格式是 「2021年8月20日 周五」
     * Get day and week
     * @return 8月20日 周五
     */
    fun getDayAndWeek(date: Date): String {

        val simpleDateFormat = SimpleDateFormat("yyyy年MM月dd日")
        val dateStr: String = simpleDateFormat.format(date)

        val dayAndWeek = StringBuilder()
        dayAndWeek.append(dateStr)
        dayAndWeek.append("   ")
        dayAndWeek.append(getWeek_ChinaName(date))
        return dayAndWeek.toString()
    }

    /**
     * Get week_china name
     * 获取今天是星期几
     * @return 星期(几)
     */
    fun getWeek_ChinaName(date: Date): String {
        val list = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")
        val calendar: Calendar = Calendar.getInstance()
        calendar.time = date
        var index: Int = calendar.get(Calendar.DAY_OF_WEEK) - 1
        if (index < 0) {
            index = 0
        }
        return list[index]
    }

}