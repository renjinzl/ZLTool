package com.renjinzl.zltool.view

import android.content.Context
import android.os.Build
import android.provider.Settings
import android.util.Log

private const val DISPLAY_NOTCH_STATUS = "display_notch_status"

/**
 * 获取虚拟操作拦（home等）高度
 */
fun Context.getNavigationBarHeight(): Int {
    val carrier = Build.MANUFACTURER
    if (carrier == ("HUAWEI")) {
        return checkHuaweiDisplayNotchStatus()
    }
    return getTopStatusBarHeight()
}


//--------------------- 华为 ------------------

//检查华为的刘海状态
private fun Context.checkHuaweiDisplayNotchStatus(): Int {
    val isLiuHai = hasNotchInHuawei()
    if (isLiuHai) {
        val mIsNotchSwitchOpen = Settings.Secure.getInt(contentResolver,DISPLAY_NOTCH_STATUS, 0)
        //  1 隐藏显示区域 0 是没隐藏
        if (mIsNotchSwitchOpen != 1) {
            return getNotchSize()
        }
    }
    return getTopStatusBarHeight()
}

private fun Context.hasNotchInHuawei(): Boolean {
    val hwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil")
    val hasNotchInScreen = hwNotchSizeUtil.getMethod("hasNotchInScreen")
    return hasNotchInScreen.invoke(hwNotchSizeUtil) as Boolean
}

private fun Context.getNotchSize(): Int {
    var ret: IntArray = intArrayOf(0, 0)
    try {
        val hwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil")
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

private fun Context.getTopStatusBarHeight(): Int {
//        return resources.getDimensionPixelSize(resources.getIdentifier("navigation_bar_height", "dimen", "android"))
//        int resourceId = context.getResources().getIdentifier(
//            "status_bar_height", "dimen", "android");
//        mStatusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
    return resources.getDimensionPixelSize(resources.getIdentifier("status_bar_height", "dimen", "android"))
//        return getStatusBarHeight()
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

//private fun Context.checkXiaoMisplayNotchStatus(): Int {
//    return getTopStatusBarHeight()
////        val hasLiuHai = hasNotchXiaoMi()
////        return if (hasLiuHai) {
////            val mIsNotchSwitchOpen =
////                Settings.Secure.getInt(context.contentResolver, DISPLAY_NOTCH_STATUS, 0)
////            //  1 隐藏显示区域 0 是没隐藏
////            if (mIsNotchSwitchOpen != 1) {
////                getTopStatusBarHeight()
////            } else {
////                0
////            }
////        } else {
////            0
////        }
//}

//@SuppressLint("PrivateApi")
//private fun Context.hasNotchXiaoMi(): Boolean {
//    return try {
//        val c: Class<*> = Class.forName("android.os.SystemProperties")
//        val get: Method = c.getMethod("getInt", String::class.java, Int::class.java)
//        get.invoke(c, "ro.miui.notch", 0) as Int == 1
//    } catch (e: Exception) {
//        e.printStackTrace();
//        false;
//    }
//}