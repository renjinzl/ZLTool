package com.renjinzl.zltool.view.net

import okhttp3.Headers

class MyCookie {

    companion object{
        private val instance: MyCookie = MyCookie()

        fun getInstance() : MyCookie{
            return instance
        }
    }
    private var cookies: MutableMap<String, CookieInfo> = HashMap()

    fun addCookie(headers: Headers?) {
        headers?.forEach { (_, value) ->
            value.split(";").forEach { string ->
                val cookie = string.split("=")
                var cValue = ""
                if (cookie.size > 1){
                    cValue = cookie[1]
                }
                val key = cookie[0]
                if (key.isNotEmpty()){
                    cookies[key] = CookieInfo(cookie[0], cValue)
                }
            }
        }
    }

    fun getCookie() : String{
        var cookie = ""
        cookies.forEach { (_, value) ->
            cookie += value.name + "=" + value.value + ";"
        }
        return cookie
    }

    fun getCookieList() : MutableMap<String, CookieInfo>{
        return cookies
    }

    fun clear() {
        cookies.clear()
    }
}
class CookieInfo(var name : String?,var value : String?)