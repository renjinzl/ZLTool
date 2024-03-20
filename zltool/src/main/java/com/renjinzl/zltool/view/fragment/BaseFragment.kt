package com.renjinzl.zltool.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewbinding.ViewBinding
import com.renjinzl.zltool.view.ZLFragment

/**

 * Author   renjinzl
 * Date     2022-06-13 10:46
 * Email    renjinzl@163.com
 * Version  1.0.0   @Update 2022-06-13 10:46   起航
 * Describe 描述
 */
abstract class BaseFragment<T : ViewBinding> : ZLFragment() {

    var blockPermissions : ((grantResults: IntArray) -> Unit)? = null

    var blockResult : ((pathList: List<String>) -> Unit)? = null

    var blockCurrent : ((Intent) -> Unit)? = null

    /**
     * 页面布局
     */
    open fun getContentLayout(): Int {
        return 0
    }

    protected lateinit var binding: T

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (getContentLayout() != 0) {
            layoutInflater.inflate(getContentLayout(), container, false)
            binding = DataBindingUtil.inflate(inflater,getContentLayout(), container, false)
        }
        iniView()
        return binding.root
    }

    abstract fun iniView()

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == EPetPermissions.PERMISSION_REQUEST_CODE){
//            blockPermissions?.invoke(grantResults)
//        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == GALLERY_REQUEST_CODE) {
//            /*** 选图回调  */
////            this.setCurrentMode(MODE_IMAGE)
//            val pathList: List<String>? = data?.getStringArrayListExtra(MatisseActivity.EXTRA_RESULT_SELECTION_PATH)
//            if (pathList != null && pathList.isNotEmpty()) {
////                originPicturesCallBack(pathList)//是否需要压缩
//                blockResult?.invoke(pathList)
////                Glide.with(context).load(File(path)).into(this)
//            }
//        }else{
//            data?.let {
//                blockCurrent?.invoke(it)
//            }
//        }
    }


}