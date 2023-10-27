package com.renjinzl.zltool.view

import android.app.Activity
import android.content.Context
import android.content.res.Resources.NotFoundException
import android.util.Log
import android.util.TypedValue
import android.view.View
import androidx.annotation.DimenRes
import androidx.fragment.app.Fragment


/**
 * 单位转换: dp -> px
 * @author   renjinzl
 * @since 目前只用于View，FragmentActivity
 * @param value 需要转换的值，单位dp
 * @return Int 转换后value
 */
fun Context.dp2px(value: Int): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value.toFloat(), resources.displayMetrics).toInt()
}
fun View.dp2px(value: Int): Int {
    return context.dp2px(value)
}
fun Activity.dp2px(value: Int): Int {
    return context.dp2px(value)
}
fun Fragment.dp2px(value: Int): Int {
    context?.run {
        return dp2px(value)
    }
    return value
}

/**
 * 单位转换: sp -> px
 * @author   renjinzl
 * @param value 需要转换的值，单位sp
 * @return Int 转换后value
 */
//fun <T> T.sp2px(value: Float): Float {
//    context()?.run {
//        val scaledDensity: Float = resources.displayMetrics.scaledDensity
//        return (scaledDensity * value + 0.5f)
//    }
//    return value
//}

fun Context.sp2px(value: Float): Float {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, resources.displayMetrics)
}
fun View.sp2px(value: Float): Float {
    return context.sp2px(value)
}
fun Activity.sp2px(value: Float): Float {
    return context.sp2px(value)
}
fun Fragment.sp2px(value: Float): Float {
    context?.run {
        return sp2px(value)
    }
    return value

}

/**
 * 失败就返回0
 */
fun Context.sp(@DimenRes id : Int ): Int {
    return try {
        resources.getDimensionPixelOffset(id)
    }catch (e: NotFoundException){
        Log.d("View.dp","没找到资源")
        0
    }
}
/**
 * 失败就返回0
 */
fun View.sp(@DimenRes id : Int ): Int {
    return try {
        resources.getDimensionPixelOffset(id)
    }catch (e: NotFoundException){
        Log.d("View.dp","没找到资源")
        0
    }
}
/**
 * 失败就返回0
 */
fun Activity.sp(@DimenRes id : Int ): Int {
    return try {
        resources.getDimensionPixelOffset(id)
    }catch (e: NotFoundException){
        Log.d("View.dp","没找到资源")
        0
    }
}

/**
 * 失败就返回0
 */
fun View.dp(@DimenRes id : Int ): Int {
    return try {
        resources.getDimensionPixelOffset(id)
    }catch (e: NotFoundException){
        Log.d("View.dp","没找到资源")
        0
    }
}

/**
 * 失败就返回0
 */
fun Context.dp(@DimenRes id : Int ): Int {
    return try {
        resources.getDimensionPixelOffset(id)
    }catch (e: NotFoundException){
        Log.d("View.dp","没找到资源")
        0
    }
}
/**
 * 失败就返回0
 */
fun Activity.dp(@DimenRes id : Int ): Int {
    return try {
        resources.getDimensionPixelOffset(id)
    }catch (e: NotFoundException){
        Log.d("View.dp","没找到资源")
        0
    }
}


/**
 * 获取context
 * @author renjinzl
 * @return Context
 */
fun <T> T.context(): Context? {
    var tempContext: Context? = null
    when (this) {
        is View -> tempContext = context
        is Fragment -> tempContext = context
        is Activity -> tempContext = context
        is Context -> tempContext = this
    }
    return tempContext
}

