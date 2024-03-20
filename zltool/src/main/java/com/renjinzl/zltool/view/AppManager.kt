package com.renjinzl.zltool.view

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import java.util.*

/**

 * Author   renjinzl
 * Date     2022-06-06 15:51
 * Email    renjinzl@163.com
 * Version  1.0.0   @Update 2022-06-06 15:51   起航
 * Describe 描述
 */
class AppManager private constructor() {

    private var activities: Stack<Activity> = Stack()

    private var mMainActivity: Activity? = null

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var instance: AppManager? = null

        fun instance(): AppManager {
            if (instance == null) {
                synchronized(AppManager::class.java) {
                    if (instance == null) {
                        instance = AppManager()
                    }
                }
            }
            return instance!!
        }
    }


    /**
     * 当前堆栈是否为空？
     */
    fun isEmpty(): Boolean {
        return activities.isEmpty()
    }

    /**
     * 获取堆栈中Activity的数量
     */
    private fun size(): Int {
        return activities.size
    }

    /**
     * 获取堆栈中所有的Activity
     */
    fun getActivityList(): Stack<Activity> {
        return activities
    }

    /**
     * 添加Activity
     */
    fun addActivity(activity: Activity) {
        if (activity !is Activity) {
            activities.add(activity)
        }else {
            setMainActivity(activity)
        }
    }

    /**
     * 添加首页Activity
     */
    fun setMainActivity(activity: Activity) {
        mMainActivity = activity
    }

    fun getMainActivity(): Activity {
        return mMainActivity!!
    }

    /**
     * 销毁MainActivity
     */
    fun finishMainActivity() {
        if (mMainActivity != null) {
            mMainActivity?.finish()
            mMainActivity = null
        }
    }

    fun hasMainActivity() : Boolean{
        return mMainActivity != null
    }

    /**
     * 获取当前Activity：堆栈最上层的Activity
     */
    fun currentActivity(): Activity {
        return if (isEmpty()) getMainActivity() else activities.lastElement()
    }

    /**
     * 结束当前Activity：堆栈最上层的Activity
     */
    fun finishCurrentActivity() {
        val activity = currentActivity()
        this.finish(activity)
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        if (!this.isEmpty()) {
            for (activity in activities) {
                activity.finish()
            }
            activities.clear()
        }
    }

    /**
     * 结束指定Activity
     *
     * @param clazz 指定需要关闭的Activity
     */
    fun finish(clazz: Class<*>) {
        if (!isEmpty()) {
            for (activity in activities) {
                if (activity.javaClass == clazz) {
                    this.finish(activity)
                    break
                }
            }
        }
    }

    /**
     * 结束指定Activity
     *
     * @param className 指定需要关闭的Activity 类名
     */
    fun finish(className: String) {
        if (!isEmpty()) {
            for (activity in activities) {
                if (activity.javaClass.simpleName == className) {
                    this.finish(activity)
                    break
                }
            }
        }
    }

    /**
     * 结束指定Activity
     *
     * @param activity [Activity]
     */
    fun finish(activity: Activity) {
        this.remove(activity)
        activity.finish()
    }

    /**
     * 从堆栈中移除某个Activity，但并不关闭它
     *
     * @param activity [Activity]
     */
    fun remove(activity: Activity) {
        if (!this.isEmpty()) {
            activities.remove(activity)
        }
    }

    /**
     * Activity是否在运行
     *
     * @param activity [Activity]
     */
    private fun isRun(activity: Activity): Boolean {
        return !(activity.isDestroyed || activity.isFinishing)
    }

    /**
     * 根据class 获取activity实列
     *
     * @param clazz [Class]
     * @return Activity
     */
    fun findActivityByClass(clazz: Class<*>): Activity? {
        if (activities.isEmpty()) {
            return mMainActivity
        }
        for (activity in activities) {
            if (activity.javaClass == clazz) {
                return activity
            }
        }
        return mMainActivity
    }

    /**
     * 根据class 判断是否存在
     *
     * @param clazz [Class]
     * @return boolean
     */
    fun hasActivityByClass(clazz: Class<*>): Boolean {
        if (activities.isEmpty()) {
            return false
        }
        for (activity in activities) {
            if (activity.javaClass == clazz) {
                return true
            }
        }
        return false
    }

    /**
     * 根据class 名字获取activity实列
     *
     * @param className 类名
     * @return Activity
     */
    fun findActivityByClassName(className: String): Activity? {
        if (activities.isEmpty()) {
            return mMainActivity
        }
        for (activity in activities) {
            if (activity.javaClass.simpleName == className) {
                return activity
            }
        }
        return mMainActivity
    }

    /**
     * 获取上一个Activity的实例（倒数第二）
     *
     * @return Activity
     */
    fun findSecondLastActivity(): Activity? {
        val size = size()
        return if (size >= 2) {
            activities[size - 2]
        } else null
    }

    /**
     * 退出
     */
    fun exitSystem(){
        finishMainActivity()
        finishAllActivity()
    }

    fun isAppOnForeground(): Boolean {
        for (activity in activities) {
            return activity.window.decorView.visibility == View.VISIBLE
        }
        return mMainActivity?.window?.decorView?.visibility == View.VISIBLE
    }
}