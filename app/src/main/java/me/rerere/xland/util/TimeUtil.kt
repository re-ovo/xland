package me.rerere.xland.util

import java.util.*

object TimeUtil {
    /**
     * Parse the time string to Date object.
     *
     * example: "2022-06-18(六)05:10:29"
     */
    @JvmStatic
    fun parseNimingbanTime(
        time: String
    ): Date {
        try {
            val year = time.substring(0, 4).toInt()
            val month = time.substring(5, 7).toInt()
            val day = time.substring(8, 10).toInt()
            val hour = time.substring(13, 15).toInt()
            val minute = time.substring(16, 18).toInt()
            val second = time.substring(19, 21).toInt()
            return Date(
                year - 1900,
                month - 1,
                day,
                hour,
                minute,
                second
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return Date()
        }
    }

    /**
     * 根据时间返回对应的时间信息
     *
     * 例如: 1分钟前, 1小时前, 1天前, 1月前, 1年前...
     */
    @JvmStatic
    fun convertTimeToBetterFormat(time: String): String {
        val date = parseNimingbanTime(time)
        val now = Date()
        val diff = now.time - date.time
        val diffSeconds = diff / 1000
        if(diffSeconds < 0) {
            return "未来"
        }
        return when {
            diffSeconds < 30 -> "刚刚"
            diffSeconds < 60 -> "${diffSeconds}秒前"
            diffSeconds < 60 * 60 -> "${diffSeconds / 60}分钟前"
            diffSeconds < 60 * 60 * 24 -> "${diffSeconds / 60 / 60}小时前"
            diffSeconds < 60 * 60 * 24 * 30 -> "${diffSeconds / 60 / 60 / 24}天前"
            diffSeconds < 60 * 60 * 24 * 30 * 12 -> "${diffSeconds / 60 / 60 / 24 / 30}月前"
            else -> "${diffSeconds / 60 / 60 / 24 / 30 / 12}年前"
        }
    }
}