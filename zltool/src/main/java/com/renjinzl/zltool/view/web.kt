package com.renjinzl.zltool.view

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Handler
import android.text.TextUtils
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView

/**
 * name
 * author   renjinzl

 * date     2022-07-20 18:11
 *
 * version  1.0.0   update 2022-07-20 18:11   起航
 * @since 描述
 */
fun WebView.initView(activity: Activity?){

    ////////////////////////////////////////////////////////////////////////////////////////////
    this.settings.allowUniversalAccessFromFileURLs = true
    this.settings.allowFileAccess = true

    this.isVerticalScrollBarEnabled = false
    this.isHorizontalScrollBarEnabled = false

    //// 通知javascript来自动打开的窗口。这适用于JavaScript函数open()窗口。默认为false。
    this.settings.javaScriptCanOpenWindowsAutomatically = true

    //// 通知webView适应各种视图：双击变大变小，手动放大后，双击可以恢复到原始
    this.settings.useWideViewPort = false

    //// 设置可以访问文件
    this.settings.allowFileAccess = true

    this.settings.textZoom = 100

    //设置 缓存模式webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 开启 DOM storage API 功能
    this.settings.domStorageEnabled = true

    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
    activity?.let {
        webChromeClient = ChromeClient(activity)
    }
    //// Android可以调用JS
    this.settings.javaScriptEnabled = true
}

class ChromeClient(val activity: Activity) : WebChromeClient() {
    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        activity.title = title
    }
}