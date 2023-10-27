package com.renjinzl.zltool.view

import android.os.Build
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

/**
 * name
 * author   renjinzl

 * date     2022-07-06 09:44
 *
 * version  1.0.0   update 2022-07-06 09:44   起航
 * @since 描述
 */
fun TabLayout.selected(block : (Int) -> Unit) : TabLayout{
    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            block(tab.position)
        }
        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabReselected(tab: TabLayout.Tab) {}
    })

    return this
}

/**
 * 隐藏提示
 */
fun TabLayout.hideToolTipText() : TabLayout{
    for (index in 0..tabCount){
        getTabAt(index)?.hideToolTipText()
    }
    return this
}

fun TabLayout.bindViewPager(viewPager : ViewPager) : TabLayout{
    setupWithViewPager(viewPager)
    hideToolTipText()
    return this
}



private fun TabLayout.Tab.hideToolTipText() : TabLayout.Tab{
    // 取消长按事件
    view.isLongClickable = false
    // api 26 以上 设置空text
    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
        view.tooltipText = ""
    }
    return this
}

fun <T : TabEntity> TabLayout.setTab(tabList: MutableList<T>){
    removeAllTabs()
    tabList.forEach {
        addTab(newTab().setText(it.tabText).hideToolTipText())
    }
}

fun TabLayout.initTab(vararg titleList: String){
    removeAllTabs()
    titleList.forEach {
        addTab(newTab().setText(it).hideToolTipText())
    }
}

/**
 * 带初始化选中
 */
fun <T : TabEntity> TabLayout.initTab(tabList: MutableList<T>){
    removeAllTabs()
    var selectedIndex = 0
    tabList.forEachIndexed { index, t ->
        addTab(newTab().setText(t.tabText).hideToolTipText())
        if (t.isSelectedTab){
            selectedIndex = index
        }
    }
    selectTab(getTabAt(selectedIndex))
}

interface TabEntity {
    val tabText : String
    val isSelectedTab : Boolean
}