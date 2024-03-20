package com.renjinzl.zltool.view.utils

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * name
 * @author   renjinzl

 * date     2022-06-27 10:11
 *
 * version  1.0.0   update 2022-06-27 10:11   起航
 * @since 描述
 */

fun JSONObject.string(key: String): String {
    return optString(key) ?: ""
}

fun JSONObject.intValue(key: String): Int {
    try {
        return getInt(key)
    } catch (e: JSONException) {
        e.printStackTrace()
    } catch (e: RuntimeException) {
        e.printStackTrace()
    }
    return 0
}

fun JSONObject.jsonArrayZL(key: String): JSONArray {
    if (has(key) && string(key).startsWith("[")) {
        return getJSONArray(key)
    }
    return JSONArray()
}

fun String.parseObject(): JSONObject {
    if (this.startsWith("{")) {
        return JSONObject(this)
    }
    return JSONObject()
}

fun JSONArray.forEachObject(block: (JSONObject) -> Unit){
    for ( i in 0 until this.length()){
        block(optJSONObject(i))
    }
}

fun JSONArray.forEachString(block: (String) -> Unit){
    for ( i in 0 until this.length()){
        block(getString(i))
    }
}
