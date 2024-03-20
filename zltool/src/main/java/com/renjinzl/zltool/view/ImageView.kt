package com.renjinzl.zltool.view

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.updateLayoutParams
import com.bumptech.glide.Glide
import com.renjinzl.zltool.view.model.ImageModel
import com.renjinzl.zltool.view.utils.router.ZLRouter
import okhttp3.*
import java.io.File


/**
 * name
 * @author   renjinzl
 * date     2022-06-18 14:14
 *
 * version  1.0.0   update 2022-06-18 14:14   起航
 * @since 描述
 */

/**
 * 加载图片
 *
 * @param url url
 * @param see 是否可看大图
 */
fun ImageView.setImageUrl(url: String?, see: Boolean = false): ImageView {
    url?.let {
        if (url.startsWith("http")) {
            Glide.with(context).load(url).into(this)
        } else {
            Glide.with(this).load(File(url)).into(this)
        }
        if (see){
//            setOnClickListener {
//                CheckImageView(context).funDismiss().showFullScreen {dialog ->
//                    setImage(url)
//                }
//            }
        }
    }
    return this
}

/**
 * 加载图片
 */
fun ImageView.setImageModel(imageModel: ImageModel): ImageView {
    setImageUrl(imageModel.url,false)
    if (imageModel.targetModel.mode.isNotEmpty()){
        setOnClickListener {
//            imageModel.targetModel.go(context)
//            "在开发".toast(context)
            ZLRouter.go(context, imageModel.targetModel.mode, imageModel.targetModel.param)
        }
    }
    return this
}
/**
 * 加载图片
 */
fun ImageView.setImageModel(imageModel: ImageModel, mWidth : Int): ImageView {
    if (imageModel.width * imageModel.height * mWidth > 0) {
        updateLayoutParams<ViewGroup.LayoutParams> {
            height = imageModel.height * mWidth / imageModel.width
        }
    }
    setImageModel(imageModel)
    return this
}

/**
 * 加载图片 定高，宽改变
 */
fun ImageView.setImageModelWoundedHeight(imageModel: ImageModel,mHeight : Int): ImageView {
    if (imageModel.width * imageModel.height * mHeight > 0) {
        updateLayoutParams<ViewGroup.LayoutParams> {
            width = imageModel.width * mHeight / imageModel.height
        }
    }
    setImageModel(imageModel)
    return this
}


//fun ImageView.uploadPhoto(path:String , progressView : View? , block : (String) -> Unit) {
//    progressView?.visibility = View.VISIBLE
//    "在开发".toast(context)
//    val bean = UploadFileBean(path, UploadFileBean.TYPE_PIC)
//    bean.upLoadType = HttpMediaRequest.UpLoadType.GALLERY
//    bean.setOnSingleFileUploadListener(object : OnSingleFileUploadListener {
//        override fun onSingleProgress(id: String?, path: String?, currentSize: Long, totalSize: Long, progress: Float) {
//            if (progressView is TextView){
//                progressView.text = progress.toString()
//            }
//        }
//
//        override fun onSingleSucceed(id: String?, path: String?, url: String, requestTag: String?) {
//            block(url)
//            progressView?.visibility = View.GONE
//        }
//
//        override fun onSingleFailed(id: String?, path: String?, resultCode: Int, requestTag: String?) {
//            progressView?.visibility = View.GONE
//        }
//    })
//    bean.startUpload()
//}