package com.renjinzl.zltool.view

import android.app.Activity
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch


/**
 * Author   renjinzl
 *
 * Date     2023-05-26 15:01
 *
 * Email    renjinzl@163.com
 *
 * Version  1.0.0   @Update 2023-05-26 15:01 起航
 *
 * Describe 倒计时
 */

/**
 * 倒计时
 *
 * time 时间 默认3秒
 *
 * interval 间隔 默认1秒
 */
fun View.countDown(lifecycleScope: LifecycleCoroutineScope, time: Int = 3, interval: Long = 1000, onCompletion: () -> Unit) {
    lifecycleScope.launch(Dispatchers.Main) {
        flow {

            (time downTo 0).forEach {
                Log.d("--countDown-", "------$it")
                delay(interval)
                emit(it)
            }

        }.onStart {
            Log.d("--countDown-","---onStart---")
        }.onCompletion {
            Log.d("--countDown-","---onCompletion---")
            onCompletion()
        }.collect {
            Log.d("--countDown-","---collect---")
        }
    }
}