package com.renjinzl.zltool.view.utils

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.renjinzl.zltool.R
import com.renjinzl.zltool.view.click
import com.renjinzl.zltool.view.setVisibility

class HeadHelper {

    var titleView: TextView? = null

    var leftImageView: ImageView? = null

    private var rightImageView: ImageView? = null

    var rightTextView: TextView? = null

    private var backgroundView : View? = null
    private var searchBackImageView : View? = null

    fun initView(activity: Activity) : HeadHelper{
        titleView = activity.findViewById(R.id.activity_title_text_view)
        setTitle(activity.title)
        leftImageView = activity.findViewById(R.id.head_left_image_view)
        leftImageView?.click {
            activity.finish()
        }
//
//        searchBackImageView = activity.findViewById<View>(R.id.searchBackImageView)?.apply {
//            setOnClickListener {
//                activity.finish()
//            }
//        }
//
        rightImageView = activity.findViewById(R.id.head_right_image_view)
//        rightTextView = activity.findViewById(R.id.rightTextView)
//        backgroundView = activity.findViewById(R.id.activity_head_background)

        return this
    }

    fun setTitle(title: CharSequence?){
        titleView?.text = title
    }

    fun setRightImage(@DrawableRes resId : Int, block : (() -> Unit)?){
        rightImageView?.visibility = View.VISIBLE
        rightImageView?.setImageResource(resId)
        rightImageView?.setOnClickListener {
            block?.invoke()
        }
    }

    fun setRightText(text: String,block : (() -> Unit)?){
        rightTextView?.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
        rightTextView?.text = text
        rightTextView?.setOnClickListener {
            block?.invoke()
        }
    }

    fun setRightText(text: String){
        rightTextView?.visibility = if (text.isNotEmpty()) View.VISIBLE else View.GONE
        rightTextView?.text = text
    }

    fun setBackgroundColor(@ColorInt color: Int){
        backgroundView?.setBackgroundColor(color)
    }

    /**
     * 展示搜索返回
     */
    fun showSearchBack(show:Boolean){
        searchBackImageView?.setVisibility(show)
    }
}