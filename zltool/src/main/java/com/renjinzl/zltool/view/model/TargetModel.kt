package com.renjinzl.zltool.view.model

import android.text.TextUtils

/**
 * name
 * @author   renjinzl
 * date     2022-06-20 18:01
 *
 * version  1.0.0   update 2022-06-20 18:01   起航
 * @since 描述
 */
interface TargetModel {

    val mode: String

    val param: String

    /**
     * 是否可以执行
     */
    fun canExecute(): Boolean {
        return !TextUtils.isEmpty(mode)
    }

}
