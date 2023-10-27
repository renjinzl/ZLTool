package com.renjinzl.zltool.view


import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.RelativeSizeSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.util.TypedValue
import androidx.annotation.ColorInt

/**
 * name
 * @author   renjinzl
 * date     2022-06-14 16:09
 *
 * version  1.0.0   update 2022-06-14 16:09   起航
 * @since 描述
 */

/**
 * 设置字体大小
 */
fun TextView.textSize(size: Int) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size.toFloat())
}

/**
 * 设置字体大小
 */
fun TextView.textSize(size: Float) {
    setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
}

fun SpannableStringBuilder.setUnderline(start: Int, end: Int) : SpannableStringBuilder{
    setSpan(UnderlineSpan(),start,end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

fun SpannableStringBuilder.setForegroundColor(start: Int, end: Int, @ColorInt color: Int) :SpannableStringBuilder{
    setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    return this
}

/**
 * 将文本内容复制到系统粘贴板
 */
fun TextView.copyText() {
    val clipboardManager = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val tempText = text
    clipboardManager.setPrimaryClip(ClipData.newPlainText(null, tempText))
    toast("成功复制到粘贴板中")
}

/**
 * name
 * author   renjinzl

 * date     2022-07-01 11:09
 *
 * version  1.0.0   update 2022-07-01 11:09   起航
 * @since 描述
 */

/**
 * 内容为空则不显示
 */
fun BaseViewHolder.setTextVisible(@IdRes viewId: Int, value: CharSequence?): BaseViewHolder {
    setText(viewId, value)
    return setGone(viewId, TextUtils.isEmpty(value))
}
fun BaseViewHolder.setTextInvisible(@IdRes viewId: Int, value: CharSequence?): BaseViewHolder {
    setText(viewId, value)
    return setVisible(viewId, !TextUtils.isEmpty(value))
}
fun BaseViewHolder.setTextList(@IdRes viewId: Int, textList: MutableList<TextModel>): BaseViewHolder {

    getView<TextView>(viewId).apply {

        if (textList.size > 0) {
            var textFontSize: Float
            textFontSize = textList[0].fontSize

            if (textFontSize == 0f) {
                textFontSize = textSize
            }
            textSize = textFontSize

            var tempString = ""
            textList.forEach {
                tempString += it.text
            }

            val spannableString = SpannableString(tempString)

            tempString = ""
            textList.forEach {
                spannableString.setSpan(RelativeSizeSpan(it.fontSize/textFontSize),tempString.length,tempString.length+it.text.length,Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
                tempString+=it.text
            }

            text = spannableString

        }
    }

    return this
}

fun BaseViewHolder.setPrice(@IdRes viewId: Int, value: CharSequence?): BaseViewHolder {
    getView<TextView>(viewId).setPrice(value)
    return setGone(viewId, TextUtils.isEmpty(value))
}

fun BaseViewHolder.setText(@IdRes viewId: Int, prefix: CharSequence = "", value: CharSequence?, suffix: CharSequence = ""): BaseViewHolder {
    getView<TextView>(viewId).setText(prefix,"$prefix$value$suffix")
    return setGone(viewId, TextUtils.isEmpty(value))
}

fun BaseViewHolder.setPriceVisible(@IdRes viewId: Int, value: CharSequence?): BaseViewHolder {
    getView<TextView>(viewId).setPrice(value)
    return setGone(viewId, TextUtils.isEmpty(value) || "0" == value)
}

/**
 * currency代表它币
 */
fun BaseViewHolder.setCurrencyVisible(@IdRes viewId: Int, value: CharSequence?): BaseViewHolder {
    val currency = "$value"+"币"
    getView<TextView>(viewId).text = currency
    return setGone(viewId, TextUtils.isEmpty(value))
}

/**
 * 划线价格
 * 空字符就隐藏
 * 取消划线 tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG))
 */
fun BaseViewHolder.setStrikePriceVisible(@IdRes viewId: Int, value: CharSequence?): BaseViewHolder {
    getView<TextView>(viewId).apply {
        setStrikePriceVisible(value)
    }
    return setGone(viewId, TextUtils.isEmpty(value))
}

/**
 * 划线价格
 *
 * 空字符就隐藏
 *
 * 取消划线 tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG))
 */
fun TextView.setStrikePriceVisible(value: CharSequence?) {
    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
    setPrice(value)
}

fun TextView.setPrice(value: CharSequence?){
    text = ""
    value?.let {
        if (it.isNotEmpty()){
//            text = "¥$value"
            val spannableString = SpannableString("¥$value")
            spannableString.setSpan(RelativeSizeSpan(0.8f), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            text=spannableString
        }
    }
}
fun TextView.setPriceVisible(value: CharSequence?){
    setVisibility(!TextUtils.isEmpty(value))
    value?.let {
        if (it.isNotEmpty()){
//            text = "¥$value"
            val spannableString = SpannableString("¥$value")
            spannableString.setSpan(RelativeSizeSpan(0.8f), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            text=spannableString
        }
    }
}
fun TextView.setTextVisible(value: CharSequence?){
    setVisibility(!TextUtils.isEmpty(value))
    text = value
}
fun TextView.setText(prefix: CharSequence = "",value: CharSequence?){
    setVisibility(!TextUtils.isEmpty(value))
    text = value
}


fun String.hidePhone() : String{
    if (this.length > 10) {
        val buff = StringBuffer()
        this.forEachIndexed { index, c ->
            if (index in 3..8) {
                buff.append('*')
            } else {
                buff.append(c)
            }
        }
        return buff.toString()
    }
    return this
}

interface TextModel{
    val fontSize:Float
    val text:String
}