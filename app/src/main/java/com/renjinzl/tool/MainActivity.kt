package com.renjinzl.tool

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.renjinzl.tool.net.AppNetUtils
import com.renjinzl.zltool.view.log
import com.renjinzl.zltool.view.net.ZLNetResultBean
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        /main.html?do=appInit
    }

    override fun onStart() {
        super.onStart()
        AppNetUtils(this, BaseModel::class.java).setUrl("main.html").setParams(mutableMapOf(Pair("do","appInit"))).post {
            "data".log()
        }
    }
}

class BaseModel : ZLNetResultBean() {

    private var codeState = ""
    override fun isSuccess(): Boolean {
        return "succeed" == codeState
    }
    override fun analyze(json: JSONObject?): ZLNetResultBean {
        json?.apply {
            codeState = optString("code")
        }
        return super.analyze(json)
    }
}