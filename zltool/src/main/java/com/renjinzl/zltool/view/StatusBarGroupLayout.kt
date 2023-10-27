package com.renjinzl.zltool.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout
import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * name
 * @author   renjinzl
 * date     2022-06-15 11:05
 *
 * version  1.0.0   update 2022-06-15 11:05   起航
 * @since 描述
 */
class StatusBarGroupLayout(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    FrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null)


    init {
        setPadding(paddingLeft, getNavigationBarHeight(), paddingRight, paddingBottom)
        Log.d("StatusBarGroupLayout0------","---- "+getNavigationBarHeight())
    }

    /**
     * 获取虚拟操作拦（home等）高度
     */
    private fun getNavigationBarHeight(): Int {
        val carrier = Build.MANUFACTURER
        if (carrier == ("HUAWEI")) {
            return checkHuaweiDisplayNotchStatus()
        }
        return getTopStatusBarHeight()
    }

    /**
     * 是否虚拟操作拦/导航栏（home等）是否显示
     *
     * @param activity Activity
     */
    private fun isNavigationBarShow(activity: Activity): Boolean {
        val display = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            activity.display
        } else {
            activity.windowManager.defaultDisplay
        }
        val size = Point()
        val realSize = Point()
        display?.getSize(size)
        display?.getRealSize(realSize)
        return realSize.y != size.y
    }

    companion object {
        private const val DISPLAY_NOTCH_STATUS = "display_notch_status"
    }

    private fun getTopStatusBarHeight(): Int {
//        return resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"))
//        int resourceId = context.getResources().getIdentifier(
//            "status_bar_height", "dimen", "android");
//        mStatusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
//        return getStatusBarHeight()
    }

    private fun getStatusBarHeight(): Int {
        var c: Class<*>? = null
        var obj: Any? = null
        var field: Field? = null
        var x = 0
        return try {
            c = Class.forName("com.android.internal.R\$dimen")
            obj = c.newInstance()
            field = c.getField("status_bar_height")
            x = field.get(obj).toString().toInt()
            resources.getDimensionPixelSize(x)
        } catch (e1: java.lang.Exception) {
            Log.d(TAG, "get status bar height fail")
            e1.printStackTrace()
            75
        }
    }
    //--------------------- 华为 ------------------

    //检查华为的刘海状态
    private fun checkHuaweiDisplayNotchStatus(): Int {
        val isLiuHai = hasNotchInHuawei()
        if (isLiuHai) {
            val mIsNotchSwitchOpen = Settings.Secure.getInt(context.contentResolver, DISPLAY_NOTCH_STATUS, 0)
            //  1 隐藏显示区域 0 是没隐藏
            if (mIsNotchSwitchOpen != 1) {
                return getNotchSize()
            }
        }
        return getTopStatusBarHeight()
    }

    private fun hasNotchInHuawei(): Boolean {
        val cl: ClassLoader = context.classLoader
        val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
        val hasNotchInScreen = hwNotchSizeUtil.getMethod("hasNotchInScreen")
        return hasNotchInScreen.invoke(hwNotchSizeUtil) as Boolean
    }

    private fun getNotchSize(): Int {
        var ret: IntArray = intArrayOf(0, 0)
        try {
            val cl: ClassLoader = context.classLoader
            val hwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil")
            val get = hwNotchSizeUtil.getMethod("getNotchSize")
            ret = get.invoke(hwNotchSizeUtil) as IntArray
        } catch (e: ClassNotFoundException) {
            Log.e("test", "getNotchSize ClassNotFoundException");
        } catch (e: NoSuchMethodException) {
            Log.e("test", "getNotchSize NoSuchMethodException");
        } catch (e: Exception) {
            Log.e("test", "getNotchSize Exception");
        }
        return ret[1]
    }


    //--------------------- 小米-------------------

    /**
     * 判断xiaomi是否有刘海屏
     * https://dev.mi.com/console/doc/detail?pId=1293
     *
     * @return
     */
//    private void checkXiaoMiisplayNotchStatus() {
//        boolean isLiuHai = hasNotchXiaoMi(this);
//        Log.d("Unity", "这个小米是刘海屏吗" + isLiuHai);
//        if (isLiuHai)// 是xiaomi刘海屏
//        {
//            int mIsNotchSwitchOpen = Settings.Secure.getInt(getContentResolver(), DISPLAY_NOTCH_STATUS, 0);
//            Log.d("Unity", "刘海开启状态=" + mIsNotchSwitchOpen);
//            if (mIsNotchSwitchOpen != 1) //  1 隐藏显示区域 0 是没隐藏
//            {
//                int liuhaiLength = getTopStatusBarHeight(this);
//                Log.d("Unity", "刘海长度=" + liuhaiLength);
//                sendTopInfo(true,liuhaiLength);
//            }
//        }else{
//            sendTopInfo(false,0);
//        }
//    }

    private fun checkXiaoMisplayNotchStatus(): Int {
        return getTopStatusBarHeight()
//        val hasLiuHai = hasNotchXiaoMi()
//        return if (hasLiuHai) {
//            val mIsNotchSwitchOpen =
//                Settings.Secure.getInt(context.contentResolver, DISPLAY_NOTCH_STATUS, 0)
//            //  1 隐藏显示区域 0 是没隐藏
//            if (mIsNotchSwitchOpen != 1) {
//                getTopStatusBarHeight()
//            } else {
//                0
//            }
//        } else {
//            0
//        }
    }

    @SuppressLint("PrivateApi")
    fun hasNotchXiaoMi(): Boolean {
        return try {
            val c: Class<*> = Class.forName("android.os.SystemProperties")
            val get: Method = c.getMethod("getInt", String.javaClass, Int.javaClass)
            get.invoke(c, "ro.miui.notch", 0) as Int == 1
        } catch (e: Exception) {
            e.printStackTrace();
            false;
        }
    }
}