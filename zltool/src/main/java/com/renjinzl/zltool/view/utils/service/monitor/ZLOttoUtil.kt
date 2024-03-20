package com.renjinzl.zltool.view.utils.service.monitor

import com.renjinzl.zltool.view.ZLOtto

/**
 * name
 * author   renjinzl

 * date     2023-11-16 22:04
 *
 * version  1.0.0   update 2023-11-16 22:04   起航
 * @since 描述
 */
class ZLOttoUtil {
    companion object {
        private val action: MutableMap<String, ZLOttoListener> = mutableMapOf()

        fun post(key:String, o: ZLOttoListener){
            action[key] = o
        }
        fun send(o: ZLOtto) {
            if (action.containsKey(o.key)) {
                action[o.key]?.apply(o)
//                action.remove(o.key)
            }
        }
    }
}

interface ZLOttoListener{
    fun apply(o:ZLOtto)
}