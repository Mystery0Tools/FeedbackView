package vip.mystery0.feedbackview.utils

import android.content.Context
import android.util.TypedValue
import java.security.MessageDigest
import java.io.Serializable

fun Context.dip(value: Float): Float = value * resources.displayMetrics.density

fun Context.dp2px(value: Float): Float {
    val metrics = resources.displayMetrics
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, metrics)
}

/**
 * SHA-256 加密
 * @return SHA-256 加密之后的字符串
 */
fun String.sha256(): String {
    val digest = MessageDigest.getInstance("SHA-256")
    val result = digest.digest(this.toByteArray())
    return toHex(result)
}

/**
 * 将指定byte数组转换为16进制字符串
 * @param byteArray 原始数据
 * @return 转换之后数据
 */
private fun toHex(byteArray: ByteArray): String =
    with(StringBuilder()) {
        byteArray.forEach {
            val value = it
            val hex = value.toInt() and (0xFF)
            val hexStr = Integer.toHexString(hex)
            if (hexStr.length == 1)
                append("0").append(hexStr)
            else
                append(hexStr)
        }
        toString()
    }

data class Pair2<T1, T2>(
    var first: T1,
    var second: T2) : Serializable {
    override fun toString(): String = "(first=$first, second=$second)"
}

data class Pair3<T1, T2, T3>(
    var first: T1,
    var second: T2,
    var third: T3) : Serializable {
    override fun toString(): String = "(first=$first, second=$second, third=$third)"
}