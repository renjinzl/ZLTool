package com.renjinzl.zltool.view

import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import com.renjinzl.zltool.R

/**
 * name
 * @author   renjinzl
 * date     2022-06-15 17:30
 *
 * version  1.0.0   update 2022-06-15 17:30   起航
 * @since 描述
 */
fun Spinner.setData(list: MutableList<String>) {
    val mAdapter: SpinnerAdapter?
    if (adapter == null || adapter is ArrayAdapter<*>) {
        mAdapter = ArrayAdapter<CharSequence>(context, android.R.layout.simple_spinner_item)
        mAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
    } else {
        mAdapter = adapter as ArrayAdapter<CharSequence>
        mAdapter.clear()
    }
    for (tempItem in list) {
        mAdapter.add(tempItem)
    }
}