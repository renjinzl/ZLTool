package com.renjinzl.zltool.view

import android.content.Context
import android.graphics.Color
import android.view.*
import android.view.animation.*
import android.widget.PopupWindow
import androidx.core.widget.PopupWindowCompat
import com.renjinzl.zltool.R
import java.lang.Exception

/**

 * Author   renjinzl
 * Date     2022-06-08 09:59
 * Email    renjinzl@163.com
 * Version  1.0.0   @Update 2022-06-08 09:59   起航
 * Describe Dialog
 */
open class Dialog(context: Context, styleRes: Int) : android.app.Dialog(context, styleRes) {

    constructor(context: Context) : this(context, R.style.dialog_style)

    var showAnimation: Animation? = null

    var dismissAnimation: Animation? = null

    private var isAnimating = false

    private var contentView: View? = null

    init {
        window?.apply {
            val params: WindowManager.LayoutParams = attributes
//        clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)//弹出后背景变暗
            decorView.setPadding(0, 0, 0, 0)
            setType(WindowManager.LayoutParams.TYPE_APPLICATION_PANEL)
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            attributes = params

//            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
        //            WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
            setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    override fun setContentView(view: View) {
        super.setContentView(view)
        contentView = view
    }

    override fun setContentView(view: View, params: ViewGroup.LayoutParams?) {
        super.setContentView(view, params)
        contentView = view
    }

    fun setPadding(padding : Int){
        window?.apply {
            decorView.setPadding(padding, 0, padding, 0)
        }
    }

    /**
     * 是否有动画效果
     */
    open fun isAnimation(): Boolean {
        return showAnimation != null && dismissAnimation != null
    }

    override fun show() {
        super.show()
        if (isAnimation() && contentView != null) {
            showAnimation?.setAnimationListener(showAnimationListener)
            contentView?.startAnimation(showAnimation)
        }
    }

    override fun dismiss() {
        if (this.isAnimation()) {
            if (!isAnimating && contentView != null) {
                dismissAnimation?.setAnimationListener(closeAnimationListener)
                contentView?.startAnimation(dismissAnimation)
            }
        } else {
            super.dismiss()
        }
    }

    /**
     * 显示动画监听器
     */
    private val showAnimationListener: Animation.AnimationListener =
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                isAnimating = true
            }

            override fun onAnimationEnd(animation: Animation) {
                isAnimating = false
            }

            override fun onAnimationRepeat(animation: Animation) {}
        }

    /**
     * 关闭动画监听器
     */
    private val closeAnimationListener: Animation.AnimationListener =
        object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                isAnimating = true
            }

            override fun onAnimationEnd(animation: Animation) {
                isAnimating = false
                contentView?.post {
                    try {
                        super@Dialog.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onAnimationRepeat(animation: Animation) {}
        }
}

//-----------------------------------expand-----------------------------

fun View.dismiss(){
    getTag(R.id.dialog).let {
        if (it is Dialog){
            it.dismiss()
        }
        if (it is PopupWindow){
            it.dismiss()
        }
    }
}

/**
 * view 装入 Dialog
 */
fun View.show() {
    show(null)
}

/**
 * view 装入 Dialog
 */
fun <T:View>T.show(block: (T.(T, Dialog) -> Unit)?) {
    val dialog = Dialog(context)
    setTag(R.id.dialog,dialog)
    dialog.setContentView(this)
    dialog.setPadding(dp2px(10))
    block?.let { block(this, dialog) }
    dialog.show()
}

/**
 * 全屏
 */
fun <T : View> T.showFullScreen(block: (T.(Dialog) -> Unit)?) : T {
    val dialog = Dialog(context)
    setTag(R.id.dialog,dialog)

    dialog.window?.apply {
        setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//        setBackgroundColor(Color.parseColor("#ffffff"))
//        //去除半透明阴影
//        val layoutParams : WindowManager.LayoutParams  = attributes
//        layoutParams.dimAmount = 1.0f
//        attributes = layoutParams
    }
    dialog.setContentView(this)
    block?.let { block(this, dialog) }
    dialog.show()
    return this
}


/**
 * 底部上滑
 */
fun <T : View> T.showAnimationFromBottom(block: (T.(T, Dialog) -> Unit)?) : T {

    if (getTag(R.id.dialog) != null && getTag(R.id.dialog) is Dialog) {
        (getTag(R.id.dialog) as Dialog).apply {
            show()
        }
    } else {
        val dialog = Dialog(context)
        setTag(R.id.dialog,dialog)
        dialog.window?.apply {
            attributes.gravity = Gravity.BOTTOM
        }
        dialog.showAnimation = getAnimationBottomSlideUp()
        dialog.dismissAnimation = getAnimationUpSlideBottom()
        this.roundTop(dp2px(10))
        dialog.setContentView(this)
        block?.let { block(this, dialog) }
        dialog.show()
    }
    return this
}
fun <T : View> T.showAnimationFromBottom() : T {
    return showAnimationFromBottom(null)
}

/**
 * 底部上滑
 */
fun <T : View> T.showAnimationFromRight(block: (T.(T, Dialog) -> Unit)?) : T {

    if (getTag(R.id.dialog)!=null && getTag(R.id.dialog) is Dialog){
        (getTag(R.id.dialog) as Dialog).apply{
            show()
        }
    }else{
        val dialog = Dialog(context)
        setTag(R.id.dialog,dialog)
        dialog.window?.apply {
            attributes.gravity = Gravity.BOTTOM
        }
        dialog.showAnimation = getAnimationRightSlideLeft()
        dialog.dismissAnimation = getAnimationLeftSlideRight()
        dialog.setContentView(this)
        block?.let { block(this, dialog) }
        dialog.show()
    }

    return this
}

/**
 * 中心放大
 */
fun <T : View> T.showAnimationFromCenter(block: (T.(T, Dialog) -> Unit)?) : T{
    val dialog = Dialog(context)
    setTag(R.id.dialog,dialog)
    dialog.showAnimation = getAnimationScale(0.5f,0.5f,true,true)
    dialog.dismissAnimation = getAnimationScale(0.5f,0.5f,true,false)
    dialog.setContentView(this)
    block?.let { block(this, dialog) }
    dialog.show()
    return this
}

fun <T : View> T.showAnimationFromCenter() : T{
    val dialog = Dialog(context)
    setTag(R.id.dialog,dialog)
    dialog.showAnimation = getAnimationScale(0.5f,0.5f,true,true)
    dialog.dismissAnimation = getAnimationScale(0.5f,0.5f,true,false)
    dialog.setContentView(this)
    dialog.show()
    return this
}

/**
 * 空白处点击不关闭
 */
fun Dialog.blankCannotDismiss(){
    setCanceledOnTouchOutside(true)
    setCancelable(false)
}



/**
 * @return 从下往上升起动画
 */
private fun getAnimationBottomSlideUp(): Animation {
    val translateAnimation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f
    )
    val alphaAnimation = AlphaAnimation(0f, 1f)
    val animationSet = AnimationSet(true)
    animationSet.addAnimation(translateAnimation)
    animationSet.addAnimation(alphaAnimation)
    animationSet.interpolator = DecelerateInterpolator()
    animationSet.duration = 200
    animationSet.fillAfter = true
    return animationSet
}

