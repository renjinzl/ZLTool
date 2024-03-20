package com.renjinzl.tool.net

import com.renjinzl.zltool.view.net.ZLNetResultBean
import com.renjinzl.zltool.view.net.ZLNetUtils

/**
 * name
 * author   renjinzl

 * date     2024-03-20 15:30
 *
 * version  1.0.0   update 2024-03-20 15:30   起航
 * @since 描述
 */
class AppNetUtils<T : ZLNetResultBean>(any: Any?, model : T) : ZLNetUtils<T>(any, model) {

    constructor(any: Any?,clazz: Class<T>) : this(any,clazz.newInstance())

    init {
        super.baseUrl = "http://mall.api.dev.epet.com/v3/"
    }

}