package com.renjinzl.zltool.view

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.Toast

/**
 * name
 * author   renjinzl

 * date     2022-07-28 16:07
 *
 * version  1.0.0   update 2022-07-28 16:07   起航
 * @since 描述
 */
fun Context.toast(text : String){
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

fun Activity.toast(text: String){
    context.toast(text)
}

fun View.toast(text: String){
    if (!TextUtils.isEmpty(text)){
        context.toast(text)
    }
//    Toast.makeText(context,string, Toast.LENGTH_SHORT).show()
}
fun String.toast(context: Context){
    if (!TextUtils.isEmpty(this)){
        context.toast(this)
    }
}