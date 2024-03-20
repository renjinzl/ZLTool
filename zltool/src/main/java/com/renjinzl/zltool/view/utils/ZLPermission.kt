package com.renjinzl.zltool.view.utils

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionMediator
import com.permissionx.guolindev.PermissionX

/**
 * name
 * author   renjinzl

 * date     2023-11-13 21:32
 *
 * version  1.0.0   update 2023-11-13 21:32   起航
 * @since 描述
 */
class ZLPermission {

    companion object {
        fun init(fragment: Fragment) : ZLPermission{
            return ZLPermission(fragment)
        }
        fun init(activity: FragmentActivity) : ZLPermission{
            return ZLPermission(activity)
        }
    }

    private var permissionMediator: PermissionMediator

    constructor(fragment: Fragment){
        permissionMediator = PermissionMediator(fragment)
    }

    constructor(activity: FragmentActivity){
        permissionMediator = PermissionMediator(activity)
    }

    fun applyPermissions(vararg permissions: String, block: (Boolean) -> Unit) {
        permissionMediator.permissions(listOf(*permissions)).request { allGranted, grantedList, deniedList ->
            block(allGranted)
        }
    }
}