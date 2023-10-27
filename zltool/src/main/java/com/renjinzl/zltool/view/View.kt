package com.renjinzl.zltool.view

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator

import android.graphics.Outline
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.view.LayoutInflater

import android.view.ViewOutlineProvider
import android.widget.FrameLayout
import android.widget.PopupWindow
import androidx.annotation.*
import androidx.core.content.ContextCompat
import com.renjinzl.zltool.R

/**
 * Author   renjinzl
 * Date     2022-06-08 17:53
 * Email    renjinzl@163.com
 * Version  1.0.0   @Update 2022-06-08 17:53   起航
 * Describe View 扩展
 */


/**
 * 根view
 */
val View.rootLayout: FrameLayout get() = (context as Activity).window.decorView.findViewById(android.R.id.content)

val View.layoutInflater: LayoutInflater get() = (context as Activity).layoutInflater

/**
 * 父控件
 * @since 孤儿view的别用 要报错
 */
val View.parentView: View get() = parent as View

fun View.setTitle(title: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        accessibilityPaneTitle = title
    } else {
        setTag(R.id.tag_accessibility_pane_title, title)
    }
}

/**
 *
 */
fun View.setVisibility(boolean: Boolean) {
    visibility = if (boolean) View.VISIBLE else View.GONE
}

/**
 * 子view快速点击
 */
fun ViewGroup.inflate(@LayoutRes id: Int) {
    LayoutInflater.from(context).inflate(id, this, true)
}

/**
 * 子view快速点击
 */
fun <T : View> T.click(@IdRes id: Int, block: View.(T) -> Unit): T {
    findViewById<T>(id)?.run { setOnClickListener { block(this) } }
    return this
}

/**
 * 快速点击
 */
fun <T : View> T.click(block: T.(T) -> Unit): T {
    setOnClickListener { block(this) }
    return this
}

/**
 * 上面圆角
 * @param radius int
 */
fun View.roundTop(radius: Int): View {
    return round(radius, 2)
}

/**
 * 圆角
 * @param radius int
 */
fun View.round(radius: Int): View {
    return round(radius, 0)
}

/**
 * 圆角
 * @param radius int
 * @param radiusSide 0 all 1 left 2 top 3 right 4 bottom
 */
private fun View.round(radius: Int, @Size(min = 0, max = 4) radiusSide: Int): View {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val w = view.width
            val h = view.height
            if (w == 0 || h == 0) {
                return
            }

            if (radiusSide > 0) {
                var left = 0
                var top = 0
                var right = w
                var bottom = h
//                when (radiusSide) {
//                    1 -> {
//                        right += radius
//                    }
//                    2 -> {
//                        bottom += radius
//                    }
//                    3 -> {
//                        left -= radius
//                    }
//                    4 -> {
//                        top -= radius
//                    }
//                }
                when (radiusSide) {
                    1 -> right += radius
                    2 -> bottom += radius
                    3 -> left -= radius
                    4 -> top -= radius
                }
                outline.setRoundRect(left, top, right, bottom, radius.toFloat())
                return
            } else {
                val top = 0
                val left = 0
                if (radius <= 0) {
                    outline.setRect(left, top, w, h)
                } else {
                    outline.setRoundRect(left, top, w, h, radius.toFloat())
                }
            }
        }
    }

    //设置圆角裁切
    clipToOutline = true
    invalidate()
    return this
}

/**
 * 圆角
 *
 * 1 2

 * 3 4
 * @param radiusSide 0 all 1 left 2 top 3 right 4 bottom
 */
 fun View.round(radius1: Int, radius2: Int, radius3: Int, radius4: Int): View {

    fun calculateBounds(): RectF {
        // 没有处理Padding的逻辑
        return RectF(0f, 0f, width.toFloat(), height.toFloat())
    }

    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val w = view.width
            val h = view.height
            if (w == 0 || h == 0) {
                return
            }


            var left = 0
            var top = 0
            var right = w
            var bottom = h

            val path = Path()
            path.addRoundRect(calculateBounds(), floatArrayOf(radius1.toFloat(), radius1.toFloat(), radius2.toFloat(), radius2.toFloat(), radius3.toFloat(), radius3.toFloat(), radius4.toFloat(), radius4.toFloat()), Path.Direction.CCW)

            //不支持2阶的曲线
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                outline.setPath(path)
            }

//            if (radius1 > 0) {
//                right += radius1
//            }
//            if (radius2 > 0) {
//                bottom += radius2
//            }
//            if (radius3 > 0) {
//                left -= radius3
//            }
//            if (radius4 > 0) {
//                top -= radius4
//            }

//            outline.setRoundRect(left, top, right, bottom,40f)

            return
        }
    }

    //设置圆角裁切
    clipToOutline = true
    invalidate()
    return this
}

