package com.renjinzl.zltool.view

import android.app.Activity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.renjinzl.zltool.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 * name
 * @author   renjinzl
 * date     2022-06-14 17:54
 *
 * version  1.0.0   update 2022-06-14 17:54   起航
 * @since 描述
 */
class ZLPopupWindow : android.widget.PopupWindow() {
    init {
        setBackgroundDrawable(null)
    }
}

fun <T : View> T.popDown(view: View,block: ((PopupWindow) -> Unit)?) : T{
//    visibility = View.INVISIBLE
    val pop = PopupWindow(this, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
    pop.isOutsideTouchable = true // 聚焦点击事件不会透传给下面的View
    pop.showAsDropDown(view)

    block?.let { block(pop) }

//    postDelayed({
//        pop.dismiss()
//        visibility = View.VISIBLE
//        pop.showAsDropDown(view,(view.width - width)/2,0)
////        layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
////            setMargins(dp2px(120),0,0,0)
////        }
////        startAnimation(getAnimationBottomSlideUp())
//    }, 10)

    return this
}
fun <T : View> T.popDown(view: View,x: Int,y: Int,isOutsideTouchable : Boolean = true) : T{

    if (getTag(R.id.dialog) != null && getTag(R.id.dialog) is PopupWindow) {
        (getTag(R.id.dialog) as PopupWindow).apply {
            if (!isShowing) {
                showAsDropDown(view,x,y)
            }
        }
    }else{
        val pop = PopupWindow(this, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        setTag(R.id.dialog,pop)
        pop.isOutsideTouchable = isOutsideTouchable // 聚焦点击事件不会透传给下面的View
        pop.showAsDropDown(view,x,y)
    }

    return this
}

fun <T : View> T.popFullScreen(view: View,block: ((T, PopupWindow) -> Unit)?) : T{
//    visibility = View.INVISIBLE
//    val pop = PopupWindow(this,  FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
//    pop.showAtLocation(view, Gravity.NO_GRAVITY,0,0)

//    postDelayed({
//        pop.dismiss()
//        visibility = View.VISIBLEt6
//        pop.showAsDropDown(view,(view.width - width)/2,0)
//    }, 0)

    val pop = PopupWindow(this, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT,true).apply {
        setTag(R.id.dialog,this)
//        setPadding(0,context.getNavigationBarHeight(),0,0)
        isOutsideTouchable = true
//        isFocusable = true
        //以下两行设置全屏
        isClippingEnabled = false
        showAtLocation((view.context as Activity).window.decorView, Gravity.TOP, 0, 0)
    }

    block?.let { block(this,pop) }

    return this
}

fun <T : View> T.popDownMatch(view: View,block: ((PopupWindow) -> Unit)?) : T{
    val pop = PopupWindow(this,  FrameLayout.LayoutParams.MATCH_PARENT,(view.context as Activity).screenHeight - view.top - view.height - view.context.getNavigationBarHeight()).apply {
        isOutsideTouchable = true
    }
    pop.showAsDropDown(view)
    block?.let { block(pop) }
    return this
}

fun View.popUp(view: View,width : Int,height : Int,block: ((View, PopupWindow) -> Unit)?) {
    visibility = View.INVISIBLE
    val pop = PopupWindow(this, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)

//    (view.context as Activity).runOnUiThread {
//    }
    pop.showAsDropDown(view)

    //协程    llifecycleScope
    GlobalScope.launch(Dispatchers.Main) {
        pop.dismiss()
        delay(100)
        visibility = View.VISIBLE

        Log.d("13123123--- " ,"  p----  "+view.width+"  ---  "+(view.width - width) / 2)

        if (!(view.context as Activity).isDestroyed){
            pop.showAsDropDown(view,(view.width - width) / 2, - (view.height + height + dp2px(5)))
        }
        block?.apply { block(pop.contentView,pop) }
    }
}


//fun View.popUp(view: View) {
////    visibility = View.INVISIBLE
////    val pop = PopupWindow(this, FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
////    pop.showAsDropDown(view)
////    GlobalScope.launch(Dispatchers.Main) {
////        pop.dismiss()
////        visibility = View.VISIBLE
////        pop.showAsDropDown(view,(view.width - width) / 2, - (view.height + height + dp2px(5)))
////        block?.apply { block(pop.contentView,pop) }
////    }
//
//    val frameLayout = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
//    frameLayout.setMargins(dp2px(120),dp2px(120),0,0)
//    rootLayout.addView(this,frameLayout)
//    GlobalScope.launch(Dispatchers.Main) {
//        startAnimation(getAnimationBottomSlideUp())
//    }
//}