package com.renjinzl.zltool.view

import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import java.util.regex.Pattern
import kotlin.random.Random

/**

 * Author   renjinzl
 * Date     2022-05-26 11:31
 * Email    renjinzl@163.com
 * Version  1.0.0   @Update 2022-05-26 11:31   起航
 * Describe 描述
 */
open class Color : android.graphics.Color() {

    companion object {

        fun parseColor(color: String,default : Int = 0x00000000): Int {
            return if (isColor(color)) android.graphics.Color.parseColor(color) else default
        }

        @ColorInt
        fun getRandomColor(): Int = this.run { return argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256)) }

        fun isColor(color: String): Boolean {
            return Pattern.compile("^#[a-fA-F0-9]{6}|^#[a-fA-F0-9]{8}").matcher(color).matches()
        }

        @ColorInt
        fun argb(@IntRange(from = 0, to = 255) alpha: Int, @IntRange(from = 0, to = 255) red: Int, @IntRange(from = 0, to = 255) green: Int, @IntRange(from = 0, to = 255) blue: Int): Int {
            return alpha shl 24 or (red shl 16) or (green shl 8) or blue
        }
    }

}

/**
 * 随机色
 */
val Color.Companion.randomColor : Int get() = android.graphics.Color.argb(255, Random.nextInt(256), Random.nextInt(256), Random.nextInt(256))