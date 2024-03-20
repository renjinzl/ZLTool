package com.renjinzl.zltool.view.utils

import android.content.Context
import com.renjinzl.zltool.view.ZLApplication

/**
 * name
 * @author   renjinzl
 * Date     2022-06-13 14:43
 * Email    renjinzl@163.com
 * Version  1.0.0   @Update 2022-06-13 14:43   起航
 * @since 描述
 */
class ZLPrePreferences(context:Context) : BasePreferences(context, "com.renjinzl.tools", Context.MODE_PRIVATE) {


    companion object {

        @Volatile
        private var instances: ZLPrePreferences? = null

        fun instance(): ZLPrePreferences {
            if (instances == null) {
                instances = instance(ZLApplication.application)
            }
            return instances!!
        }

        fun instance(context:Context): ZLPrePreferences {
            if (instances == null) {
                synchronized(ZLPrePreferences::class.java) {
                    if (instances == null) {
                        instances = ZLPrePreferences(context)
                    }
                }
            }
            return instances!!
        }
    }


    fun getAll(): Map<String?, *>? {
        return if (preferences == null) null else preferences.all
    }


}