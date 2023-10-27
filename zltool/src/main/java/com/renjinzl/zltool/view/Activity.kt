package com.renjinzl.zltool.view

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat


/**

 * Author   renjinzl
 * Date     2022-06-10 10:30
 * Email    renjinzl@163.com
 * Version  1.0.0   @Update 2022-06-10 10:30   起航
 * Describe 描述
 */
val Activity.context: Context get() = this

/**
 * 屏幕高
 */
val Activity.screenHeight: Int get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    windowManager.currentWindowMetrics.bounds.height()
} else {
    val displayMetrics = DisplayMetrics()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
        displayMetrics
    )
    displayMetrics.heightPixels
}

/**
 * 屏幕宽
 */
val Activity.screenWidth: Int get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
    windowManager.currentWindowMetrics.bounds.width()
} else {
    val displayMetrics = DisplayMetrics()
    (getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
        displayMetrics
    )
    displayMetrics.widthPixels
}


/**
 * 设置将内容沉浸到导航栏中，实现真正的全屏
 */
fun Activity.setScreenFullNavigationBar() {

    //false 表示沉浸，true表示不沉浸
//    WindowCompat.setDecorFitsSystemWindows(window, false)

    window?.apply {
        //设置专栏栏和导航栏的底色，透明
        statusBarColor = Color.TRANSPARENT
//        navigationBarColor = Color.TRANSPARENT
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
//            navigationBarDividerColor = Color.TRANSPARENT
//        }
        decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }
//    window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)


//    statusBarColor(false)
}

fun Activity.statusBarColor(isBlack: Boolean){
    //设置沉浸后专栏栏和导航字体的颜色，
    ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))?.let { controller ->
        controller.isAppearanceLightStatusBars = isBlack
        controller.isAppearanceLightNavigationBars = isBlack
    }
}

/**
 * 全屏
 */
fun Activity.hideSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    ViewCompat.getWindowInsetsController(window.decorView)?.let { controller ->
        //WindowInsetsCompat.Type.systemBars()表示状态栏、导航栏和标题栏
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

/**
 * 退出全屏
 * window.decorView -> findViewById<FrameLayout>(android.R.id.content)
 */
fun Activity.showSystemUI() {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))
        ?.show(WindowInsetsCompat.Type.systemBars())
}

/**
 * 显示和隐藏键盘
 */
fun Activity.showSoftKeyboard(isShow: Boolean) {
    val windowInsetsControllerCompat: WindowInsetsControllerCompat? = ViewCompat.getWindowInsetsController(findViewById<FrameLayout>(android.R.id.content))
    if (isShow) {
        windowInsetsControllerCompat?.show(WindowInsetsCompat.Type.ime())
    } else {
        windowInsetsControllerCompat?.hide(WindowInsetsCompat.Type.ime())
    }
}

private val Activity.windowInsetsCompat: WindowInsetsCompat?
    get() = ViewCompat.getRootWindowInsets(
        findViewById<FrameLayout>(android.R.id.content)
    )

/**
 * 获取状态栏的高度
 */
fun Activity.getStatusBarsHeight(): Int {
    val windowInsetsCompat = windowInsetsCompat ?: return 0
    return windowInsetsCompat.getInsets(WindowInsetsCompat.Type.statusBars()).top
}

/**
 * APP进入前台
 */
fun Activity.appEnterForeground(){
}
/**
 * APP进入前台
 */
fun Activity.appEnterBackground(){
}
