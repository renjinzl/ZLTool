package com.renjinzl.zltool.view.utils.service.monitor

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.renjinzl.zltool.view.AppManager
import com.renjinzl.zltool.view.appEnterBackground
import com.renjinzl.zltool.view.appEnterForeground
import com.renjinzl.zltool.view.isBackground

/**

 * Author   renjinzl
 * Date     2022-06-10 18:09
 * Email    renjinzl@163.com
 * Version  1.0.0   @Update 2022-06-10 18:09   起航
 * Describe 描述
 */
class ActivityLifecycleCallback : Application.ActivityLifecycleCallbacks{

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        AppManager.instance().addActivity(activity)
    }

    override fun onActivityStarted(activity: Activity) {
        //从后台进入前台
        if (activity.application.isBackground) {
            activity.application.isBackground = false
            activity.appEnterForeground()
        }
    }

    override fun onActivityResumed(activity: Activity) {
    }

    override fun onActivityPaused(activity: Activity) {
    }

    override fun onActivityStopped(activity: Activity) {
        if (!isAppOnForeground()) {
            activity.application.isBackground = true
            activity.appEnterBackground()
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
    }

    override fun onActivityDestroyed(activity: Activity) {
        //// 在ActivityManager中关闭当前界面并移除
        AppManager.instance().remove(activity)
    }


    /**
     * 程序是否在前台运行
     */
    private fun isAppOnForeground(): Boolean {
        return AppManager.instance().isAppOnForeground()
    }
}