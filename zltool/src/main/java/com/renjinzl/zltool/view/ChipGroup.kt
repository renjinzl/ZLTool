package com.renjinzl.zltool.view


import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

/**
 * ChipGroup
 * @author   renjinzl
 * date     2022-06-14 13:52
 *
 * version  1.0.0   update 2022-06-14 13:52   起航
 * @since ChipGroup
 */
fun <T : ChipGroupItemEntity> ChipGroup.setData(data: MutableList<T>) {
    removeAllViews()
    for (item in data) {
        val chip = TextView(context)
        chip.setBackgroundColor(Color.RED)
        chip.setPadding(dp2px(5), dp2px(2), dp2px(5), dp2px(2))
        chip.round(dp2px(10))
        chip.textSize(12)
        chip.text = item.chipName
        addView(chip, this.childCount, ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT))
    }
}

fun ChipGroup.setDataString(data: MutableList<String>) {
    removeAllViews()
    for (item in data) {
        val chip = TextView(context)
        chip.setBackgroundColor(Color.RED)
        chip.setPadding(dp2px(5), dp2px(2), dp2px(5), dp2px(2))
        chip.round(dp2px(10))
        chip.textSize(12)
        chip.text = item
        addView(chip, this.childCount, ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, ChipGroup.LayoutParams.WRAP_CONTENT))
    }
}

/**
 * 数据自定义
 */
inline fun <T, reified R : View> ChipGroup.setData(data: MutableList<T>,height:Int = ChipGroup.LayoutParams.WRAP_CONTENT, block: ChipGroup.(T,R?) -> R) {
//    for (item in data){
//        val chip  = block(item)
//        addView(chip,this.childCount,ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT,ChipGroup.LayoutParams.WRAP_CONTENT))
//    }

    if (data.isEmpty()) {
        removeAllViews()
    } else {
        if (childCount > data.size) {
            removeViews(data.size,childCount - data.size)
        }
        for (i in 0 until data.size) {
            var view: View? = getChildAt(i)
            if (view != null) {
                if (view is R){
                    block(data[i], view)
                } else {
                    removeView(view)
                    view = block(data[i],null)
                    addView(view, i, ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, height))
                }
            } else {
                view = block(data[i],null)
                addView(view, this.childCount, ChipGroup.LayoutParams(ChipGroup.LayoutParams.WRAP_CONTENT, height))
            }
            view.id = i
        }
    }

}

/**
 * 常用配置
 */
fun ChipGroup.configGeneral(): ChipGroup {
    chipSpacingHorizontal = dp2px(2)
    chipSpacingVertical = dp2px(5)
    return this
}

interface ChipGroupItemEntity{
    val chipName : String
}