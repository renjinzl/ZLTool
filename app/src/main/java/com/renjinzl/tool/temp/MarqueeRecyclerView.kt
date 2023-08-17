package com.renjinzl.tool.temp

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import java.lang.ref.SoftReference

class MarqueeRecyclerView(@NonNull context: Context, @Nullable attrs: AttributeSet?) : RecyclerView(context, attrs) {

    companion object{
        const val TIME_AUTO_POLL: Long = 30
    }

    var autoPollTask: AutoPollTask? = null

    /**
     * 是否正在自动轮询
     */
    var running = false

    /**
     * 是否可以自动轮询,可在不需要的是否置false
     */
    var canRun = false

    fun initAutoPoll() {
        if (autoPollTask == null) {
            autoPollTask = AutoPollTask(this)
            postDelayed(autoPollTask, TIME_AUTO_POLL)
        }
    }

    //开启:如果正在运行,先停止->再开启
    fun start() {
        if (canRun) {
            running = true
        }
    }

    fun stop() {
        running = false
    }

    override fun performClick(): Boolean {
        Log.d("MarqueeRecyclerView","performClick")
        return super.performClick()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                stop()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                performClick()
                if (canRun) start()
            }
        }
        return super.onTouchEvent(e)
    }
}

class AutoPollTask(reference: MarqueeRecyclerView) : Runnable {
    private val mReference: SoftReference<MarqueeRecyclerView> = SoftReference(reference)
    override fun run() {
        val recyclerView = mReference.get()
        if (recyclerView != null) {
            if (recyclerView.running && recyclerView.canRun) {
                recyclerView.scrollBy(1, 1)
            }
            recyclerView.postDelayed(recyclerView.autoPollTask, MarqueeRecyclerView.TIME_AUTO_POLL)
        }
    }

}