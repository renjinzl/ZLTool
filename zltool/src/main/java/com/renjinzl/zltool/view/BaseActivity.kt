package com.renjinzl.zltool.view

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.renjinzl.zltool.R
import com.renjinzl.zltool.view.utils.HeadHelper
import com.renjinzl.zltool.view.utils.ZLUriUtils


abstract class BaseActivity<T : ViewBinding>(private val bindingLayout : Int = 0) : AppCompatActivity() {

    /**
     * 是否有头部
     */
    open fun hasContentHead(): Boolean {
        return true
    }

    /**
     * 是否全屏
     */
    open fun isFullScreen(): Boolean {
        return false
    }

    /**
     * 页面布局
     */
    open fun getHeadLayout(): Int {
        return 0
    }

    @Deprecated("不用了，使用第三方替代")
    var blockResult: ((pathList: List<String>) -> Unit)? = null

    var headHelper: HeadHelper? = null

    protected var _binding: T? = null

    protected val binding get() = _binding!!

    protected abstract fun getViewBinding(): T

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        if (isFullScreen()) {
//            enableEdgeToEdge()
            setScreenFullNavigationBar()
        }
//        statusBarColor(true)
        AppManager.instance().addActivity(this)
        super.onCreate(savedInstanceState)

        _binding = getViewBinding()

        if (getHeadLayout() != 0) {
            super.setContentView(R.layout.zl_activity_head_layout)
            layoutInflater.inflate(getHeadLayout(), findViewById(R.id.head_layout))
            findViewById<FrameLayout>(R.id.content_layout).addView(binding.root)
        } else {
            super.setContentView(binding.root)
        }

        initView()

    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
        title?.toString()?.log("-----title-----")
        headHelper?.setTitle(title)
    }


    @Deprecated("不用了，使用第三方替代")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //只选择了一个
        data?.apply {
            val urlList = arrayListOf<String>()

            val selectedVideo = data.data
            val tempPath: String = selectedVideo?.let {
                ZLUriUtils.uri2Path(context, selectedVideo) ?: ""
            }?:""



            if (!TextUtils.isEmpty(tempPath)) {
                urlList.add(tempPath)
            }else{
                data.clipData?.apply {
                    for (i in 0 until itemCount){
                        ZLUriUtils.uri2Path(context, getItemAt(i).uri)?.let {
                            urlList.add(it)
                        }
                    }
                }
            }

            if (urlList.size > 0) {
                blockResult?.invoke(urlList)
            }

        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == ZLGallery.GALLERY_REQUEST_CODE) {
//            /*** 选图回调  */
////            this.setCurrentMode(MODE_IMAGE)
//            val pathList: List<String>? = data?.getStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH)
//            if (pathList != null && pathList.isNotEmpty()) {
////                originPicturesCallBack(pathList)//是否需要压缩
//                blockResult?.invoke(pathList)
////                Glide.with(context).load(File(path)).into(this)
//            }
//        }
//    }

}