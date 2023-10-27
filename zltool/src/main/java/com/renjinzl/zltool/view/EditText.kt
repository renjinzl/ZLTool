package com.renjinzl.zltool.view

import android.content.Context
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * name
 * @author   renjinzl
 * date     2022-06-15 10:35
 *
 * version  1.0.0   update 2022-06-15 10:35   起航
 * @since 描述
 */

/**
 * 显示键盘
 */
fun EditText.focusEditShowKeyBoard() {
    isEnabled = true
    isFocusable = true
    isFocusableInTouchMode = true
    requestFocus()
    val inputManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    setSelection(text.length)
    inputManager.showSoftInput(this, 0)
}

/**
 * 是否可编辑
 */
fun EditText.canEdit(boolean: Boolean) {
    isFocusable = boolean
    isFocusableInTouchMode = boolean
    if (boolean){
        requestFocus()
    }
}

fun EditText.sureBack(block : EditText.() -> Unit) {
    setOnKeyListener { v, keyCode, event ->
        when(keyCode){
            KeyEvent.KEYCODE_SEARCH , KeyEvent.KEYCODE_ENTER -> block()
            KeyEvent.KEYCODE_BACK , KeyEvent.KEYCODE_DEL -> {
                return@setOnKeyListener false
            }
        }
        true
    }
}