package com.renjinzl.zltool.view.utils

import android.os.Build
import android.util.Log
import androidx.viewbinding.BuildConfig

class ZLLog {
    companion object{
        fun d(string: String){
            Log.d("tools",string)
        }
    }
}