/**
 * @return 从左往右动画
 */
fun getAnimationRightSlideLeft(): Animation {
    val translateAnimation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
    )
    val alphaAnimation = AlphaAnimation(0f, 1f)
    val animationSet = AnimationSet(true)
    animationSet.addAnimation(translateAnimation)
    animationSet.addAnimation(alphaAnimation)
    animationSet.interpolator = DecelerateInterpolator()
    animationSet.duration = 200
    animationSet.fillAfter = true
    return animationSet
}

/**
 * @return 从上往下的降落动画
 */
private fun getAnimationUpSlideBottom(): Animation {
    val translateAnimation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f
    )
    val alphaAnimation = AlphaAnimation(1f, 0f)
    val animationSet = AnimationSet(true)
    animationSet.addAnimation(translateAnimation)
    animationSet.addAnimation(alphaAnimation)
    animationSet.interpolator = DecelerateInterpolator()
    animationSet.duration = 200
    animationSet.fillAfter = true
    return animationSet
}
/**
 * @return 从左往右的退出动画
 */
private fun getAnimationLeftSlideRight(): Animation {
    val translateAnimation = TranslateAnimation(
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f,
        Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f
    )
    val alphaAnimation = AlphaAnimation(1f, 0f)
    val animationSet = AnimationSet(true)
    animationSet.addAnimation(translateAnimation)
    animationSet.addAnimation(alphaAnimation)
    animationSet.interpolator = DecelerateInterpolator()
    animationSet.duration = 200
    animationSet.fillAfter = true
    return animationSet
}

/**
 * 缩放动画
 *
 * @param pivotXValue x参考
 * @param pivotYValue y参考
 * @param gradients   是否渐变
 * @return
 */
fun getAnimationScale(pivotXValue: Float,pivotYValue: Float,gradients: Boolean,enter: Boolean): Animation {
    return if (enter) {
        getAnimationScale(pivotXValue, pivotYValue, 0f, 1f, 0f, 1f, gradients, 0f, 1f)
    } else {
        getAnimationScale(pivotXValue, pivotYValue, 1f, 0.5f, 1f, 0.5f, gradients, 1f, 0f)
    }
}

fun getAnimationScale(pivotXValue: Float, pivotYValue: Float, fromX: Float, toX: Float, fromY: Float, toY: Float, gradients: Boolean, fromAlpha: Float, toAlpha: Float): Animation {
    val scaleAnimation = ScaleAnimation(fromX, toX, fromY, toY, Animation.RELATIVE_TO_SELF, pivotXValue, Animation.RELATIVE_TO_SELF, pivotYValue)
    val animationSet = AnimationSet(true)
    animationSet.addAnimation(scaleAnimation)
    val alphaAnimation = AlphaAnimation(fromAlpha, toAlpha)
    animationSet.addAnimation(alphaAnimation)
    if (gradients) {
        if (toAlpha == 1f) {
//            animationSet.interpolator = OvershootInterpolator()
            animationSet.interpolator = PathInterpolator(.02f, 1.12f, .22f, .96f)
        } else {
//            animationSet.interpolator = AnticipateInterpolator()
            animationSet.interpolator = PathInterpolator(1f, -0.02f, 1f, .02f)
        }
//        animationSet.interpolator = DecelerateInterpolator()
        //https://cubic-bezier.com/#.02,1.12,.22,.96
//        animationSet.interpolator = PathInterpolator(.02f, 1.12f, .22f, .96f)
    }
    animationSet.duration = 200
    animationSet.fillAfter = true
    return animationSet
}
