package com.renjinzl.zltool.view.model

import com.renjinzl.zltool.view.utils.forEachObject
import org.json.JSONArray
import org.json.JSONObject


/**
 * name
 * author   renjinzl

 * date     2022-07-11 15:36
 *
 * version  1.0.0   update 2022-07-11 15:36   起航
 * @since 描述
 */
class Model {
    companion object {
        fun <T : ZLBaseDataModel> createModel(model: T, json: JSONObject?) : T {
            json?.apply {
                model.analyze(this)
            }
            return model
        }
        fun <T : ZLBaseDataModel> createModel(clazz: Class<T>, jsonArray : JSONArray?) : MutableList<T> {
            val tempList : MutableList<T> = arrayListOf()
            jsonArray?.forEachObject {
                tempList.add(clazz.newInstance().apply {
                    analyze(it)
                })
            }
            return tempList
        }
    }
}