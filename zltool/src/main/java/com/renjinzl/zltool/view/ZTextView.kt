package com.renjinzl.zltool.view

import android.widget.ImageView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.renjinzl.zltool.view.model.ImageModel

/**
 * name
 * author   renjinzl

 * date     2023-10-31 14:40
 *
 * version  1.0.0   update 2023-10-31 14:40   起航
 * @since 描述
 */


fun BaseViewHolder.setImageUrl(@IdRes viewId: Int, url: String): BaseViewHolder {
    getView<ImageView>(viewId).setImageUrl(url)
    return this
}

fun BaseViewHolder.setImageModel(@IdRes viewId: Int, model: ImageModel): BaseViewHolder {
    getView<ImageView>(viewId).setImageModel(model)
    return this
}
fun BaseViewHolder.setImageModel(@IdRes viewId: Int, model: ImageModel, mWidth : Int): BaseViewHolder {
    getView<ImageView>(viewId).setImageModel(model,mWidth)
    return this
}
fun BaseViewHolder.setImageModelWoundedHeight(@IdRes viewId: Int, model: ImageModel, mHeight : Int): BaseViewHolder {
    getView<ImageView>(viewId).setImageModelWoundedHeight(model,mHeight)
    return this
}