package com.renjinzl.zltool.view

import androidx.annotation.Size
import java.math.BigDecimal

/**
 * name
 * author   renjinzl

 * date     2022-09-27 14:50
 *
 * version  1.0.0   update 2022-09-27 14:50   起航
 * @since 各种计算

 */


/**
 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
 * 这是一个父方法，需要各种类型在上面添加（上面已经有一个Double的例子）
 *
 * @param number1 被除数
 * @param number2 除数
 * @param scale   表示表示需要精确到小数点以后几位,最大5位数
 */
private fun divide(number1: BigDecimal, number2: BigDecimal, @Size(min = 0, max = 5) scale: Int): BigDecimal {
    return number1.divide(number2, if (scale <= 0) 0 else if (scale > 5) 5 else scale, BigDecimal.ROUND_HALF_UP)
}

/**
 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
 *
 * this number 被除数
 * @param number 除数
 * @param scale   表示表示需要精确到小数点以后几位,最大5位数
 * @return 两个参数的商
 */
fun Int.divide(number: Int, @Size(min = 0, max = 5) scale: Int): Float {
    return divide(BigDecimal(this), BigDecimal(number), scale).toFloat()
}

/**
 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指定精度，以后的数字四舍五入。
 *
 * this number 被除数
 * @param number 除数
 * @param scale   表示表示需要精确到小数点以后几位,最大5位数
 * @return 两个参数的商
 */
fun Long.divide(number: Int, @Size(min = 0, max = 5) scale: Int): Float {
    return divide(BigDecimal(this), BigDecimal(number), scale).toFloat()
}

/**
 * 提供精确加法计算的add方法
 *
 * this value 被加数
 * @param value 加数
 * @return 两个参数的和
 */
fun Double.add(value: Double): Double {
    return BigDecimal(this).add(BigDecimal(value)).toDouble()
}

/**
 * 提供精确乘法运算的mul方法
 *
 * this value1 被乘数
 * @param value 乘数
 * @return 两个参数的积
 */
fun Double.multiply(value: Double): Double {
    return BigDecimal(this).multiply(BigDecimal(value)).toDouble()
}