/**
 * 设置高级背景
 * @param radius 圆角
 * @param color 填充颜色
 * @param strokeWith 边框宽带
 * @param strokeColor 边框颜色
 */
fun View.backgroundColor(radius: Float, @ColorInt color: Int, strokeWith: Int, @ColorInt strokeColor: Int) {
    background = GradientDrawable().apply {
        cornerRadius = radius
        if (strokeColor != 0) {
            setStroke(strokeWith, strokeColor)
        }
        setColor(color)
    }
}

/**
 * 设置高级背景
 * @param radius 圆角
 * @param strokeWith 边框宽带
 * @param strokeColor 边框颜色
 */
fun View.backgroundColor(radius: Float, strokeWith: Int, @ColorInt strokeColor: Int) {
    background = GradientDrawable().apply {
        cornerRadius = radius
        if (strokeColor != 0) {
            setStroke(strokeWith, strokeColor)
        }
        setColor(Color.parseColor("#00000000"))
    }
}

/**
 * 设置高级背景
 * @param radius 圆角
 * @param color 填充颜色
 */
fun View.backgroundColor(radius: Float, @ColorInt color: Int) {
    background = GradientDrawable().apply {
        cornerRadius = radius
        setColor(color)
    }
}

fun View.backgroundTint(@ColorInt color: Int) {
    backgroundTintList = ColorStateList.valueOf(color)
}

/**
 * 设置高级背景 左右渐变
 * @param radius 圆角
 * @param color 填充颜色 vararg
 */
fun View.backgroundColorLeftRight(radius: Float, @ColorInt vararg color: Int) {
    background = GradientDrawable().apply {
        colors = intArrayOf(*color)
        orientation = GradientDrawable.Orientation.LEFT_RIGHT
        cornerRadius = radius
    }
}

/**
 * 获取颜色
 */
@ColorInt
fun View.getColor(@ColorRes id: Int): Int {
    return ContextCompat.getColor(context, id)
}

//------------------PopupWindow and Dialog---------------------------
fun <T : View> T.dismiss() {
    parent?.apply {
        if (this is PopupWindow) {
            (this as PopupWindow).dismiss()
        }
        if (this is Dialog) {
            (this as Dialog).dismiss()
        }
    }
}

fun <T : View> T.isShowing(): Boolean {
    parent?.apply {
        if (this is PopupWindow) {
            return (this as PopupWindow).isShowing
        }
        if (this is Dialog) {
            return (this as Dialog).isShowing
        }
    }
    return false
}
//------------------动画---------------------------
/**
 * 自身
 */
private var changeBounds = ChangeBounds()

/**
 * 状态
 */
private var fade = Fade()

/**
 * 空间 转
 */
private var changeTransform = ChangeTransform()

/**
 * 视觉
 */
private var changeClipBounds = ChangeClipBounds()

fun View.changeBoundsAndFade() {
    this.parent?.run {
        if (this is ViewGroup) {
            val set = TransitionSet()
            set.interpolator = AccelerateDecelerateInterpolator()
            set.duration = 300
            set.addTransition(changeBounds)
            set.addTransition(fade)
            set.ordering = TransitionSet.ORDERING_TOGETHER
            TransitionManager.beginDelayedTransition(this, set)
        }
    }
}

fun View.changeBoundsAndTransform() {
    this.changeBoundsAndTransform(300)
}

fun View.changeBoundsAndTransform(duration: Int) {
    this.parent?.run {
        if (this is ViewGroup) {
            val set = TransitionSet()
            set.interpolator = AccelerateDecelerateInterpolator()
            set.duration = duration.toLong()
            set.addTransition(changeBounds)
            set.addTransition(changeTransform)
            set.ordering = TransitionSet.ORDERING_TOGETHER
            TransitionManager.beginDelayedTransition(this, set)
        }
    }
}

/**
 * 大小
 */
fun View.changeBounds() {
    this.parent?.run {
        if (this is ViewGroup) {
            changeBounds.duration = 300
            changeBounds.interpolator = AccelerateDecelerateInterpolator()
            TransitionManager.beginDelayedTransition(this, changeBounds)
        }
    }
}

/**
 * 移动
 */
fun View.changeTransform() {
    this.parent?.run {
        if (this is ViewGroup) {
            changeTransform.duration = 300
            changeTransform.interpolator = DecelerateInterpolator()
            TransitionManager.beginDelayedTransition(this, changeTransform)
        }
    }
}

/**
 * 视线
 */
fun View.changeClipBounds() {
    this.parent?.run {
        if (this is ViewGroup) {
            changeClipBounds.interpolator = DecelerateInterpolator()
            TransitionManager.beginDelayedTransition(this, changeClipBounds)
        }
    }
}