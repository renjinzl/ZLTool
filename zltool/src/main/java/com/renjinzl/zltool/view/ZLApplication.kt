package com.renjinzl.zltool.view

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import com.renjinzl.zltool.view.utils.ZLPrePreferences

/**
 * name
 * author   renjinzl

 * date     2023-10-31 11:59
 *
 * version  1.0.0   update 2023-10-31 11:59   起航
 * @since 描述
 */
open class ZLApplication  : Application(){
    private val mModuleList = arrayOf("com.renjinzl.tools.base.BaseApplication")
    var inReception : Boolean = false

    companion object{
        val application : Application get() = AppManager.instance().currentActivity().application
    }


    /**
     * 初始化
     */
    private fun init() {
        ZLPrePreferences.instance(this)
//        if (AccountServiceImpl.getInstance().isAgreePrivacy){
//            ThirdAppHelper.init(this)
//        }
    }
}

/**
 * 是否在后台
 */
var Application.isBackground : Boolean get() { return (this as ZLApplication).inReception } set(value) { (this as ZLApplication).inReception = value}

// 取得版本号
//fun Application.getVersion(context: Context): String {
//    return try {
//        val manager = context.packageManager.getPackageInfo(context.packageName, 0)
//        manager.versionName
//    } catch (e: PackageManager.NameNotFoundException) {
//        "Unknown"
//    }
//}