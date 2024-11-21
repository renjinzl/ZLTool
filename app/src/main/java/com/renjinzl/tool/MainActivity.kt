package com.renjinzl.tool

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.renjinzl.tool.net.AESHelper
import com.renjinzl.tool.net.AppNetUtils
import com.renjinzl.tool.net.LogoNetUtils
import com.renjinzl.zltool.view.*
import com.renjinzl.zltool.view.net.ZLNetResultBean
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.textView).setOnClickListener {
            LogoNetUtils(this, BaseModel::class.java).setParams(mutableMapOf(Pair("do","EmpLogin"),Pair("username", AESHelper.Encrypt("testWareCQ")),Pair("password",AESHelper.Encrypt("epet2019!")))).post {

                this.data.log("logo_data->")

                AppNetUtils(this@MainActivity, BaseModel::class.java).setUrl("verify/userInfo.html").setParams(mutableMapOf(Pair("do","currenWarehouse"))).get {
                }.default {
                    this.data.log("data->")
                }
            }

        }

        findViewById<RecyclerView>(R.id.recyclerView).apply {
            initVertical()
            addItemType(ItemView(0))

            val menusInfo: MutableList<ZLViewModel> = arrayListOf()

            menusInfo.apply {
                add(SettingItem(0, "切换仓库", false, ""))
            }

            setData(menusInfo)
        }
//        /main.html?do=appInit
    }

    override fun onStart() {
        super.onStart()

        "X15t_gott_auth=eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjp7InVpZCI6MTk2MDk2NiwiYWNJZCI6IjIyOTkwMTkxNjQ5MDk3ODY5IiwibmFtZSI6InRlc3RXYXJlQ1EiLCJvcmdJZCI6IjEwMCJ9LCJpYXQiOjE3MTEwNzAwMDcsImV4cCI6MTcxMTEwNjAwN30.Djf3mOYzrkVQvMRX4onN-JKI7seuatI95dbnfO3kvN4; expires=Mon, 20-Mar-2034 01:13:27 GMT; Max-Age=315360000; path=/; domain=.epet.com"
            .split(";").forEach { string ->

                string.log("coo:")
                val cookie = string.split("=")
                cookie[0].log("key:")
                cookie[1].log("value:")
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
            data = this.toString()
            codeState = optString("code")
        }
        return this
    }
}

class ItemView(itemType: Int) : ZLRecyclerViewItemView<SettingItem>(itemType, R.layout.item_list) {

    override fun initViews(helper: BaseViewHolder, item: Any) {
        (item as SettingItem).apply {
//            helper.setText(R.id.txt_setting_title, item.title)
//            helper.setText(R.id.txt_tip, item.tip)
        }
    }
}
class SettingItem(override val itemType: Int) : ZLViewModel() {

    var title = ""
    var isCatch = true
    var tip = ""

    constructor(itemType: Int,title: String, isCatch: Boolean) : this(itemType) {
        this.title = title
        this.isCatch = isCatch
        tip = ""
    }

    constructor(itemType: Int,title: String, isCatch: Boolean, tip: String): this(itemType)  {
        this.title = title
        this.isCatch = isCatch
        this.tip = tip
    }

}