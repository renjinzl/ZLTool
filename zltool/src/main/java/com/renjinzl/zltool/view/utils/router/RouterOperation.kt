package com.renjinzl.zltool.view.utils.router

import android.content.Context

/**
 * name
 * author   renjinzl

 * date     2023-10-31 16:20
 *
 * version  1.0.0   update 2023-10-31 16:20   起航
 * @since 描述
 */
interface RouterOperation {
    fun apply(context: Context, mode: String, param: String)
}