package com.renjinzl.zltool.view.model

import androidx.annotation.CallSuper
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.renjinzl.zltool.view.ZLViewModel
import com.renjinzl.zltool.view.utils.ZLLog
import org.json.JSONException
import org.json.JSONObject

open class ZLBaseDataModel (override var itemType: Int = 0) : ZLViewModel() {

    /**
     * 原始数据
     */
    var data: String = ""

    var isChecked: Boolean = false

    constructor() : this(0)

    /**
     * 你想怎么解
     */
    @CallSuper
    open fun analyze(json: JSONObject?){
        ZLLog.d("-----$this-----$json")
    }

}
