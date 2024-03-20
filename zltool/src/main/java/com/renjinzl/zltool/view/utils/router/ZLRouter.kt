package com.renjinzl.zltool.view.utils.router


import android.app.Activity
import android.content.Context
import android.content.Intent
import com.renjinzl.zltool.view.utils.ZLLog
import org.json.JSONException
import org.json.JSONObject


/**
 * name
 * author   renjinzl

 * date     2023-10-31 15:33
 *
 * version  1.0.0   update 2023-10-31 15:33   起航
 * @since 描述
 */
class ZLRouter {

    companion object {

        private val modeArray: MutableMap<String, RouterOperation> = mutableMapOf()

        init {
            ZLLog.d("init-EPRouter-------init")
        }

        fun <T:Activity>addModel(mode: String, clazz: Class<T>){
            modeArray[mode] = RouterEntity(clazz)
        }

        fun addModel(mode: String, operation: RouterOperation){
            modeArray[mode] = operation
        }

        fun go(context: Context, mode: String, param: String = "") {
            ZLLog.d("model:$mode param:$param")
            modeArray[mode]?.apply(context,mode,param)
        }
    }
}

private class RouterEntity<T:Activity>(val clazz: Class<T>) : RouterOperation {
    override fun apply(context: Context, mode: String, param: String) {
        context.startActivity(Intent(context,clazz).apply {

            putExtra("mode",mode)

            if (param.startsWith("{") && param.endsWith("}")) {
                try {
                    JSONObject(param).let { json ->
                        json.keys().forEach {
                            putExtra(it,json.getString(it))
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            } else {
                putExtra("param", param)
            }
        })
    }
}
