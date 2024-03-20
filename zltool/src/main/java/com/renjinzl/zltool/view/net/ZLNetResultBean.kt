package com.renjinzl.zltool.view.net

import androidx.annotation.CallSuper
import com.renjinzl.zltool.view.ZLViewModel
import com.renjinzl.zltool.view.log
import org.json.JSONException
import org.json.JSONObject
import java.io.Serializable

/**
 * 请求返回
 */
open class ZLNetResultBean : ZLViewModel() {

    /**
     * 状态码
     */
     var code = 200

    /**
     * 描述信息
     */
    var message: String = ""

    /**
     * 数据对象/成功返回对象
     */
    var data: String = ""

    /**
     * 是否成功
     *
     * @return
     */
    open fun isSuccess(): Boolean {
        return code == 200
    }

    @CallSuper
    open fun analyze(json: JSONObject?):ZLNetResultBean{
        json?.apply {
            code = optInt("code")
            message = optString("msg")
            data = optString("data")
        }
        return this
    }
}