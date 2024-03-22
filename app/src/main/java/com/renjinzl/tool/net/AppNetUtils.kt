package com.renjinzl.tool.net

import android.text.TextUtils
import com.renjinzl.zltool.view.net.MyCookie
import com.renjinzl.zltool.view.net.ZLNetResultBean
import com.renjinzl.zltool.view.net.ZLNetUtils
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.experimental.and

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
        super.baseUrl = "http://pda.dev.epet.com/pda/"
        setParams(mutableMapOf(Pair("postsubmit","r9b8s7m4"),Pair("version","14.1"),Pair("appname","pda_factory")))
        var tempString = "|r9b8s7m4"
        MyCookie.getInstance().getCookieList().forEach { (t, u) ->
            tempString+="|${u.value}"
        }
        tempString +="|NM1906Yik4rk94nD01c"
        setParams(mutableMapOf(Pair("authkey",Md5.encryptionByMd5(tempString))))
    }

}

class LogoNetUtils<T : ZLNetResultBean>(any: Any?, model : T) : ZLNetUtils<T>(any, model) {

    constructor(any: Any?,clazz: Class<T>) : this(any,clazz.newInstance())

    init {
        super.baseUrl = "http://mallapi.dev.epet.com/v3/center/login.html?do=EmpLogin"
        //            version=14.1&appname=pda

        setParams(mutableMapOf(Pair("postsubmit","r9b8s7m4"),Pair("version","14.1"),Pair("appname","pda")))
    }

}