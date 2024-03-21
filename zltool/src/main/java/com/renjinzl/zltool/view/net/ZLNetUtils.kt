package com.renjinzl.zltool.view.net

import android.app.Dialog
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.renjinzl.zltool.view.context
import com.renjinzl.zltool.view.log
import com.renjinzl.zltool.view.model.ZLBaseDataModel
import com.renjinzl.zltool.view.toast
import com.renjinzl.zltool.view.utils.ZLLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.util.*


/**
 * OkHttp4 RequestBody.create()过时解决办法 https://blog.csdn.net/qq_19306415/article/details/102954712?utm_medium=distribute.pc_relevant.none-task-blog-2~default~baidujs_baidulandingword~default-12-102954712-blog-78654313.235^v38^pc_relevant_sort&spm=1001.2101.3001.4242.7&utm_relevant_index=13
 */
open class ZLNetUtils<T : ZLNetResultBean>(private val any: Any?, private val  model : T) {

    constructor(any: Any?,clazz: Class<T>) : this(any,clazz.newInstance())

    private var failureBlock: (ZLNetUtils<T>.() -> Unit)? = null

    private var defaultBlock: (T.() -> Unit)? = null

    protected var baseUrl: String = ""
    private var url: String = ""
    private var mParams: JSONObject = JSONObject()
    private var mpPrams: MutableMap<String, Any> = mutableMapOf()

    private var headers: TreeMap<String, Any> = TreeMap<String, Any>()

    private var loadingMessage = ""
    private var loadingDialog : Dialog? = null

    private val client = OkHttpClient()

    init {
        headers["content-type"] = "application/json"
        headers["Authorization"].toString().log("token->")
//        mpPrams["Authorization"] = "Bearer "+AccountServiceImpl.getInstance().token

        any?.context()?.let {
            loadingDialog = Dialog(it)
//            loadingDialog?.setContentView(R.layout.load_layout)
        }
    }

    private fun initHeader(): Request.Builder {
        return Request.Builder().addHeader("content-type", "application/json")
    }

    fun setUrl(url: String): ZLNetUtils<T> {
        this.url = url
        return this
    }

    private fun getUrl() : String{
        return "${baseUrl}$url?"
    }

    fun setParams(params: MutableMap<String, Any>?): ZLNetUtils<T> {
        params?.forEach { (key, value) ->
            mParams.put(key,value)
            mpPrams[key] = value

            ("$key $value").log("参数：")
        }

        return this
    }

    fun setParams(params: JSONObject?): ZLNetUtils<T> {
        params?.keys()?.forEach {
            mParams.put(it,params[it])
            mpPrams[it] = params[it]
        }
        return this
    }

    fun setLoadingMessage(message: String = "") : ZLNetUtils<T> {
        loadingMessage = message
        return this
    }

    //execute 同步
    fun  get(success: T.() -> Unit): ZLNetUtils<T> {

        if (!TextUtils.isEmpty(loadingMessage)){
            loadingDialog?.show()
        }

        var tUrl = ""
        mpPrams.forEach { (key, value) ->
            if (!TextUtils.isEmpty(""+value)){
                tUrl += "$key=$value&"
            }
        }

        ZLLog.d(getUrl()+tUrl)

        val response = client.newCall(initHeader().url(getUrl()+tUrl).build()).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
                call.toString().log()
                callBack(null,success)//todo 请求失败
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body != null) {
                    callBack(response.body?.string(), success)
                } else {
                    callBack(null,success)//todo 请求失败
                }
            }
        })
        return this
    }

    fun  post(success: T.() -> Unit): ZLNetUtils<T> {
        if (!TextUtils.isEmpty(loadingMessage)){
            loadingDialog?.show()
        }
        val formBody: FormBody = FormBody.Builder().let {
            mpPrams.forEach { (key, value) ->
                it.add(key, value.toString())
            }
            it.build()
        }

        val body: RequestBody = mParams.toString().toRequestBody()

        val response = client.newCall(initHeader().post(formBody).url(getUrl()).build()).enqueue(object : Callback{
            override fun onFailure(call: Call, e: IOException) {
                callBack(null,success)//todo 请求失败
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body!=null){
                    callBack(response.body?.string(),success)
                }else{
                    callBack(null,success)//todo 请求失败
                }
            }
        })
        return this
    }

    private fun callBack(dataString :String?,success: (T.() -> Unit)?) {
        loadingDialog?.dismiss()
//        ZLLog.d("-----${url}-----： "+responseResultBean.message+":"+responseResultBean.data)
        dataString?.also {
            try {
                model.analyze(JSONObject(it))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        any?.lifecycle()?.launch(Dispatchers.Main) {
            if (model.isSuccess()) {
                success?.let { it(model) }
            } else {
                failureBlock?.let { it() }
                any.context()?.apply {
                    model.message.toast(this)
                }
            }
            defaultBlock?.let { it(model) }
        }

//        ljq(responseResultBean.action)
    }


    /**
     * 不管错误还是失败都会屌用
     */
    fun default(block: T.() -> Unit) : ZLNetUtils<T>{
        this.defaultBlock = block
        return this
    }

    val MEDIA_TYPE_MARKDOWN = "text/x-markdown; charset=utf-8".toMediaType()
    fun uploadPhoto(path: String, progressView: View?, success: (T.() -> Unit)?) {
        progressView?.visibility = View.VISIBLE

        if (!TextUtils.isEmpty(loadingMessage)){
            loadingDialog?.show()
        }

        val file = File(path)
        if (!file.exists()) {
            any?.context()?.toast("文件不存在")
            return
        }

        ("上传到："+getUrl()).log()
        val multipartBody = MultipartBody.Builder().setType(MultipartBody.FORM)
            .addFormDataPart("file", file.name, RequestBody.create("image/*".toMediaTypeOrNull(), file))
            .build()
        val request = Request.Builder()
            .addHeader("content-type","multipart/form-data")
            .url(getUrl())
//            .post(file.asRequestBody(MEDIA_TYPE_MARKDOWN))
            .post(multipartBody)
            .build()


        val response = client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                loadingDialog?.dismiss()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.body != null) {
                    callBack(response.body?.string(),success)
                } else {
                    callBack(null,success)//todo 请求失败
                }
            }
        })
    }
}


private fun ZLBaseDataModel.analyze(json: String?){
    json?.apply {
        if (json.startsWith("{")&&json.endsWith("}")){
            try {
                analyze(JSONObject(json))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

    }
}


fun <T> T.lifecycle(): LifecycleCoroutineScope? {
    if (this is AppCompatActivity) return lifecycleScope
    if (this is Fragment) return lifecycleScope
    return null
}